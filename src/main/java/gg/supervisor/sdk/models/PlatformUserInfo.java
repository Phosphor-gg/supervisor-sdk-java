package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Platform's view of a linked user. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlatformUserInfo(
        @JsonProperty("user_id") String userId,
        String email,
        @JsonProperty("linked_at") String linkedAt,
        boolean authorized,
        @JsonProperty("has_active_subscription") boolean hasActiveSubscription,
        Tier tier) {
}
