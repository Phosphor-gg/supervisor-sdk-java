package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Request to moderate text or an image. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ModerationRequest(
        String text,
        String image,
        ModerationModel model,
        @JsonProperty("enabled_labels") List<ModerationLabel> enabledLabels,
        @JsonProperty("include_context") Boolean includeContext,
        @JsonProperty("include_implicit") Boolean includeImplicit) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String text;
        private String image;
        private ModerationModel model;
        private List<ModerationLabel> enabledLabels;
        private Boolean includeContext;
        private Boolean includeImplicit;

        public Builder text(String text) { this.text = text; return this; }
        public Builder image(String image) { this.image = image; return this; }
        public Builder model(ModerationModel model) { this.model = model; return this; }
        public Builder enabledLabels(List<ModerationLabel> labels) { this.enabledLabels = labels; return this; }
        public Builder includeContext(boolean includeContext) { this.includeContext = includeContext; return this; }
        public Builder includeImplicit(boolean includeImplicit) { this.includeImplicit = includeImplicit; return this; }

        public ModerationRequest build() {
            return new ModerationRequest(text, image, model, enabledLabels, includeContext, includeImplicit);
        }
    }
}
