package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;

/** Available AI moderation models. */
public enum ModerationModel {
    AUTO("auto"),
    OBSERVER("observer"),
    SENTINEL("sentinel"),
    ARBITER("arbiter");

    private final String value;

    ModerationModel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
