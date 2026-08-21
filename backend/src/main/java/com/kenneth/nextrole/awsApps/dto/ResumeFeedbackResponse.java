package com.kenneth.nextrole.awsApps.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeFeedbackResponse {

    private Integer overallResumeScore;

    private List<String> strengths;

    private List<String> weaknesses;

    private List<Recommendation> recommendations;

    private String bestResumeBullet;

    private List<String> atsWarnings;
}