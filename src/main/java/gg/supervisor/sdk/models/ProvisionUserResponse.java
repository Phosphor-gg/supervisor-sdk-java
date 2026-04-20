package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Result of provisioning a user. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProvisionUserResponse(
        @JsonProperty("user_id") String userId,
        String email,
        @JsonProperty("is_new_account") boolean isNewAccount,
        @JsonProperty("is_newly_linked") boolean isNewlyLinked) {
}
