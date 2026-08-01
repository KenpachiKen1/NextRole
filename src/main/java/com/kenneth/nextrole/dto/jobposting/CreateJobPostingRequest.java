package com.kenneth.nextrole.dto.jobposting;



import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

@Getter @Setter
public class CreateJobPostingRequest {

    private String title;
    private String location;
    private Double salary;
    private String postingUrl;
    private String employmentType;

    @Nullable
    private Long companyId; //if null then the company was not in the database
    private String requisitionCode;

}
