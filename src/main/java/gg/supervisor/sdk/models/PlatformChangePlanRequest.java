package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Change an active subscription's plan for a platform user. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlatformChangePlanRequest(
        @JsonProperty("user_email") String userEmail,
        Tier tier,
        @JsonProperty("billing_cycle") BillingCycle billingCycle) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userEmail;
        private Tier tier;
        private BillingCycle billingCycle;

        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder tier(Tier tier) { this.tier = tier; return this; }
        public Builder billingCycle(BillingCycle billingCycle) { this.billingCycle = billingCycle; return this; }

        public PlatformChangePlanRequest build() {
            return new PlatformChangePlanRequest(userEmail, tier, billingCycle);
        }
    }
}
