package com.kenneth.nextrole.Tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenneth.nextrole.exception.JobParseException;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class AshbyExtractor implements JobExtractor {

    private static final String ASHBY_HOST = "jobs.ashbyhq.com";
    private static final String ASHBY_API_BASE = "https://api.ashbyhq.com/posting-api/job-board/";

    private static final Pattern GET_COMPANY_NAME = Pattern.compile("^/([^/]+)/([^/]+)/?$");

    private final RestClient restClient;
    private final ObjectMapper mapper;

    public AshbyExtractor(RestClient.Builder builder, ObjectMapper mapper) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = builder
                .requestFactory(requestFactory)
                .build();

        this.mapper = mapper;
    }

    private record AshbyUrlParts(String company, String jobId) {}

    private AshbyUrlParts buildApiSetup(String url) {
        URI uri = URI.create(url);

        if (ASHBY_HOST.equals(uri.getHost())) {
            Matcher matcher = GET_COMPANY_NAME.matcher(uri.getPath());
            if (matcher.matches()) {
                return new AshbyUrlParts(matcher.group(1), matcher.group(2));
            }
        }

        throw new JobParseException("Invalid Ashby URL: " + url);
    }

    @Override
    public String extract(String url) {
        AshbyUrlParts parts = buildApiSetup(url);
        String apiUrl = ASHBY_API_BASE + parts.company() + "/";

        String jobsJson;
        try {
            jobsJson = restClient.get().uri(apiUrl).retrieve().body(String.class); //returns a map of jobs
        } catch (RestClientException e) {
            throw new JobParseException("Error retrieving job info from " + apiUrl);
        }

        try {
            JsonNode node = mapper.readTree(jobsJson);
            JsonNode jobs = node.get("jobs");
            if (jobs != null && jobs.isArray()) {
                for (JsonNode job : jobs) {
                    if (job.get("id").asText().equals(parts.jobId())) {
                        return job.toString();
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new JobParseException("Malformed response from Ashby: " + apiUrl);
        }

        throw new JobParseException(
                "Could not find job " + parts.jobId() + " on Ashby board " + parts.company());
    }
}