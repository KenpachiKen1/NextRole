package com.kenneth.nextrole.Model;

import com.kenneth.nextrole.JobStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity //database table definition
@Getter @Setter //creates getters and setters for me
@NoArgsConstructor
@AllArgsConstructor
@Builder //would utilize this to build out each object in the table
@Table(name = "JobEntry")
public class JobEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "jobPosting_id")
    private JobPosting jobPosting;
    @ManyToOne
    private Resume resumeUsed;

    private String notes;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatus status = JobStatus.SUBMITTED;

    private LocalDateTime appliedAt;


}