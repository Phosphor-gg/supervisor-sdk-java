package gg.supervisor.sdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gg.supervisor.sdk.exceptions.SupervisorException;
import gg.supervisor.sdk.models.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/** Client for the Supervisor content moderation API. */
public class SupervisorClient {
    private static final String DEFAULT_BASE_URL = "https://supervisor.gg";

    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private SupervisorClient(Builder builder) {
        this.apiKey = builder.apiKey;
        this.baseUrl = builder.baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(builder.timeout))
                .build();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Builder builder() {
        return new Builder();
    }

    private <T> T request(String method, String path, Object body, TypeReference<T> typeRef) {
        try {
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30));

            if ("POST".equals(method) && body != null) {
                reqBuilder.POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)));
            } else {
                reqBuilder.GET();
            }

            HttpResponse<String> response = httpClient.send(reqBuilder.build(), HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                handleError(response);
            }

            return objectMapper.readValue(response.body(), typeRef);
        } catch (SupervisorException e) {
            throw e;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("HTTP request failed", e);
        }
    }

    private void handleError(HttpResponse<String> response) {
        String message;
        String details = null;
        try {
            var body = objectMapper.readValue(response.body(), new TypeReference<Map<String, String>>() {});
            message = body.getOrDefault("error", "Unknown error");
            details = body.get("details");
        } catch (Exception e) {
            message = "HTTP " + response.statusCode();
        }
        throw new SupervisorException(response.statusCode(), message, details);
    }

    /**
     * Moderate text or an image for harmful content.
     *
     * @param request the moderation request
     * @return ModerationResponse with flagged status and detected labels
     */
    public ModerationResponse moderate(ModerationRequest request) {
        if (request.image() != null && !request.image().isBlank()) {
            request = new ModerationRequest(request.text(), ImagePrep.prepareImage(request.image()),
                    request.model(), request.enabledLabels(), request.includeContext(), request.includeImplicit());
        }
        return this.request("POST", "/api/moderate", request, new TypeReference<>() {});
    }

    /**
     * Moderate multiple texts in a single request.
     *
     * @param request the batch moderation request
     * @return list of ModerationResponse, one per input text
     */
    public List<ModerationResponse> moderateBatch(BatchModerationRequest request) {
        if (request.images() != null && !request.images().isEmpty()) {
            List<String> images = request.images().stream()
                    .map(image -> image == null || image.isBlank() ? image : ImagePrep.prepareImage(image))
                    .toList();
            request = new BatchModerationRequest(request.texts(), images,
                    request.model(), request.enabledLabels(), request.includeContext(), request.includeImplicit());
        }
        return this.request("POST", "/api/batch", request, new TypeReference<>() {});
    }

    /**
     * Check a username for policy violations.
     *
     * @param username the username to check
     * @return UsernameCheckResponse with flagged status and confidence score
     */
    public UsernameCheckResponse checkUsername(String username) {
        return this.request("POST", "/api/username", new UsernameCheckRequest(username), new TypeReference<>() {});
    }

    /**
     * Get all available moderation labels.
     *
     * @return map of label name to description for every label supported by the API
     */
    public Map<String, String> getLabels() {
        return this.request("GET", "/api/labels", null, new TypeReference<Map<String, String>>() {});
    }

    public static class Builder {
        private String apiKey;
        private String baseUrl = DEFAULT_BASE_URL;
        private long timeout = 30;

        public Builder apiKey(String apiKey) { this.apiKey = apiKey; return this; }
        public Builder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        public Builder timeout(long timeoutSeconds) { this.timeout = timeoutSeconds; return this; }

        public SupervisorClient build() {
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalArgumentException("API key is required");
            }
            return new SupervisorClient(this);
        }
    }
}
