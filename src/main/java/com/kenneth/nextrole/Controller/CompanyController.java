package com.kenneth.nextrole.Controller;

import com.kenneth.nextrole.Service.CompanyService;
import com.kenneth.nextrole.dto.company.CompanyResponse;
import com.kenneth.nextrole.dto.company.CreateCompanyRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Prolly gonna delete this createCompany soon.
     */

    /**
     * Create a company
     * For now, mostly useful for internal/admin/agent testing
     */
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(
            @Valid @RequestBody CreateCompanyRequest request
    ) {
        CompanyResponse response = companyService.registerCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all companies
     */
    @GetMapping("/list")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.listAllCompanies());
    }

    /**
     * Get one company by id
     */
    @GetMapping("/{companyId}/")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyById(companyId));
    }

    /**
     * Find a company by exact name
     * Example:
     * GET /api/companies/search?name=Google
     */
    @GetMapping("/search")
    public ResponseEntity<CompanyResponse> getCompanyByName(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.findByName(name));
    }
}