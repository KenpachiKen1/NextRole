package com.kenneth.nextrole.dto.jobposting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JobPostingResponse {

    private Long id;

    private String title;

    private String location;

    private Double salary;

    private String postingUrl;

    private Long companyId;

    private String companyName;

    private String reqCode;
}