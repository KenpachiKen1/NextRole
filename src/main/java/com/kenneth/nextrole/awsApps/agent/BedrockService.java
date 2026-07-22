package com.kenneth.nextrole.awsApps.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Repository.JobPostingRepository;
import com.kenneth.nextrole.awsApps.dto.*;
import com.kenneth.nextrole.exception.JobPostingNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;



@Service
public class BedrockService {

    private final BedrockRuntimeClient client;
    private final PromptBuilder promptBuilder;
    private final ObjectMapper mapper;
    private final JobPostingRepository repository;
    private static final String SONNET_MODEL =
            "us.anthropic.claude-sonnet-4-5-20250929-v1:0";
    public BedrockService(BedrockRuntimeClient client, PromptBuilder p, ObjectMapper mapper, JobPostingRepository repository){
        this.client = client;
        this.promptBuilder = p;
        this.mapper = mapper;
        this.repository = repository;
    }
    private String invokeClaude(String systemPrompt, String userPrompt){
        String nativeRequest = buildClaudeRequest(userPrompt, systemPrompt);
        try {
            var response = client.invokeModel(
                    request -> request
                            .modelId(SONNET_MODEL)
                            .body(SdkBytes.fromUtf8String(nativeRequest))
            );
            var responseBody = new JSONObject(response.body().asUtf8String());

            String stopReason = responseBody.optString("stop_reason", "");
            if ("max_tokens".equals(stopReason)) {
                throw new RuntimeException(
                        "Claude response was truncated (hit max_tokens). Increase max_tokens or shorten the prompt."
                );
            }

            String text = new JSONPointer("/content/0/text").queryFrom(responseBody).toString();
            return stripMarkdownFence(text);

        } catch (SdkClientException e) {
            System.err.printf("ERROR: Can't invoke '%s'. Reason: %s", SONNET_MODEL, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String buildClaudeRequest(String userPrompt, String systemPrompt) {

        JSONObject body = buildRequestBody(userPrompt, systemPrompt);


        return body.toString();
    }


    public ResumeFeedbackResponse reviewResume(ParsedResume resume) throws JsonProcessingException{

        String systemPrompt =
                promptBuilder.getResumeFeedbackPrompt();

        String userPrompt =
                """
                Candidate Resume
                ----------------
                
                %s
                """.formatted(resume.getText());

        String json =
                invokeClaude(systemPrompt, userPrompt);



        return mapper.readValue(json, ResumeFeedbackResponse.class);

    }

    private String stripMarkdownFence(String json){
        json = json.trim();
        if (json.startsWith("```")) {
            json = json.replaceFirst("^```(?:json)?", "");
            json = json.replaceFirst("```$", "");
            json = json.trim();
        }
        return json;
    }

    public ResumeTailoringResponse tailorResume(ParsedResume resume, Long jobPostingId) throws JsonProcessingException {

        JobPosting jp = this.repository.findById(jobPostingId).orElseThrow(() -> new JobPostingNotFoundException("Job Posting not found"));
        String systemPrompt =
                promptBuilder.getResumeTailoringPrompt();

        String userPrompt =
                """
                Candidate Resume
                ----------------
                %s
        
                Job Posting
                ----------------
                Title:
                %s
        
                Description:
                %s
                """
                        .formatted(
                                resume.getText(),
                                jp.getTitle(),
                                jp.getDescription()
                        );

        String json =
                invokeClaude(systemPrompt, userPrompt);



        return mapper.readValue(json, ResumeTailoringResponse.class);


    }


    public InterviewPrepResponse interviewPrepResponse(ParsedResume resume, Long jobPostingId) throws JsonProcessingException {

        JobPosting jp = this.repository.findById(jobPostingId).orElseThrow(() -> new JobPostingNotFoundException("Job Posting not found"));
        String systemPrompt =
                promptBuilder.getInterviewPrompt();

        String userPrompt =
                """
                Candidate Resume
                ----------------
                %s
        
                Job Posting
                ----------------
                Title:
                %s
        
                Description:
                %s
                """
                        .formatted(
                                resume.getText(),
                                jp.getTitle(),
                                jp.getDescription()
                        );

        String json =
                invokeClaude(systemPrompt, userPrompt);




        return mapper.readValue(json, InterviewPrepResponse.class);


    }




    private JSONObject buildRequestBody(String userPrompt, String systemPrompt){

        JSONObject body = new JSONObject();

        body.put("anthropic_version", "bedrock-2023-05-31");
        body.put("max_tokens", 2000);
        body.put("temperature", 0.1);

        body.put("system", systemPrompt);

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);

        JSONArray messages = new JSONArray();
        messages.put(userMessage);

        body.put("messages", messages);
        return body;
    }
}