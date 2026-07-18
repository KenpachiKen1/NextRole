package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.Model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    /*
    Queries needed.
     */

    List<JobPosting> findByCompanyId (Long Id); //List all job postings by the job, should include jobs that the user hasn't applied to either.
    List<JobPosting> findByTitleContainingIgnoreCase(String title);


    @Query("SELECT DISTINCT r FROM JobPosting r JOIN r.preferredKeywords k WHERE k.keyword IN :keywords")
    List<JobPosting> findByPreferredKeywords(@Param("keywords") List<String> keywords);


    @Query("SELECT DISTINCT jp FROM JobPosting jp JOIN jp.requiredKeywords k WHERE k.keyword IN :keywords ")
    List<JobPosting> findByRequiredKeywords(@Param("keywords") List<String> keywords);

}
