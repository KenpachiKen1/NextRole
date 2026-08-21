package com.kenneth.nextrole.awsApps.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResumeTailoringRequest {

    @NotNull
    private Long resumeId;

    @NotNull
    private Long jobPostingId;
}
