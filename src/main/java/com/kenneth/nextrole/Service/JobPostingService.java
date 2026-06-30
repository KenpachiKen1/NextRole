package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.Company;
import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Repository.JobPostingRepository;
import com.kenneth.nextrole.dto.jobposting.CreateJobPostingRequest;
import com.kenneth.nextrole.dto.jobposting.JobPostingResponse;
import com.kenneth.nextrole.dto.jobposting.UpdateJobPostingRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;

    }

    private JobPostingResponse toResponse(JobPosting jp) {
        return JobPostingResponse.builder()
                .id(jp.getId())
                .title(jp.getTitle())
                .location(jp.getLocation())
                .salary(jp.getSalary())
                .postingUrl(jp.getPostingUrl())
                .companyId(jp.getCompany().getId())
                .companyName(jp.getCompany().getName())
                .build();
    }


    //admin (me) or agent work
    /*
        Future updates would probably be that my agent would rescrape the job posting if it's still available.
     */
    @Transactional
    public JobPostingResponse createJobPosting(CreateJobPostingRequest request, Company company){
        JobPosting jp = JobPosting.builder().
                title(request.getTitle()).
                salary(request.getSalary()).
                company(company).location(request.getLocation()).postingUrl(request.getPostingUrl()).
                requisitionCode(request.getRequisitionCode())
                .build();
        jp = jobPostingRepository.save(jp);
        return toResponse(jp);
    }

    @Transactional
    public JobPostingResponse updateJobPosting(Long postingId, UpdateJobPostingRequest request){
        JobPosting jp = jobPostingRepository.findById(postingId)
                .orElseThrow(() -> new EntityNotFoundException("Job Posting not found"));

        if(request.getSalary() != null){
            jp.setSalary(request.getSalary());
        }

        if (request.getRequisitionCode() != null){
            jp.setRequisitionCode(request.getRequisitionCode());
        }

        if(request.getLocation() != null){
            jp.setLocation(request.getLocation());
        }

        if (request.getPostingUrl() != null){
            jp.setPostingUrl(request.getPostingUrl());
        }

        jp = jobPostingRepository.save(jp);

        return toResponse(jp);
    }

    public JobPostingResponse getJobPostingById(Long id){
        JobPosting jp = jobPostingRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Posting not found"));
        return toResponse(jp);
    }

    public List<JobPostingResponse> getAllJobPostings() {
        return jobPostingRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<JobPostingResponse> getByCompany(Long companyId) {
        return jobPostingRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<JobPostingResponse> searchByTitle(String title) {
        return jobPostingRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toResponse)
                .toList();
    }


}