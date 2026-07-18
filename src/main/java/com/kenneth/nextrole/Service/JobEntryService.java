package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.JobStatus;
import com.kenneth.nextrole.Model.JobEntry;
import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.JobEntryRepository;
import com.kenneth.nextrole.Repository.JobPostingRepository;
import com.kenneth.nextrole.Repository.ResumeRepository;
import com.kenneth.nextrole.dto.jobentry.CreateJobEntryRequest;
import com.kenneth.nextrole.dto.jobentry.JobEntryResponse;
import com.kenneth.nextrole.dto.jobentry.UpdateJobEntryRequest;
import com.kenneth.nextrole.exception.JobEntryNotFoundException;
import com.kenneth.nextrole.exception.JobPostingNotFoundException;
import com.kenneth.nextrole.exception.ResumeNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobEntryService {
    private final JobEntryRepository jobEntryRepository;
    private final ResumeRepository resumeRepository;
    private final JobPostingRepository jobPostingRepository;

    public JobEntryService(JobEntryRepository jobEntryRepository, ResumeRepository resumeRepository, JobPostingRepository jobPostingRepository){
        this.jobEntryRepository = jobEntryRepository;
        this.resumeRepository = resumeRepository;
        this.jobPostingRepository = jobPostingRepository;
    }


    private JobEntryResponse toResponse(JobEntry je){
        return JobEntryResponse.builder().
                id(je.getId()).
                jobPostingId(je.getJobPosting().getId()).
                resumeId( je.getResumeUsed() != null
                        ? je.getResumeUsed().getId()
                        : null). //in case they didn't upload a resume
                appliedAt(je.getAppliedAt()).jobTitle(je.getJobPosting().getTitle()).
                status(je.getStatus()).notes(je.getNotes()).
                resumeTitle(
                        je.getResumeUsed() != null
                                ? je.getResumeUsed().getResumeTitle()
                                : null
                ).build();
    }

    @Transactional
    public JobEntryResponse createEntry(CreateJobEntryRequest request, User user, Long ResumeId, Long jobPostingID){
        JobStatus status = request.getStatus() != null ? request.getStatus() : JobStatus.SUBMITTED;

        Resume resume = resumeRepository.findById(ResumeId).
                orElseThrow(() -> new ResumeNotFoundException("Resume not be found"));

        JobPosting jobPosting = jobPostingRepository.findById(jobPostingID).
                orElseThrow((() -> new JobPostingNotFoundException("Job not found")));

        JobEntry entry = JobEntry.builder().user(user).
                jobPosting(jobPosting).
                resumeUsed(resume).
                notes(request.getNotes()).
                status(status).
                appliedAt(LocalDateTime.now()). //might change this so that the user can input a time since I will have automated functions based on this.
                        build();
        entry = jobEntryRepository.save(entry);
        return toResponse(entry);

    }

    @Transactional
    public JobEntryResponse updateEntry(Long entryId, UpdateJobEntryRequest request, User user){
        JobEntry entry = jobEntryRepository.findById(entryId)
                .orElseThrow(() -> new JobEntryNotFoundException("Entry not found"));

        if (!entry.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("Not authorized");
        }
        if (request.getNotes() != null){
            entry.setNotes(request.getNotes());
        }
        if (request.getResumeId() != null){

            Resume resume = resumeRepository.findById(request.getResumeId()).
                    orElseThrow(() -> new ResumeNotFoundException("Resume not found"));
            if (!resume.getUser().getId().equals(user.getId())) { //ownership check for the resume
                throw new AccessDeniedException("Not authorized");
            }
            entry.setResumeUsed(resume);
        }

        if (request.getStatus() != null){
            entry.setStatus(request.getStatus());
        }

        entry =  jobEntryRepository.save(entry);
        return toResponse(entry);

    }

    /*
    Update these to stream the toResponse
     */

    //find the jobEntriesByUserID
    public List<JobEntryResponse> findByUserId(Long userId){
        return jobEntryRepository.findByUser_Id(userId).stream().
                map(this::toResponse).
                toList();
    }

    public JobEntryResponse getJobEntry (Long entryId, Long userId){
        JobEntry j = jobEntryRepository.findByIdAndUser_Id(entryId, userId).
                orElseThrow(() -> new JobEntryNotFoundException("Job entry not found"));

        return toResponse(j);
    }

    //Deletes the entry, returns an updated list
    public List <JobEntryResponse> deleteByUserIdAndJobPostingId(Long userId, Long jobPostingId){
        jobEntryRepository.deleteByUser_IdAndJobPosting_Id(userId, jobPostingId);

        return jobEntryRepository.findByUser_Id(userId).
                stream().
                map(this::toResponse).
                toList();
    }

    public List<JobEntryResponse> findByJobPosting_TitleContainingIgnoreCaseAndUser_Id(String title, Long uid){
        return jobEntryRepository.findByJobPosting_TitleContainingIgnoreCaseAndUser_Id(title, uid).
                stream().
                map(this::toResponse).toList();
    }
    public List<JobEntryResponse>findByUser_IdAndStatus(Long userId, JobStatus status){
        return jobEntryRepository.findByUser_IdAndStatus(userId, status).
                stream().
                map(this::toResponse)
                .toList();
    }

    public List<JobEntryResponse> findByUser_IdAndJobPosting_TitleContainingIgnoreCaseAndStatus(Long userId, String title, JobStatus status){
        return jobEntryRepository.
                findByUser_IdAndJobPosting_TitleContainingIgnoreCaseAndStatus(userId, title, status).stream()
                .map(this::toResponse).toList();
    }

}
