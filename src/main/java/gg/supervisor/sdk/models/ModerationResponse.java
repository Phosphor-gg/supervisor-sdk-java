package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Result of a moderation request. Label fields are plain strings (not the
 * ModerationLabel enum) so new or aliased labels returned by the server pass
 * through instead of failing deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ModerationResponse(
        boolean flagged,
        List<String> labels,
        @JsonProperty("implicit_labels") List<String> implicitLabels,
        @JsonProperty("model_version") String modelVersion,
        @JsonProperty("needs_context") Boolean needsContext,
        @JsonProperty("context_labels") List<String> contextLabels,
        @JsonProperty("rewritten_text") String rewrittenText) {

    /** Returns true if the content was flagged. */
    public boolean isFlagged() {
        return flagged;
    }
}
