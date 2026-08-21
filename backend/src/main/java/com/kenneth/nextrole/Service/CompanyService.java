package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.Company;
import com.kenneth.nextrole.Repository.CompanyRepository;
import com.kenneth.nextrole.dto.company.CompanyResponse;
import com.kenneth.nextrole.dto.company.CreateCompanyRequest;
import com.kenneth.nextrole.exception.CompanyAlreadyExistsException;
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

                .companyWebsite(company.getCompanyWebsite())
                .build();
    }

    @Transactional
    public CompanyResponse registerCompany(CreateCompanyRequest request) {
        if (companyRepository.existsByName(request.getName())) {
            throw new CompanyAlreadyExistsException("Company already exists");
        }

        Company company = Company.builder()
                .name(request.getName())
                .companyWebsite(request.getCompanyWebsite())
                .build();

        company = companyRepository.save(company);
        return toResponse(company);
    }

    public List<CompanyResponse> listAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CompanyResponse getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return toResponse(company);
    }

    public CompanyResponse findByName(String name) {
        Company company = companyRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        return toResponse(company);
    }

    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
}
