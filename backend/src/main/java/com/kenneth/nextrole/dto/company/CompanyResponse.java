package com.kenneth.nextrole.dto.company;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyResponse {

    private Long id;
    private String name;
    private String companyPhoto;
    private String companyWebsite;
}