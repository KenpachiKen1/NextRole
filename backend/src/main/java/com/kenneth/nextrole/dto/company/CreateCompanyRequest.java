package com.kenneth.nextrole.dto.company;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    private String name;

    private String companyPhoto;
    private String companyWebsite;
}

