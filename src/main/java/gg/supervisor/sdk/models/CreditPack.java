package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** A one-time credit pack a platform can sell. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreditPack(
        String id,
        @JsonProperty("price_id") String priceId,
        String name,
        String description,
        @JsonProperty("price_cents") long priceCents,
        String currency,
        @JsonProperty("credits_amount") long creditsAmount) {
}
