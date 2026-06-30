package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.JobStatus;
import com.kenneth.nextrole.Model.JobEntry;

import java.util.List;
import java.util.Optional;

import com.kenneth.nextrole.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobEntryRepository extends JpaRepository<JobEntry, Long> {

    Optional<JobEntry> findByIdAndUser_Id(Long entryId, Long userId);

    List<JobEntry> findByUser_Id(Long userId);
    void deleteByUser_IdAndJobPosting_Id(Long userId, Long jobPostingId);
    //if a user wants to find the jobs they applied to containing their input
    List<JobEntry> findByJobPosting_TitleContainingIgnoreCaseAndUser_Id(String title, Long userId);

    List<JobEntry> findByUser_IdAndStatus(Long userId, JobStatus status); //for filtering purposes

    List<JobEntry> findByUser_IdAndJobPosting_TitleContainingIgnoreCaseAndStatus(Long userId, String title, JobStatus status);

}
