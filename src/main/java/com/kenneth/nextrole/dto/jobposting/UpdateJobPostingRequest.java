package com.kenneth.nextrole.dto.jobposting;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateJobPostingRequest {

    private String title;

    private String location;

    private Double salary;

    private String postingUrl;
    private String requisitionCode;


}
