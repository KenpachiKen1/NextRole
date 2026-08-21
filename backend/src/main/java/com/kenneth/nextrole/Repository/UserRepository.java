package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //some queries I want

    /*
     Exists by username
     Exists by email

     */

    //Optional is for when I am looking for data i actually intend to use, forces to handle exceptions explicitly
    Optional<User> findByEmail(String email); //typically will use this for updating accounts, checking data
    Optional<User> findByUsernameIgnoreCase(String username);

    //the lazy check linear version. Signup validation
    boolean existsByUsernameIgnoreCase(String username);  //checks if a user exists with the given username
    boolean existsByEmail(String email); //checks if a user with te given email exists


}
