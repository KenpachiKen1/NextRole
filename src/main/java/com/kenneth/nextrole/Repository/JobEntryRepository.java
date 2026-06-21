package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.JobStatus;
import com.kenneth.nextrole.Model.JobEntry;

import java.util.List;
import java.util.Optional;

import com.kenneth.nextrole.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobEntryRepository extends JpaRepository<JobEntry, Long> {

    List<JobEntry> findByUserId(Long userId); //list the applications for a given user.

    void deleteByUserIdAndJobPostingId(Long userId, Long jobPostingId);

    //if a user wants to find the jobs they applied to containing their input
    List<JobEntry> findByJobPosting_TitleContainingIgnoreCaseAndUser_Id(String title, Long userId);

    List<JobEntry> findByUser_IdAndStatus(Long userId, JobStatus status); //for filtering purposes

    List<JobEntry> findByUser_IdAndJobPosting_TitleContainingIgnoreCaseAndStatus(Long userId, String title, JobStatus status);

}
