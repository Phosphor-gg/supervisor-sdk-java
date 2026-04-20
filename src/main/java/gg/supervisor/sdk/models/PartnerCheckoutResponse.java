package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Checkout session URL response. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PartnerCheckoutResponse(@JsonProperty("checkout_url") String checkoutUrl) {
}
