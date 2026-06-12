package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;

/** Content moderation category labels. */
public enum ModerationLabel {
    PROFANITY("profanity"),
    TOXICITY("toxicity"),
    HARASSMENT("harassment"),
    HATE("hate"),
    INSULT("insult"),
    SEXUAL("sexual"),
    SEXUAL_MINORS("sexual/minors"),
    SEXUAL_EXPLICIT("sexual/explicit"),
    SENSITIVE("sensitive"),
    VIOLENCE("violence"),
    SELF_HARM("self-harm"),
    MEDICAL("medical"),
    SPAM("spam"),
    PROMOTIONAL("promotional"),
    SCAM("scam"),
    ILLEGAL("illegal");

    private final String value;

    ModerationLabel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static ModerationLabel fromValue(String value) {
        for (ModerationLabel label : values()) {
            if (label.value.equals(value)) {
                return label;
            }
        }
        throw new IllegalArgumentException("Unknown label: " + value);
    }
}
