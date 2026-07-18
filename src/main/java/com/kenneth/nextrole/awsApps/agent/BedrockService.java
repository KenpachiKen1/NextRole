package com.kenneth.nextrole.awsApps.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.awsApps.dto.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;

import java.io.IOException;


@Service
public class BedrockService {


    private final Region region = Region.US_EAST_1;
    private final BedrockRuntimeClient client =
            BedrockRuntimeClient.builder()
                    .credentialsProvider(DefaultCredentialsProvider.builder().build())
                    .region(region)
                    .build();
    private final PromptBuilder promptBuilder;
    private String SYSTEM_PROMPT;
    private final ObjectMapper mapper;
    public BedrockService(PromptBuilder p, ObjectMapper mapper ){
        this.promptBuilder = p;
        this.mapper = mapper;
    }






    private String invokeClaude(String systemPrompt, String userPrompt){

        var modelId = "anthropic.claude-3-haiku-20240307-v1:0";
        String nativeRequest = buildClaudeRequest(userPrompt, systemPrompt);
        try {
            var response = client.invokeModel(
                    request -> request
                            .modelId(modelId)
                            .body(SdkBytes.fromUtf8String(nativeRequest))
            );
            var responseBody = new JSONObject(response.body().asUtf8String());
            return new JSONPointer("/content/0/text").queryFrom(responseBody).toString();

        } catch (SdkClientException e) {
            System.err.printf("ERROR: Can't invoke '%s'. Reason: %s", modelId, e.getMessage());
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

    public ResumeTailoringResponse tailorResume(ParsedResume resume, JobPosting jp) throws JsonProcessingException {
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


    public InterviewPrepResponse interviewPrepResponse(ParsedResume resume, JobPosting jp) throws JsonProcessingException {
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
        body.put("max_tokens", 600);
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
