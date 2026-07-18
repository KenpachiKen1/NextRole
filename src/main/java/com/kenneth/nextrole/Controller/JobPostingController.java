package com.kenneth.nextrole.Controller;

import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Service.JobPostingService;
import com.kenneth.nextrole.dto.jobposting.CreateJobPostingRequest;
import com.kenneth.nextrole.dto.jobposting.JobPostingResponse;
import com.kenneth.nextrole.dto.jobposting.UpdateJobPostingRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/job-postings")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    /**
     * Get all job postings
     * GET /api/job-postings
     */
    @GetMapping("/allJobs")
    public ResponseEntity <List<JobPostingResponse>> getAllJobPostings() {
        List <JobPostingResponse> job = jobPostingService.getAllJobPostings();
        return ResponseEntity.status(HttpStatus.OK).body(job);

    }

    /**
     * Get one job posting by id
     * GET /api/job-postings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobPostingResponse> getJobPostingById(@PathVariable Long id) {
        JobPostingResponse job = jobPostingService.getJobPostingById(id);
        return ResponseEntity.status(HttpStatus.OK).body(job);
    }

    /**
     * Get all postings for a company
     * GET /api/job-postings/company/{companyId}
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity <List<JobPostingResponse>>getPostingsByCompany(@PathVariable Long companyId) {

        List <JobPostingResponse> job = jobPostingService.getByCompany(companyId);
        return ResponseEntity.status(HttpStatus.OK).body(job);
    }

    /**
     * Search postings by title
     * GET /api/job-postings/search?title=engineer
     */
    @GetMapping("/search")
    public ResponseEntity <List<JobPostingResponse>> searchByTitle(@RequestParam String title) {

        List <JobPostingResponse> job = jobPostingService.searchByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(job);
    }

//    /**
//     * Create a job posting
//     * For now, this is intended for agent/admin/internal use.
//     *
//     * POST /api/job-postings
//     */
   @PostMapping("/create-job-posting")
   @ResponseStatus(HttpStatus.CREATED)
   public JobPostingResponse createJobPosting(
            @Valid @RequestBody CreateJobPostingRequest request) throws IOException {
       return jobPostingService.createJobPosting(request);

    }

    /**
     * Update a job posting
     * PUT /api/job-postings/{postingId}
     */
    @PutMapping("/{postingId}")
    public ResponseEntity <JobPostingResponse> updateJobPosting(
            @PathVariable Long postingId,
            @Valid @RequestBody UpdateJobPostingRequest request
    ) {

        JobPostingResponse job = jobPostingService.updateJobPosting(postingId, request);
        return ResponseEntity.status(HttpStatus.OK).body(job);

    }
}