package com.kenneth.nextrole.dto.jobposting;



import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateJobPostingRequest {

    private String title;
    private String location;
    private Double salary;
    private String postingUrl;
    private String employmentType;
    private Long companyId;
    private String requisitionCode;

}
