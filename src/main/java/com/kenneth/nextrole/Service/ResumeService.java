package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.ResumeRepository;
import com.kenneth.nextrole.dto.resume.CreateResumeRequest;
import com.kenneth.nextrole.dto.resume.ResumeResponse;
import com.kenneth.nextrole.dto.resume.UpdateResumeRequest;
import com.kenneth.nextrole.exception.ResumeNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
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
                .orElseThrow(() -> new ResumeNotFoundException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not authorized");
        }

        return toResponse(resume);
    }

    @Transactional
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

    @Transactional
    public ResumeResponse updateResume(UpdateResumeRequest request, User user, Long resumeId){
        Resume resume = resumeRepository.findById(resumeId).
                orElseThrow(() -> new ResumeNotFoundException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("This resume does not belong to this you");
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
    may rewrite this idk
     */

    @Transactional
    public List<ResumeResponse> deleteUserResume(Long id, User user) {
        resumeRepository.deleteByIdAndUserId(id, user.getId());

        return resumeRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public List<ResumeResponse> getCurrentUserResumes(User user) {
        Long userId = user.getId();
        return resumeRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public Optional<Resume> findResumeTitleAndUserId (String resumeTitle, Long userId){
        return resumeRepository.findByResumeTitleAndUserId(resumeTitle, userId);
    }

    public List<Resume> findByUserId(Long userId){
        return resumeRepository.findByUserId(userId); //returns the list of resume's
    }




}
