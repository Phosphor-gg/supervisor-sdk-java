package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;

/** Subscription tiers. */
public enum Tier {
    FREE("Free"),
    BASIC("Basic"),
    STANDARD("Standard"),
    PREMIUM("Premium");

    private final String value;

    Tier(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
