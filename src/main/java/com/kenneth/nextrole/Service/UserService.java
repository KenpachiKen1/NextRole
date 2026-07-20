package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.UserRepository;
import com.kenneth.nextrole.dto.user.UpdateUserRequest;
import com.kenneth.nextrole.dto.user.UserResponse;
import com.kenneth.nextrole.exception.EmailAlreadyExistsException;
import com.kenneth.nextrole.exception.UsernameAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService( UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public UserResponse toResponse(User user){
        return UserResponse.builder().id(user.getId()).
                username(user.getUsername())
                .first_name(user.getFirstName()).last_name(user.getLastName())
                .email(user.getEmail())
                .profilePhoto(user.getProfilePhoto())
                .status(user.getCustomer().getSubscriptionStatus()).build();
    }

    /*
    Pass the email from the controller to here to authenticate.
     */

    public UserResponse getUserProfile(User user){
        return toResponse(user);
    }


    @Transactional
    public UserResponse updateCurrentUser(UpdateUserRequest request, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found")); //global Exception handler auto calls it

        if (request.getUsername() != null) {
            if (userRepository.existsByUsernameIgnoreCase(request.getUsername())
                    && !user.getUsername().equalsIgnoreCase(request.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already in use");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            if (userRepository.existsByEmail(request.getEmail())
                    && !user.getEmail().equalsIgnoreCase(request.getEmail())) {
                throw new EmailAlreadyExistsException("Email already in use!");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getProfilePhoto() != null) {
            user.setProfilePhoto(request.getProfilePhoto());
        }

        user = userRepository.save(user);
        return toResponse(user);
    }

    public void deleteAccount(String email){

        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);

        SecurityContextHolder.clearContext();
    }

}
