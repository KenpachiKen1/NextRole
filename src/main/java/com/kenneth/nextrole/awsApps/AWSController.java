package com.kenneth.nextrole.awsApps;


import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.awsApps.agent.BedrockService;
import com.kenneth.nextrole.awsApps.agent.ResumeParserService;
import com.kenneth.nextrole.awsApps.dto.InterviewPrepResponse;
import com.kenneth.nextrole.awsApps.dto.ParsedResume;
import com.kenneth.nextrole.awsApps.dto.ResumeFeedbackResponse;
import com.kenneth.nextrole.awsApps.dto.ResumeTailoringResponse;
import com.kenneth.nextrole.security.CustomUserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/aws-tools")
public class AWSController {


    /*
     bedrock METHODS HERE
     */

    private final ResumeParserService parser;
    private final BedrockService service;

    public AWSController(ResumeParserService parser, BedrockService service){
        this.parser = parser;
        this.service = service;
    }

    @PostMapping("/resumeFeedback-agent")
    public ResponseEntity<ResumeFeedbackResponse> invokeResumeFeedbackAgent(@AuthenticationPrincipal  CustomUserPrincipal principal, String objectKey) throws IOException {

        User user = principal.getUser();


        ParsedResume resume = parser.parseResumePDF(user, objectKey);
        ResumeFeedbackResponse response = service.reviewResume(resume);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    //SHOULD BE PASSING IN THE JOB POSTING ID NOT AN ENTIRE JOB POSTING

    @PostMapping("/interviewPrep-agent")
    public ResponseEntity<InterviewPrepResponse> invokeInterviewPrepAgent(@AuthenticationPrincipal  CustomUserPrincipal principal, String objectKey, JobPosting jp) throws IOException {

        User user = principal.getUser();


        ParsedResume resume = parser.parseResumePDF(user, objectKey);
        InterviewPrepResponse response = service.interviewPrepResponse(resume, jp);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/resumeTailoring-agent")
    public ResponseEntity<ResumeTailoringResponse> invokeTailoringAgent(@AuthenticationPrincipal  CustomUserPrincipal principal, String objectKey, JobPosting jp) throws IOException {

        User user = principal.getUser();


        ParsedResume resume = parser.parseResumePDF(user, objectKey);
        ResumeTailoringResponse response = service.tailorResume(resume, jp);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }







}
