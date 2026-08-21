package com.kenneth.nextrole.Tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kenneth.nextrole.exception.JobParseException;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class GreenhouseExtractor implements JobExtractor {

    private static final Set<String> GREENHOUSE_HOSTS =
            Set.of("boards.greenhouse.io", "job-boards.greenhouse.io");

    private static final Pattern GREENHOUSE_JOB_PATH =
            Pattern.compile("^/([^/]+)/jobs/(\\d+)/?$");

    private final RestClient restClient;

    public GreenhouseExtractor(RestClient.Builder builder) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = builder
                .requestFactory(requestFactory)
                .build();
    }

    @Override
    public String extract(String url) {
        String apiUrl = convertToApi(url);

        try {
            return restClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            throw new JobParseException("Error retrieving job info from " + apiUrl);
        }
    }

    private String convertToApi(String url) {
        URI uri = URI.create(url);
        String host = uri.getHost();

        if (GREENHOUSE_HOSTS.contains(host)) {
            Matcher matcher = GREENHOUSE_JOB_PATH.matcher(uri.getPath());
            if (matcher.matches()) {
                return String.format(
                        "https://boards-api.greenhouse.io/v1/boards/%s/jobs/%s",
                        matcher.group(1), matcher.group(2));
            }
        }

        throw new JobParseException("Invalid Greenhouse URL: " + url);
    }
}