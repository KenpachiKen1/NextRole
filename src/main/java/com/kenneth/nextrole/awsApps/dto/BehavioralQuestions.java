package com.kenneth.nextrole.awsApps.dto;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehavioralQuestions {
    private String question;
    private String reason;
}
