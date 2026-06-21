package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.ResumeRepository;
import com.kenneth.nextrole.dto.resume.CreateResumeRequest;
import com.kenneth.nextrole.dto.resume.ResumeResponse;
import com.kenneth.nextrole.dto.resume.UpdateResumeRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService (ResumeRepository resumeRepository){
        this.resumeRepository = resumeRepository;
    }


    private ResumeResponse toResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .resumeTitle(resume.getResumeTitle())
                .resumeUrl(resume.getResumeUrl())
                .uploadedAt(resume.getUploadedAt())
                .build();
    }

    public ResumeResponse getResume(Long resumeId, User user) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized");
        }

        return toResponse(resume);
    }

    public ResumeResponse createResume (CreateResumeRequest request, User user){
        Resume resume = Resume.builder().user(user).
                resumeUrl(request.getResumeUrl()).
                resumeTitle(request.getResumeTitle()).
                uploadedAt(LocalDateTime.now()).build();

        resume = resumeRepository.save(resume);
        return toResponse(resume);

    }
    /*
    Changing update and create to resumeResponse so that the frontend receives the updated information immediately.
    Prevents having to make multiple api calls.
     */
    public ResumeResponse updateResume(UpdateResumeRequest request, User user, Long resumeId){
        Resume resume = resumeRepository.findById(resumeId).
                orElseThrow(() -> new RuntimeException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())){
            throw new RuntimeException("This resume does not belong to this user.");
        }

        if(request.getResumeTitle() != null){
                resume.setResumeTitle(request.getResumeTitle());;
            }

        if(request.getResumeUrl() != null){
                resume.setResumeUrl(request.getResumeUrl());
            }


       resume = resumeRepository.save(resume);
        return toResponse(resume);
    }

    /*
    Adjust this by creating my own exception here but for now leave as is. Create your exception classes before creating your controllers
     */
    public void deleteUserResume(Long resumeId, Long userId){
        Resume resume = resumeRepository.
                findByIdAndUserId(resumeId, userId) //get the requested resume
                .orElseThrow(); //if nothing returns then throw an error (resume doesn't exist for that specific user)

        resumeRepository.delete(resume); //finally delete the requested resume since it exists
    }
    public Optional<Resume> findResumeTitleAndUserId (String resumeTitle, Long userId){
        return resumeRepository.findByResumeTitleAndUserId(resumeTitle, userId);
    }

    public List<Resume> findByUserId(Long userId){
        return resumeRepository.findByUserId(userId); //returns the list of resume's
    }




}
