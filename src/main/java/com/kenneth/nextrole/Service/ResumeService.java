package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.ResumeRepository;
import com.kenneth.nextrole.awsApps.S3Service;
import com.kenneth.nextrole.dto.resume.CreateResumeRequest;
import com.kenneth.nextrole.dto.resume.ResumeResponse;
import com.kenneth.nextrole.dto.resume.UpdateResumeRequest;
import com.kenneth.nextrole.dto.resume.ViewSingleResumeResponse;
import com.kenneth.nextrole.exception.ResumeNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final S3Service service;


    public ResumeService (ResumeRepository resumeRepository, S3Service service){
        this.resumeRepository = resumeRepository;
        this.service = service;
    }

    private ResumeResponse toResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .resumeTitle(resume.getResumeTitle())
                .fileSize(resume.getFileSize())
                .uploadedAt(resume.getUploadedAt())
                .build();
    }

    private ViewSingleResumeResponse singleResumeResponse(Resume resume, String url){
        return ViewSingleResumeResponse.builder()
                .id(resume.getId())
                .resumeTitle(resume.getResumeTitle())
                .fileSize(resume.getFileSize())
                .uploadedAt(resume.getUploadedAt())
                .url(url)
                .build();
    }

    public ViewSingleResumeResponse getResume(Long resumeId, User user) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResumeNotFoundException("Resume not found"));

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not authorized");
        }

        String url = service.getPresignedUrl(resume.getS3ObjectKey());

        return singleResumeResponse(resume, url);
    }

    /*
    Integrate the s3 functions here
     */
    @Transactional
    public ResumeResponse createResume (CreateResumeRequest request, User user){

        String objectKey = genObjectKey(user, request.getFile());

        try {
            service.uploadResume(request.getFile(), objectKey);
        } catch (Exception e){
            throw new RuntimeException("Could not upload file");
        }

        Resume resume = Resume.builder().user(user).
                fileSize(request.getFile().getSize()).
                resumeTitle(request.getResumeTitle()).
                uploadedAt(LocalDateTime.now()).s3ObjectKey(objectKey).
                build();

        resume = resumeRepository.save(resume);



        return toResponse(resume);

    }
    /*
    Changing update and create to resumeResponse so that the frontend receives the updated information immediately.
    Prevents having to make multiple api calls.
     */


    //building out the object key to send back to S3.
    private String genObjectKey(User user, MultipartFile file){
        String uuid = UUID.randomUUID().toString();
        String original_file = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(original_file);
        return "users/"+user.getId()+"resumes/"+uuid+"/"+extension;
    }
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


        if(request.getFileSize() != null){
            resume.setFileSize(request.getFileSize());
        }


       resume = resumeRepository.save(resume);
        return toResponse(resume);
    }
    /*
    may rewrite this idk
     */

    @Transactional
    public List<ResumeResponse> deleteUserResume(Long id, User user) {
        Resume resume = resumeRepository.findById(id).
                orElseThrow(() -> new ResumeNotFoundException("Resume not found"));

        if(resume.getUser() != user){
            throw new AccessDeniedException("This resume is not yours");
        }
        service.deleteResume(resume.getS3ObjectKey());

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
