package com.kenneth.nextrole.Tools;


import com.kenneth.nextrole.Tools.dto.JobExtractionResponse;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;


import org.json.JSONObject;
import org.json.JSONPointer;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import com.fasterxml.jackson.databind.ObjectMapper;
/*
The purpose of this class is to do two things.

1. Extract text provided by a url.

2. Pass it to a bedrock agent to then invoke it.
 */

@Service
public class JobInfoExtractorAgent {
    private final BedrockRuntimeClient client;

    public JobInfoExtractorAgent(BedrockRuntimeClient client){
        this.client = client;
    }



    private static final String system_prompt = """
                You are an expert Job Posting Extraction AI.
                
                Your job is to extract structured information from a job posting.
                
                You MUST return ONLY valid JSON.
                
                Do NOT include:
                - Markdown
                - Triple backticks
                - Explanations
                - Notes
                - Additional text before or after the JSON
                
                Use the following schema exactly:
                
                {
                  "companyName": null,
                  "jobTitle": null,
                  "location": null,
                  "employmentType": null,
                  "salary": null,
                  "companyWebsite": null
                  "requisitionCode": null,
                  "requiredSkills": [],
                  "preferredSkills": [],
                  "jobDescription": null,
                  "companyPhoto" : null
             
                }
                
                Rules:
                
                - If a field cannot be determined, return null.
                - Arrays should be empty [] if no information is available.
                - Preserve wording where reasonable.
                - Do not invent information.
                - Extract only information that exists in the posting.
                - jobDescription should be a concise summary (2-4 paragraphs), not the entire posting.
                - requiredSkills should contain technologies, programming languages, frameworks, and tools.
                - experienceLevel should be values like:
                    - Internship
                    - Entry Level
                    - Associate
                    - Mid Level
                    - Senior
                    - Staff
                    - Principal
                    - Manager
                    - Director
                    - Executive
                - employmentType should be values like:
                    - Full Time
                    - Part Time
                    - Contract
                    - Internship
                    - Temporary""";


    public JobExtractionResponse generateJobDetails(String url) throws IOException {
        try{
            String jsonResponse = invokeClaude(url);
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(jsonResponse, JobExtractionResponse.class);

        }catch (SdkClientException e) {
            System.err.printf("Error: %s", e.getMessage());
            throw new RuntimeException(e);
        }

    }



    private String invokeClaude (String url) throws IOException {
        var modelId = "anthropic.claude-3-haiku-20240307-v1:0";
        String text = extractText(url);
        String nativeRequest = buildClaudeRequest(text);

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


    private String extractText(String URL) throws IOException {
        try{
            //Creating the doc, accessing the URL with a userAgent to prevent being blocked by bot security. Timeout is for page load
            Document doc = Jsoup.connect(URL).
                    userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36").
                    timeout(10000).get();

            String title = doc.title();

            doc.body();
            String pageText =
                    doc.body().text();

            return """
            Job Posting Title:
            %s
            
            Job Posting:
            %s
            """.formatted(title, pageText);
        } catch (IOException e){
            throw new IOException("Error parsing website", e);
        }
    }




    private String buildClaudeRequest(String postingText) {

        JSONObject body = buildRequestBody(postingText);


        return body.toString();
    }

    private JSONObject buildRequestBody(String postingText) {
        JSONObject body = new JSONObject();
        body.put("anthropic_version", "bedrock-2023-05-31");
        body.put("max_tokens", 600);
        body.put("temperature", 0.1);

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put(
                "content",
                """
                Extract the structured job posting information from the following job posting.
            
                %s
                """.formatted(postingText)
        );
        JSONArray messages = new JSONArray();
        messages.put(userMessage);

        body.put("system", system_prompt);
        body.put("messages", messages);
        return body;
    }



}
