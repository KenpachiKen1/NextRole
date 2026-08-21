package com.kenneth.nextrole.awsApps.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
public class InterviewPrepRequest {


    @NotNull
    private Long jobPostingId;

    @NotNull
    private Long resumeId;
}
