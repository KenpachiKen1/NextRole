package com.kenneth.nextrole.Service;
import com.kenneth.nextrole.Model.Company;
import com.kenneth.nextrole.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService (CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public Company registerCompany(Company company){
        return companyRepository.save(company);
    }

    public List<Company> listAllCompanies(){
        return companyRepository.findAll();
    }

    public Optional<Company> findByName (String name){
        return companyRepository.findByName(name);
    }

    public boolean existsByName(String name){
        return companyRepository.existsByName(name);
    }



}
