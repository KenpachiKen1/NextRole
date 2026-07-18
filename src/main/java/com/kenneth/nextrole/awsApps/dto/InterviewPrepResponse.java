package com.kenneth.nextrole.awsApps.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewPrepResponse {

    private String question;

    private String category;
}
