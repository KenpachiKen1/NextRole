package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.Company;
import com.kenneth.nextrole.Repository.CompanyRepository;
import com.kenneth.nextrole.dto.company.CompanyResponse;
import com.kenneth.nextrole.dto.company.CreateCompanyRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;


    @Test
    void registerCompany_savesCompany_whenNameNotTaken() {
        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setName("Anthropic");
        request.setCompanyWebsite("https://anthropic.com");

        Company saved = Company.builder()
                .id(1L)
                .name("Anthropic")
                .companyWebsite("https://anthropic.com")
                .build();

        when(companyRepository.existsByName("Anthropic")).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenReturn(saved);

        CompanyResponse response = companyService.registerCompany(request);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Anthropic");
        assertThat(captor.getValue().getCompanyWebsite()).isEqualTo("https://anthropic.com");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Anthropic");
    }

    @Test
    void registerCompany_throwsIllegalArgumentException_whenNameAlreadyExists() {
        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setName("Anthropic");

        when(companyRepository.existsByName("Anthropic")).thenReturn(true);

        assertThatThrownBy(() -> companyService.registerCompany(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(companyRepository, never()).save(any());
    }

    @Test
    void listAllCompanies_returnsAllCompaniesMappedToResponses() {
        Company anthropic = Company.builder().id(1L).name("Anthropic").build();
        Company openai = Company.builder().id(2L).name("OpenAI").build();

        when(companyRepository.findAll()).thenReturn(List.of(anthropic, openai));

        List<CompanyResponse> responses = companyService.listAllCompanies();

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(CompanyResponse::getName)
                .containsExactlyInAnyOrder("Anthropic", "OpenAI");
    }

    @Test
    void listAllCompanies_returnsEmptyList_whenNoneExist() {
        when(companyRepository.findAll()).thenReturn(List.of());

        List<CompanyResponse> responses = companyService.listAllCompanies();

        assertThat(responses).isEmpty();
    }

    @Test
    void getCompanyById_returnsCompany_whenFound() {
        Company company = Company.builder().id(1L).name("Anthropic").build();

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        CompanyResponse response = companyService.getCompanyById(1L);

        assertThat(response.getName()).isEqualTo("Anthropic");
    }

    @Test
    void getCompanyById_throwsEntityNotFound_whenMissing() {
        when(companyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.getCompanyById(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void findByName_returnsCompany_whenFound() {
        Company company = Company.builder().id(1L).name("Anthropic").build();

        when(companyRepository.findByName("Anthropic")).thenReturn(Optional.of(company));

        CompanyResponse response = companyService.findByName("Anthropic");

        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void findByName_throwsEntityNotFound_whenMissing() {
        when(companyRepository.findByName("Ghost Inc")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.findByName("Ghost Inc"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void existsByName_delegatesDirectlyToRepository() {
        when(companyRepository.existsByName("Anthropic")).thenReturn(true);

        boolean exists = companyService.existsByName("Anthropic");

        assertThat(exists).isTrue();
        verify(companyRepository).existsByName("Anthropic");
    }
}