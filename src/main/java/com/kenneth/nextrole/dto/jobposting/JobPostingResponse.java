package com.kenneth.nextrole.dto.jobposting;

import com.kenneth.nextrole.Model.Keyword;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Set;

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
    private Set<String> requiredSkills;
    private Set<String> preferredSkills;
    private String jobDescription;
}