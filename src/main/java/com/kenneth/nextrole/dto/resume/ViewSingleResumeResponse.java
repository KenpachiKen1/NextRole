package com.kenneth.nextrole.dto.resume;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class ViewSingleResumeResponse {

    private Long id;

    private String resumeTitle;

    private Long fileSize;

    private LocalDateTime uploadedAt;

    private String url;
}
