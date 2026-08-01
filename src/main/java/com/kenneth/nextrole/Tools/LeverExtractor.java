package com.kenneth.nextrole.Tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kenneth.nextrole.exception.JobParseException;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class LeverExtractor implements JobExtractor {

    private static final String LEVER_HOST = "jobs.lever.co";
    private static final String LEVER_API_BASE = "https://api.lever.co/v0/postings/";

    // matches /{site}/{postingId} with an optional trailing /apply
    private static final Pattern LEVER_JOB_PATH =
            Pattern.compile("^/([^/]+)/([^/]+?)(?:/apply)?/?$");

    private final RestClient restClient;

    public LeverExtractor(RestClient.Builder builder) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = builder
                .requestFactory(requestFactory)
                .build();
    }

    private record LeverUrlParts(String site, String postingId) {}

    private LeverUrlParts buildApiSetup(String url) {
        URI uri = URI.create(url);

        if (LEVER_HOST.equals(uri.getHost())) {
            Matcher matcher = LEVER_JOB_PATH.matcher(uri.getPath());
            if (matcher.matches()) {
                return new LeverUrlParts(matcher.group(1), matcher.group(2));
            }
        }

        throw new JobParseException("Invalid Lever URL: " + url);
    }

    @Override
    public String extract(String url) {
        LeverUrlParts parts = buildApiSetup(url);
        String apiUrl = LEVER_API_BASE + parts.site() + "/" + parts.postingId() + "?mode=json";

        try {
            return restClient.get().uri(apiUrl).retrieve().body(String.class);
        } catch (RestClientException e) {
            throw new JobParseException("Error retrieving job info from " + apiUrl);
        }
    }
}