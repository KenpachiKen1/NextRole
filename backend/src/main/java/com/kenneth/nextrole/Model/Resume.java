package com.kenneth.nextrole.Model;



import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String resumeTitle;

    private LocalDateTime uploadedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String s3ObjectKey;

    private Long fileSize;
    @Override
    public String toString(){
        return String.format("Resume ID: %d, Name: %s, resume Url: %d", id, resumeTitle, fileSize);

    }

    @ManyToMany
    @JoinTable(
            name = "resume_keywords",
            joinColumns = @JoinColumn(name = "resume_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )

    @Builder.Default
    private Set<Keyword> resumeKeywords = new HashSet<>();


}
