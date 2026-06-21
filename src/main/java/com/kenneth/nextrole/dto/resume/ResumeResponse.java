package com.kenneth.nextrole.dto.resume;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ResumeResponse {


    private Long id;

    private String resumeTitle;

    private String resumeUrl;

    private LocalDateTime uploadedAt;

}
