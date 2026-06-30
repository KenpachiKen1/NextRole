package com.kenneth.nextrole.Controller;


import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Service.ResumeService;
import com.kenneth.nextrole.dto.resume.CreateResumeRequest;
import com.kenneth.nextrole.dto.resume.ResumeResponse;
import com.kenneth.nextrole.dto.resume.UpdateResumeRequest;
import com.kenneth.nextrole.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController (ResumeService resumeService){
        this.resumeService = resumeService;
    }

    @PostMapping("/createResume")
    public ResponseEntity<ResumeResponse> createResume(@Valid @RequestBody CreateResumeRequest request, @AuthenticationPrincipal CustomUserPrincipal customUserPrincipal){
        User user = customUserPrincipal.getUser();
        ResumeResponse response = resumeService.createResume(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/showcaseResume/{id}/")
    public ResponseEntity<ResumeResponse> showResume(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal customUserPrincipal){
        User user = customUserPrincipal.getUser();
        ResumeResponse response = resumeService.getResume(id, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/updateResume/{id}/")
    public ResponseEntity <ResumeResponse> updateResume(@PathVariable Long id, UpdateResumeRequest request,
                                                        @AuthenticationPrincipal CustomUserPrincipal customUserPrincipal){
        User user = customUserPrincipal.getUser();
        ResumeResponse response = resumeService.updateResume(request, user, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    //returning the list of resume's a user has uploaded
    @GetMapping("/resume-list")
    public ResponseEntity <List<ResumeResponse>> getUserResumeList(@AuthenticationPrincipal CustomUserPrincipal customUserPrincipal){
        User user = customUserPrincipal.getUser();
        List< ResumeResponse > response = resumeService.getCurrentUserResumes(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @DeleteMapping("/deleteResume/{id}/")
    public ResponseEntity <List <ResumeResponse>> deleteUserResume(@PathVariable Long id,
                                                               @AuthenticationPrincipal CustomUserPrincipal customUserPrincipal){
        User user = customUserPrincipal.getUser();

        List< ResumeResponse > response =  resumeService.deleteUserResume(id,user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
