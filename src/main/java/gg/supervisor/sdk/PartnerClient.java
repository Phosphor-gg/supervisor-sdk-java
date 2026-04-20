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
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Client for the Supervisor Partner API with OAuth2 client credentials.
 * Handles automatic token exchange and refresh.
 */
public class PartnerClient {
    private static final String DEFAULT_BASE_URL = "https://api.supervisor.gg";

    private final String clientId;
    private final String clientSecret;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private String accessToken;
    private Instant tokenExpiresAt = Instant.EPOCH;

    private PartnerClient(Builder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
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

    private synchronized String ensureToken() {
        if (accessToken != null && Instant.now().isBefore(tokenExpiresAt.minusSeconds(30))) {
            return accessToken;
        }

        try {
            var tokenReq = Map.of(
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "grant_type", "client_credentials"
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/api/partner/token"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(tokenReq)))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                handleError(response);
            }

            var tokenResp = objectMapper.readValue(response.body(), PartnerTokenResponse.class);
            this.accessToken = tokenResp.accessToken();
            this.tokenExpiresAt = Instant.now().plusSeconds(tokenResp.expiresIn());
            return this.accessToken;
        } catch (SupervisorException e) {
            throw e;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Token exchange failed", e);
        }
    }

    private <T> T request(String method, String path, Object body, TypeReference<T> typeRef) {
        String token = ensureToken();
        try {
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .header("Authorization", "Bearer " + token)
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

    /** Provision or link a user by email. */
    public ProvisionUserResponse provisionUser(String email) {
        return request("POST", "/api/partner/users/provision", Map.of("email", email), new TypeReference<>() {});
    }

    /** List all users linked to this partner. */
    public List<PartnerUserInfo> listUsers() {
        return request("GET", "/api/partner/users", null, new TypeReference<>() {});
    }

    /** Get a specific linked user by ID. */
    public PartnerUserInfo getUser(String userId) {
        return request("GET", "/api/partner/users/" + userId, null, new TypeReference<>() {});
    }

    /** Moderate content on behalf of a linked user. */
    public ModerationResponse moderate(PartnerModerationRequest req) {
        return request("POST", "/api/partner/moderate", req, new TypeReference<>() {});
    }

    /** Create a Stripe checkout session for a partner user. */
    public PartnerCheckoutResponse createCheckout(PartnerCheckoutRequest req) {
        return request("POST", "/api/partner/checkout", req, new TypeReference<>() {});
    }

    /** Confirm a user's authorization with the provided code. */
    public ConfirmAuthorizationResponse confirmAuthorization(String code) {
        return request("POST", "/api/partner/users/confirm-authorization", Map.of("code", code), new TypeReference<>() {});
    }

    /** Get the Stripe Connect onboarding status. */
    public StripeConnectStatusResponse getConnectStatus() {
        return request("GET", "/api/partner/connect/status", null, new TypeReference<>() {});
    }

    public static class Builder {
        private String clientId;
        private String clientSecret;
        private String baseUrl = DEFAULT_BASE_URL;
        private long timeout = 30;

        public Builder clientId(String clientId) { this.clientId = clientId; return this; }
        public Builder clientSecret(String clientSecret) { this.clientSecret = clientSecret; return this; }
        public Builder baseUrl(String baseUrl) { this.baseUrl = baseUrl; return this; }
        public Builder timeout(long timeoutSeconds) { this.timeout = timeoutSeconds; return this; }

        public PartnerClient build() {
            if (clientId == null || clientId.isBlank()) {
                throw new IllegalArgumentException("Client ID is required");
            }
            if (clientSecret == null || clientSecret.isBlank()) {
                throw new IllegalArgumentException("Client secret is required");
            }
            return new PartnerClient(this);
        }
    }
}
