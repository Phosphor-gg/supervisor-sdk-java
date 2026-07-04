package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** The lifetime (Verified) plan: one-time purchase, no billing cycle. Amount is in cents. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record LifetimePlan(
        @JsonProperty("product_id") String productId,
        @JsonProperty("price_id") String priceId,
        String name,
        long amount,
        String currency,
        @JsonProperty("monthly_credits") long monthlyCredits) {
}
