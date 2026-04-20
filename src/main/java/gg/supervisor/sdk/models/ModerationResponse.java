package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Result of a moderation request. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ModerationResponse(
        boolean flagged,
        List<ModerationLabel> labels,
        @JsonProperty("implicit_labels") List<ModerationLabel> implicitLabels,
        @JsonProperty("model_version") String modelVersion,
        @JsonProperty("needs_context") Boolean needsContext,
        @JsonProperty("context_labels") List<ModerationLabel> contextLabels,
        @JsonProperty("rewritten_text") String rewrittenText) {

    /** Returns true if the content was flagged. */
    public boolean isFlagged() {
        return flagged;
    }
}
