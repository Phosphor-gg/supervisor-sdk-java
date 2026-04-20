package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Stripe Connect onboarding status. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StripeConnectStatusResponse(
        @JsonProperty("account_id") String accountId,
        @JsonProperty("onboarding_complete") boolean onboardingComplete,
        @JsonProperty("charges_enabled") boolean chargesEnabled) {
}
