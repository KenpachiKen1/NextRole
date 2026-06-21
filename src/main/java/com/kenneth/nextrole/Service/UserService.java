package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService( UserRepository userRepository ){
        this.userRepository = userRepository;
    }


    //When I start making my DTO's these will change most likely
    public User createUser (User user){
        return userRepository.save(user);
    }

    public User updateUser (User user){
        return userRepository.save(user);
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional <User> findByUsernameIgnoreCase(String username){
            return userRepository.findByUsernameIgnoreCase(username);
    }


    public void deleteAccount(Long userId, String rawPassword){
        User user = userRepository.findById(userId).orElseThrow();

        //set this up once i have password hashing set up.

    }
    public boolean existsByUsernameIgnoreCase(String username){
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
