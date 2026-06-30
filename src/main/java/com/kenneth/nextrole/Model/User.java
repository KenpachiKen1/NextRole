package com.kenneth.nextrole.Model;


import com.kenneth.nextrole.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter //generates getters and setters
@AllArgsConstructor //all args constructor with complete body
@NoArgsConstructor //no argument constructor
@Builder //builds out the user -> think Model.objects.create... from django
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private LocalDateTime createdAt;



    @Column(nullable = true)
    private String profilePhoto;

    @Column(nullable = false)

    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Customer customer; //each user will automatically have a customer account created


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) //mappedBy tells jpa that the resumes are own by the User
    private List<Resume> resumes = new ArrayList<>();

    @Override
    public String toString(){
        return String.format("User ID: %d, username: %s, email: %s, profile_photo: %s", id, username, email, profilePhoto);
    }
}
