package com.kenneth.nextrole.awsApps.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResumeFeedbackRequest {

    @NotNull
    private Long resumeId;
}
