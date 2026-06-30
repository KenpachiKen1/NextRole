package com.kenneth.nextrole.dto.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    private String username;
    private String profilePhoto;
    private String email;

}
