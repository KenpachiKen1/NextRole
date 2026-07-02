package com.kenneth.nextrole.dto.resume;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateResumeRequest { //this is for creating the resume

    @NotNull
    private Long fileSize;
    @NotNull
    private String resumeTitle;


}
