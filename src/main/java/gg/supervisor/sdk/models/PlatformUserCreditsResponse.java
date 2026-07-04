package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Remaining credits of an authorized linked user. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlatformUserCreditsResponse(
        @JsonProperty("user_id") String userId,
        String email,
        long balance,
        @JsonProperty("monthly_allocation") long monthlyAllocation,
        @JsonProperty("used_this_month") long usedThisMonth,
        @JsonProperty("remaining_this_month") long remainingThisMonth,
        @JsonProperty("extra_credits") long extraCredits,
        @JsonProperty("reset_date") String resetDate) {
}
