package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Everything a platform can sell: plans and credit packs. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlatformProductsResponse(
        List<PlanPrice> plans,
        @JsonProperty("credit_packs") List<CreditPack> creditPacks) {
}
