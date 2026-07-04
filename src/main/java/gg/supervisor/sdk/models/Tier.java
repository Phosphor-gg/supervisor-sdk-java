package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;

/** Subscription tiers. */
public enum Tier {
    FREE("free"),
    BASIC("basic"),
    STANDARD("standard"),
    PREMIUM("premium"),
    /** Lifetime plan: one-time purchase, no billing cycle. */
    VERIFIED("verified");

    private final String value;

    Tier(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
