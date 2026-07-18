package com.kenneth.nextrole.Model;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity //database table definition
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder //would utilize this to build out each object in the table
@Table(name = "JobPosting")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Double salary;
    private String location;
    private String postingUrl;
    private String requisitionCode;
    private String description;


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToMany
    @JoinTable(
            name = "job_required_keywords",
            joinColumns = @JoinColumn(name = "job_posting_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    @Builder.Default
    private Set<Keyword> requiredKeywords = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "job_preferred_keywords",
            joinColumns = @JoinColumn(name = "job_posting_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )

    @Builder.Default
    private Set<Keyword> preferredKeywords = new HashSet<>();
    @Override
    public String toString(){
        String salaryText = salary != null ? String.format("$%.2f", salary) : "Not listed";
        return String.format("Role title: %s, Salary: %s, Location: %s, PostingUrl: %s", title, salaryText, location, postingUrl);
    }
}