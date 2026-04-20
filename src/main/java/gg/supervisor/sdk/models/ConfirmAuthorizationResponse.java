package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Result of confirming authorization. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ConfirmAuthorizationResponse(
        @JsonProperty("user_id") String userId,
        String email) {
}
