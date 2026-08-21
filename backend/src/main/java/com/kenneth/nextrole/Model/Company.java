package com.kenneth.nextrole.Model;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity //database table definition
@Getter @Setter //creates getters and setters for me
@NoArgsConstructor
@AllArgsConstructor
@Builder //would utilize this to build out each object in the table
@Table(name = "Company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    private String companyWebsite;


    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @Builder.Default
    private List<JobPosting> jobPostings = new ArrayList<>();

    @Override
    public String toString(){
        return String.format("Company ID: %d, Name: %s, Website: %s", id, name, companyWebsite);
    }


}
