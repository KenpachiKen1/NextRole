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
import com.kenneth.nextrole.dto.jobposting.UpdateJobPostingRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class JobPostingService {


    private final JobPostingRepository jobPostingRepository;
    private final JobInfoExtractorAgent agent;
    private final CompanyRepository companyRepository;
    private final KeywordRepository keywordRepository;

    public JobPostingService(JobPostingRepository jobPostingRepository,
                             JobInfoExtractorAgent agent,
                             CompanyRepository companyRepository,
                             KeywordRepository keywordRepository) {
        this.agent = agent;
        this.jobPostingRepository = jobPostingRepository;
        this.companyRepository = companyRepository;
        this.keywordRepository = keywordRepository;
    }

    private JobPostingResponse toResponse(JobPosting jp) {
        Company company = jp.getCompany();

        return JobPostingResponse.builder()
                .id(jp.getId())
                .title(jp.getTitle())
                .location(jp.getLocation())
                .salary(jp.getSalary())
                .postingUrl(jp.getPostingUrl())
                .companyId(company != null ? company.getId() : null)
                .companyName(company != null ? company.getName() : null)
                .reqCode(jp.getRequisitionCode())
                .jobDescription(jp.getDescription())
                .requiredSkills(toKeywordNames(jp.getRequiredKeywords()))
                .preferredSkills(toKeywordNames(jp.getPreferredKeywords()))
                .build();
    }

    private Set<String> toKeywordNames(Set<Keyword> keywords) {
        return keywords.stream()
                .map(Keyword::getKeyword)
                .collect(Collectors.toSet());
    }

    /**
     * Finds an existing keyword by name, or creates and saves a new one.
     */
    private Keyword findOrCreateKeyword(String word) {
        return keywordRepository.findByKeyword(word)
                .orElseGet(() -> keywordRepository.save(
                        Keyword.builder()
                                .keyword(word)
                                .build()
                ));
    }

    // admin (me) or agent work
    /*
        Future updates would probably be that my agent would rescrape the job posting if it's still available.
     */
    @Transactional(rollbackFor = Exception.class)
    public JobPostingResponse createJobPosting(CreateJobPostingRequest request) throws IOException {
        JobExtractionResponse rp = agent.generateJobDetails(request.getPostingUrl());

        Company company = companyRepository.findByName(rp.getCompanyName())
                .orElseGet(() -> companyRepository.save(
                        Company.builder()
                                .companyWebsite(rp.getCompanyWebsite())
                                .name(rp.getCompanyName())
                                .build()
                ));

        JobPosting jp = JobPosting.builder()
                .title(rp.getJobTitle())
                .salary(rp.getSalary())
                .company(company)
                .location(rp.getLocation())
                .postingUrl(request.getPostingUrl())
                .requisitionCode(rp.getRequisitionCode())
                .description(rp.getJobDescription())
                .build();

        if (rp.getPreferredSkills() != null) {
            for (String word : rp.getPreferredSkills()) {
                jp.getPreferredKeywords().add(findOrCreateKeyword(word));
            }
        }

        if (rp.getRequiredSkills() != null) {
            for (String word : rp.getRequiredSkills()) {
                jp.getRequiredKeywords().add(findOrCreateKeyword(word));
            }
        }

        jp = jobPostingRepository.save(jp);
        return toResponse(jp);
    }

    @Transactional(rollbackFor = Exception.class)
    public JobPostingResponse updateJobPosting(Long postingId, UpdateJobPostingRequest request) {
        JobPosting jp = jobPostingRepository.findById(postingId)
                .orElseThrow(() -> new EntityNotFoundException("Job Posting not found"));

        if (request.getSalary() != null) {
            jp.setSalary(request.getSalary());
        }

        if (request.getRequisitionCode() != null) {
            jp.setRequisitionCode(request.getRequisitionCode());
        }

        if (request.getLocation() != null) {
            jp.setLocation(request.getLocation());
        }

        if (request.getPostingUrl() != null) {
            jp.setPostingUrl(request.getPostingUrl());
        }

        if (request.getTitle() != null) {
            jp.setTitle(request.getTitle());
        }

        jp = jobPostingRepository.save(jp);
        return toResponse(jp);
    }

    public JobPostingResponse getJobPostingById(Long id) {
        JobPosting jp = jobPostingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Posting not found"));
        return toResponse(jp);
    }

    public List<JobPostingResponse> getAllJobPostings() {
        return jobPostingRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<JobPostingResponse> getByCompany(Long companyId) {
        return jobPostingRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<JobPostingResponse> searchByTitle(String title) {
        if (title == null || title.isBlank()) {
            // limit 10 just to prevent sending back the entire list
            return jobPostingRepository.findRandom10().stream()
                    .map(this::toResponse)
                    .toList();
        }

        return jobPostingRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toResponse)
                .toList();
    }


}