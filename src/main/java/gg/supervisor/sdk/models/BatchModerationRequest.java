package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Request to moderate multiple texts at once. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BatchModerationRequest(
        List<String> texts,
        ModerationModel model,
        @JsonProperty("enabled_labels") List<ModerationLabel> enabledLabels,
        @JsonProperty("include_context") Boolean includeContext) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> texts;
        private ModerationModel model;
        private List<ModerationLabel> enabledLabels;
        private Boolean includeContext;

        public Builder texts(List<String> texts) { this.texts = texts; return this; }
        public Builder model(ModerationModel model) { this.model = model; return this; }
        public Builder enabledLabels(List<ModerationLabel> labels) { this.enabledLabels = labels; return this; }
        public Builder includeContext(boolean includeContext) { this.includeContext = includeContext; return this; }

        public BatchModerationRequest build() {
            return new BatchModerationRequest(texts, model, enabledLabels, includeContext);
        }
    }
}
