package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Create a checkout session for a platform user. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlatformCheckoutRequest(
        @JsonProperty("user_email") String userEmail,
        Tier tier,
        @JsonProperty("billing_cycle") BillingCycle billingCycle,
        @JsonProperty("success_url") String successUrl,
        @JsonProperty("cancel_url") String cancelUrl) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userEmail;
        private Tier tier;
        private BillingCycle billingCycle;
        private String successUrl;
        private String cancelUrl;

        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder tier(Tier tier) { this.tier = tier; return this; }
        public Builder billingCycle(BillingCycle billingCycle) { this.billingCycle = billingCycle; return this; }
        public Builder successUrl(String successUrl) { this.successUrl = successUrl; return this; }
        public Builder cancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; return this; }

        public PlatformCheckoutRequest build() {
            return new PlatformCheckoutRequest(userEmail, tier, billingCycle, successUrl, cancelUrl);
        }
    }
}
