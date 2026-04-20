package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;

/** Billing cycle options. */
public enum BillingCycle {
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    ANNUAL("Annual"),
    TRIENNIAL("Triennial");

    private final String value;

    BillingCycle(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
