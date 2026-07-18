package com.kenneth.nextrole.Tools.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JobExtractionResponse {

    private String companyName;
    private String jobTitle;
    private String location;
    private String employmentType;
    private Double salary;
    private String requisitionCode;
    private String companyWebsite;
    private List<String> requiredSkills;
    private List<String> preferredSkills;
    private String jobDescription;
}