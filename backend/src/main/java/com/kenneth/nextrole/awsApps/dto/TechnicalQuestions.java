package com.kenneth.nextrole.awsApps.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalQuestions {
    private String question;
    private String reason;
}
