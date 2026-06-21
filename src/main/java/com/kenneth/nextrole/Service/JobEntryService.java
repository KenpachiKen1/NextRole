package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.JobStatus;
import com.kenneth.nextrole.Model.JobEntry;
import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.JobEntryRepository;
import com.kenneth.nextrole.Repository.ResumeRepository;
import com.kenneth.nextrole.dto.jobentry.CreateJobEntryRequest;
import com.kenneth.nextrole.dto.jobentry.JobEntryResponse;
import com.kenneth.nextrole.dto.jobentry.UpdateJobEntryRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobEntryService {
    private final JobEntryRepository jobEntryRepository;
    private final ResumeRepository resumeRepository;

    public JobEntryService(JobEntryRepository jobEntryRepository, ResumeRepository resumeRepository){
        this.jobEntryRepository = jobEntryRepository;
        this.resumeRepository = resumeRepository;
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
                resumeTitle(je.getResumeUsed().getResumeTitle()).build();
    }


    public JobEntryResponse createEntry(CreateJobEntryRequest request, User user, JobPosting jobPosting, Resume resume){
        JobStatus status = request.getStatus() != null ? request.getStatus() : JobStatus.SUBMITTED;

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

    public JobEntryResponse updateEntry(Long entryId, UpdateJobEntryRequest request, User user){
        JobEntry entry = jobEntryRepository.findById(entryId).orElseThrow(() -> new RuntimeException("Entry not found"));
        if (!entry.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Not authorized");
        }
        if (request.getNotes() != null){
            entry.setNotes(request.getNotes());
        }
        if (request.getResumeId() != null){

            Resume resume = resumeRepository.findById(request.getResumeId()).
                    orElseThrow(() -> new RuntimeException("Resume not found"));
            if (!resume.getUser().getId().equals(user.getId())) { //ownership check for the resume
                throw new RuntimeException("Not authorized");
            }
            entry.setResumeUsed(resume);
        }

        if (request.getStatus() != null){
            entry.setStatus(request.getStatus());
        }

        entry =  jobEntryRepository.save(entry);
        return toResponse(entry);

    }

    public List<JobEntry> findByUserId(Long userId){
        return jobEntryRepository.findByUserId(userId);
    }

    public void deleteByUserIdAndJobPostingId(Long userId, Long jobPostingId){
        jobEntryRepository.deleteByUserIdAndJobPostingId(userId, jobPostingId);
    }

    public List<JobEntry> findByJobPosting_TitleContainingIgnoreCaseAndUser_Id(String title, Long uid){
        return jobEntryRepository.findByJobPosting_TitleContainingIgnoreCaseAndUser_Id(title, uid);
    }

    public List<JobEntry>findByUser_IdAndStatus(Long userId, JobStatus status){
        return jobEntryRepository.findByUser_IdAndStatus(userId, status);
    }

    public List<JobEntry> findByUser_IdAndJobPosting_TitleContainingIgnoreCaseAndStatus(Long userId, String title, JobStatus status){
        return jobEntryRepository.findByUser_IdAndJobPosting_TitleContainingIgnoreCaseAndStatus(userId, title, status);
    }

}
