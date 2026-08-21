package com.kenneth.nextrole.dto.resume;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateResumeRequest {

    private Long fileSize;
    private String resumeTitle;

}
