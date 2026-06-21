package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.Model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    /*
    Queries needed.
     */

    List<JobPosting> findByCompanyId (Long Id); //List all job postings by the job, should include jobs that the user hasn't applied to either.
    List<JobPosting> findByTitleContainingIgnoreCase(String title);

}
