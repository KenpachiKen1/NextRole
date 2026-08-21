package com.kenneth.nextrole.Model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword;

    @ManyToMany(mappedBy = "requiredKeywords")
    @Builder.Default
    private Set<JobPosting> requiredForJobs = new HashSet<>();

    @ManyToMany(mappedBy = "preferredKeywords")
    @Builder.Default
    private Set<JobPosting> preferredForJobs = new HashSet<>();

    @ManyToMany(mappedBy = "resumeKeywords")
    @Builder.Default
    private Set<Resume> resumes = new HashSet<>();
}