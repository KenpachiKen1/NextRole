package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.Company;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.CompanyRepository;
import com.kenneth.nextrole.dto.company.CompanyResponse;
import com.kenneth.nextrole.dto.company.CreateCompanyRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    private CompanyResponse toResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .companyPhoto(company.getCompanyPhoto())
                .companyWebsite(company.getCompanyWebsite())
                .build();
    }

    @Transactional
    public CompanyResponse registerCompany(CreateCompanyRequest request) {
        if (companyRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Company already exists");
        }

        Company company = Company.builder()
                .name(request.getName())
                .companyPhoto(request.getCompanyPhoto())
                .companyWebsite(request.getCompanyWebsite())
                .build();

        company = companyRepository.save(company);
        return toResponse(company);
    }

    public List<CompanyResponse> listAllCompanies(User user) {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CompanyResponse getCompanyById(Long companyId, User user) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return toResponse(company);
    }

    public CompanyResponse findByName(String name, User user) {
        Company company = companyRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return toResponse(company);
    }

    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
}
