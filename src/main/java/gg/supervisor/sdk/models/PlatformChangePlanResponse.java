package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Result of a plan change. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlatformChangePlanResponse(
        @JsonProperty("subscription_id") String subscriptionId,
        Tier tier,
        @JsonProperty("billing_cycle") BillingCycle billingCycle) {
}
