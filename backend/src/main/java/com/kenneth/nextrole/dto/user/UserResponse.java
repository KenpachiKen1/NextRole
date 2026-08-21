package com.kenneth.nextrole.dto.user;


import com.kenneth.nextrole.SubscriptionStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String profilePhoto;
    private SubscriptionStatus status;
    private String first_name;
    private String last_name;
}
