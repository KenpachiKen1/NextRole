package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.Company;
import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.Keyword;
import com.kenneth.nextrole.Repository.CompanyRepository;
import com.kenneth.nextrole.Repository.JobPostingRepository;
import com.kenneth.nextrole.Repository.KeywordRepository;
import com.kenneth.nextrole.Tools.JobInfoExtractorAgent;
import com.kenneth.nextrole.Tools.dto.JobExtractionResponse;
import com.kenneth.nextrole.dto.jobposting.CreateJobPostingRequest;
import com.kenneth.nextrole.dto.jobposting.JobPostingResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JobPostingServiceTest {

    @Mock private JobPostingRepository jobPostingRepository;
    @Mock private JobInfoExtractorAgent agent;
    @Mock private CompanyRepository companyRepository;
    @Mock private KeywordRepository keywordRepository;

    @InjectMocks
    private JobPostingService jobPostingService;

    @Test
    void createJobPosting_reusesExistingCompany_whenNameAlreadyExists() throws Exception {
        CreateJobPostingRequest request = new CreateJobPostingRequest();
        request.setPostingUrl("https://jobs.example.com/123");

        JobExtractionResponse extracted = JobExtractionResponse.builder()
                .companyName("Anthropic")
                .jobTitle("Backend Engineer")
                .location("Remote")
                .salary(150000.0)
                .requiredSkills(List.of())
                .preferredSkills(List.of())
                .build();

        Company existingCompany = Company.builder().name("Anthropic").build();
        JobPosting savedPosting = JobPosting.builder()
                .title("Backend Engineer")
                .company(existingCompany)
                .build();

        when(agent.generateJobDetails("https://jobs.example.com/123")).thenReturn(extracted);
        when(companyRepository.existsByName("Anthropic")).thenReturn(true);
        when(companyRepository.findByName("Anthropic")).thenReturn(Optional.of(existingCompany));
        when(jobPostingRepository.save(any(JobPosting.class))).thenReturn(savedPosting);

        JobPostingResponse response = jobPostingService.createJobPosting(request);

        // key assertion: never create a duplicate company row
        verify(companyRepository, never()).save(any());
        verify(companyRepository).findByName("Anthropic");
        assertThat(response.getCompanyName()).isEqualTo("Anthropic");
    }

    @Test
    void createJobPosting_createsNewCompany_whenNameDoesNotExist() throws Exception {
        CreateJobPostingRequest request = new CreateJobPostingRequest();
        request.setPostingUrl("https://jobs.example.com/456");

        JobExtractionResponse extracted = JobExtractionResponse.builder()
                .companyName("NewCo")
                .companyWebsite("https://newco.com")
                .jobTitle("SWE")
                .salary(120000.0)
                .requiredSkills(List.of())
                .preferredSkills(List.of())
                .build();

        Company newCompany = Company.builder().name("NewCo").companyWebsite("https://newco.com").build();
        JobPosting savedPosting = JobPosting.builder().title("SWE").company(newCompany).build();

        when(agent.generateJobDetails(anyString())).thenReturn(extracted);
        when(companyRepository.existsByName("NewCo")).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenReturn(newCompany);
        when(jobPostingRepository.save(any(JobPosting.class))).thenReturn(savedPosting);

        jobPostingService.createJobPosting(request);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("NewCo");
        // never looked up a company that doesn't exist yet
        verify(companyRepository, never()).findByName(any());
    }

    @Test
    void createJobPosting_reusesExistingKeyword_insteadOfCreatingDuplicate() throws Exception {
        CreateJobPostingRequest request = new CreateJobPostingRequest();
        request.setPostingUrl("https://jobs.example.com/789");

        JobExtractionResponse extracted = JobExtractionResponse.builder()
                .companyName("Anthropic")
                .jobTitle("Backend Engineer")
                .salary(150000.0)
                .requiredSkills(List.of("Java")) // already exists in the DB
                .preferredSkills(List.of())
                .build();

        Company company = Company.builder().name("Anthropic").build();
        Keyword existingJavaKeyword = Keyword.builder().keyword("Java").build();

        when(agent.generateJobDetails(anyString())).thenReturn(extracted);
        when(companyRepository.existsByName("Anthropic")).thenReturn(true);
        when(companyRepository.findByName("Anthropic")).thenReturn(Optional.of(company));
        when(keywordRepository.findByKeyword("Java")).thenReturn(Optional.of(existingJavaKeyword));
        when(jobPostingRepository.save(any(JobPosting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // echo back what was saved

        jobPostingService.createJobPosting(request);

        // should reuse the existing keyword row, never create a new one
        verify(keywordRepository, never()).save(any());
    }

    @Test
    void createJobPosting_createsNewKeyword_whenNotFoundInDb() throws Exception {
        CreateJobPostingRequest request = new CreateJobPostingRequest();
        request.setPostingUrl("https://jobs.example.com/999");

        JobExtractionResponse extracted = JobExtractionResponse.builder()
                .companyName("Anthropic")
                .jobTitle("Backend Engineer")
                .salary(150000.0)
                .requiredSkills(List.of("Kubernetes")) // brand new skill
                .preferredSkills(List.of())
                .build();

        Company company = Company.builder().name("Anthropic").build();
        Keyword newKeyword = Keyword.builder().keyword("Kubernetes").build();

        when(agent.generateJobDetails(anyString())).thenReturn(extracted);
        when(companyRepository.existsByName("Anthropic")).thenReturn(true);
        when(companyRepository.findByName("Anthropic")).thenReturn(Optional.of(company));
        when(keywordRepository.findByKeyword("Kubernetes")).thenReturn(Optional.empty());
        when(keywordRepository.save(any(Keyword.class))).thenReturn(newKeyword);
        when(jobPostingRepository.save(any(JobPosting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        jobPostingService.createJobPosting(request);

        verify(keywordRepository).save(any(Keyword.class));
    }

    /*
     This test used to prove a bug: JobPosting.salary was a primitive double,
     so a null salary from the extractor threw an NPE. Now that
     JobPosting.salary is a boxed Double, null should flow through cleanly —
     this test locks that fix in place so nobody accidentally reverts it.
    */
    @Test
    void createJobPosting_handlesNullSalary_whenNotListedInPosting() throws Exception {
        CreateJobPostingRequest request = new CreateJobPostingRequest();
        request.setPostingUrl("https://jobs.example.com/no-salary-listed");

        JobExtractionResponse extractedWithNoSalary = JobExtractionResponse.builder()
                .companyName("Anthropic")
                .jobTitle("Backend Engineer")
                // .salary(...) intentionally left unset -> null
                .requiredSkills(List.of())
                .preferredSkills(List.of())
                .build();

        Company company = Company.builder().name("Anthropic").build();
        JobPosting savedPosting = JobPosting.builder()
                .title("Backend Engineer")
                .salary(null)
                .company(company)
                .build();

        when(agent.generateJobDetails(anyString())).thenReturn(extractedWithNoSalary);
        when(companyRepository.existsByName("Anthropic")).thenReturn(true);
        when(companyRepository.findByName("Anthropic")).thenReturn(Optional.of(company));
        when(jobPostingRepository.save(any(JobPosting.class))).thenReturn(savedPosting);

        JobPostingResponse response = jobPostingService.createJobPosting(request);

        assertThat(response).isNotNull();
        // if JobPostingResponse.salary is also nullable Double, you can add:
        // assertThat(response.getSalary()).isNull();
    }
}