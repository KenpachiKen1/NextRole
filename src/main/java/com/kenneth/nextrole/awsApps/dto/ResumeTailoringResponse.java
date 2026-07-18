package com.kenneth.nextrole.awsApps.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeTailoringResponse {

    private Integer matchScore;

    private List<String> strengths;

    private List<String> missingKeywords;

    private List<Recommendation> recommendations;
}