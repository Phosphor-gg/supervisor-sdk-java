package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Request to moderate multiple texts at once. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BatchModerationRequest(
        List<String> texts,
        List<String> images,
        ModerationModel model,
        @JsonProperty("enabled_labels") List<ModerationLabel> enabledLabels,
        @JsonProperty("include_context") Boolean includeContext) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> texts;
        private List<String> images;
        private ModerationModel model;
        private List<ModerationLabel> enabledLabels;
        private Boolean includeContext;

        public Builder texts(List<String> texts) { this.texts = texts; return this; }
        public Builder images(List<String> images) { this.images = images; return this; }
        public Builder model(ModerationModel model) { this.model = model; return this; }
        public Builder enabledLabels(List<ModerationLabel> labels) { this.enabledLabels = labels; return this; }
        public Builder includeContext(boolean includeContext) { this.includeContext = includeContext; return this; }

        public BatchModerationRequest build() {
            if (texts != null && !texts.isEmpty()
                    && images != null && !images.isEmpty()
                    && texts.size() != images.size()) {
                throw new IllegalArgumentException(
                        "When both texts and images are provided, their lengths must be equal: "
                                + "texts=" + texts.size() + ", images=" + images.size());
            }
            return new BatchModerationRequest(texts, images, model, enabledLabels, includeContext);
        }
    }
}
