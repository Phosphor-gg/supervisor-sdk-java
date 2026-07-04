package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Credit pack checkout for a linked user. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlatformCreditCheckoutRequest(
        @JsonProperty("user_email") String userEmail,
        @JsonProperty("price_id") String priceId,
        @JsonProperty("success_url") String successUrl,
        @JsonProperty("cancel_url") String cancelUrl) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userEmail;
        private String priceId;
        private String successUrl;
        private String cancelUrl;

        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder priceId(String priceId) { this.priceId = priceId; return this; }
        public Builder successUrl(String successUrl) { this.successUrl = successUrl; return this; }
        public Builder cancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; return this; }

        public PlatformCreditCheckoutRequest build() {
            return new PlatformCreditCheckoutRequest(userEmail, priceId, successUrl, cancelUrl);
        }
    }
}
