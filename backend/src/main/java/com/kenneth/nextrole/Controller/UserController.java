package com.kenneth.nextrole.Controller;


import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Service.UserService;
import com.kenneth.nextrole.dto.user.UpdateUserRequest;
import com.kenneth.nextrole.dto.user.UserResponse;
import com.kenneth.nextrole.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    public UserController (UserService userService){
        this.userService = userService;
    }


    //Adjust these to account for exceptions
    @PutMapping("/updateAccount")
    public ResponseEntity<UserResponse> updateAccount (@Valid @RequestBody UpdateUserRequest request,
                                                       Authentication authentication){
        String email = authentication.getName(); // the email
        UserResponse response = userService.updateCurrentUser(request, email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity <UserResponse> profile (@AuthenticationPrincipal CustomUserPrincipal userPrincipal){
        User user = userPrincipal.getUser();
        UserResponse response = userService.getUserProfile(user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity <String> deleteAccount(@AuthenticationPrincipal CustomUserPrincipal userPrincipal){
        userService.deleteAccount(userPrincipal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted account");
    }

}
