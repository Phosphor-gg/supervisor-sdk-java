package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A subscription plan price a platform can sell. Amount is in cents.
 * paymentLink is always null on the platform products endpoint: mint links
 * via createCheckout so the revenue share applies.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanPrice(
        @JsonProperty("price_id") String priceId,
        @JsonProperty("product_id") String productId,
        Tier tier,
        @JsonProperty("billing_cycle") BillingCycle billingCycle,
        long amount,
        String currency,
        @JsonProperty("payment_link") String paymentLink) {
}
