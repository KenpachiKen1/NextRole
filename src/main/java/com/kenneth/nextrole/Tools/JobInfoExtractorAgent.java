package com.kenneth.nextrole.Tools;


import com.kenneth.nextrole.Tools.dto.JobExtractionResponse;
import com.kenneth.nextrole.exception.JobParseException;
import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URI;


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
    private final AshbyExtractor ashbyExtractor;
    private final LeverExtractor leverExtractor;
    private final PlaywrightExtractor playwrightExtractor;
    private final GreenhouseExtractor greenhouseExtractor;

    public JobInfoExtractorAgent(BedrockRuntimeClient client, AshbyExtractor
                                         ashbyExtractor, LeverExtractor leverExtractor,
                                 GreenhouseExtractor greenhouseExtractor,
                                 PlaywrightExtractor playwrightExtractor){
        this.ashbyExtractor = ashbyExtractor;
        this.greenhouseExtractor = greenhouseExtractor;
        this.leverExtractor = leverExtractor;
        this.playwrightExtractor = playwrightExtractor;
        this.client = client;
    }



    private static final String system_prompt = """
                You are an expert Job Posting Extraction AI.
               \s
                Your job is to extract structured information from a job posting.
               \s
                You MUST return ONLY valid JSON.
               \s
                Do NOT include:
                - Markdown
                - Triple backticks
                - Explanations
                - Notes
                - Additional text before or after the JSON
               \s
               \s
               \s
                Use the following schema exactly:
               \s
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
            \s
                }
               \s
                Rules:
               \s
                - If a field cannot be determined, return null.
                - Arrays should be empty [] if no information is available.
                - Preserve wording where reasonable.
                - Do not invent information.
                - Extract only information that exists in the posting.
                - jobDescription should be a concise summary (2-4 paragraphs), not the entire posting.
                - requiredSkills should contain technologies, programming languages, frameworks, and tools.
                - Give only one salary amount not a range, also omit the currency symbol just the plain number.
                - employmentType should be values like:
                    - Full Time
                    - Part Time
                    - Contract
                    - Internship
                    - Temporary
          
                    RETURN ONLY JSON
            """

            ;


    public JobExtractionResponse generateJobDetails(String url) throws IOException {
        try {
            String jsonResponse = invokeClaude(url);
            String cleaned = stripMarkdownFences(jsonResponse);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cleaned, JobExtractionResponse.class);

        } catch (SdkClientException e) {
            System.err.printf("Error: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String stripMarkdownFences(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.replaceFirst("^```[a-zA-Z]*\\r?\\n?", "");
            if (trimmed.endsWith("```")) {
                trimmed = trimmed.substring(0, trimmed.length() - 3);
            }
        }
        return trimmed.trim();
    }


    private String invokeClaude (String url) throws IOException {
        var modelId = "us.anthropic.claude-sonnet-4-5-20250929-v1:0";
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
        URI uri = URI.create(URL);
        String host = uri.getHost();


        if (host == null){
            throw new JobParseException("A valid url is required");
        }

        return switch (host) {
            case "boards.greenhouse.io", "job-boards.greenhouse.io" -> this.greenhouseExtractor.extract(URL); //returns a string in json format
            case "jobs.lever.co" -> this.leverExtractor.extract(URL); //returns a string in json format
            case "jobs.ashbyhq.com" -> this.ashbyExtractor.extract(URL); //returns a string in json format
            default -> this.playwrightExtractor.extract(URL); //returns a plain string, main fallback.
        };

    }

    private String buildClaudeRequest(String postingText) {

        JSONObject body = buildRequestBody(postingText);

        return body.toString();
    }

    private JSONObject buildRequestBody(String postingText) {
        JSONObject body = new JSONObject();
        body.put("anthropic_version", "bedrock-2023-05-31");
        body.put("max_tokens", 2000);
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
