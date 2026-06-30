package com.kenneth.nextrole.Controller;

import com.kenneth.nextrole.Service.CompanyService;
import com.kenneth.nextrole.dto.company.CompanyResponse;
import com.kenneth.nextrole.dto.company.CreateCompanyRequest;
import com.kenneth.nextrole.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies(@AuthenticationPrincipal CustomUserPrincipal customUserPrincipal) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.listAllCompanies(customUserPrincipal.getUser()));
    }

    /**
     * Get one company by id
     */
    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId, @AuthenticationPrincipal CustomUserPrincipal customUserPrincipal) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getCompanyById(companyId, customUserPrincipal.getUser()));
    }

    /**
     * Find a company by exact name
     * Example:
     * GET /api/companies/search?name=Google
     */
    @GetMapping("/search")
    public ResponseEntity<CompanyResponse> getCompanyByName(@RequestParam String name, @AuthenticationPrincipal CustomUserPrincipal customUserPrincipal) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.findByName(name, customUserPrincipal.getUser()));
    }
}