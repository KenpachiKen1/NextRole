package com.kenneth.nextrole.awsApps.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewPrepResponse {

    private List<TechnicalQuestions> technicalQuestions;

    private List<BehavioralQuestions> behavioralQuestions;
}
