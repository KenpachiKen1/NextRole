package com.kenneth.nextrole.awsApps.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentActionRequest {

    @NotBlank (message = "An action is required")
    private String action; //The action the user wants the agent to make
}
