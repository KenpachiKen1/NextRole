package com.kenneth.nextrole.awsApps.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    private String title;

    private String description;
}