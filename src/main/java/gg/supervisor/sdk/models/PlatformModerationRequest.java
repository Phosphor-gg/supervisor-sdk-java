package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** Moderate content on behalf of a platform user. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlatformModerationRequest(
        @JsonProperty("user_email") String userEmail,
        String text,
        String image,
        ModerationModel model,
        @JsonProperty("enabled_labels") List<ModerationLabel> enabledLabels,
        @JsonProperty("include_context") Boolean includeContext) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userEmail;
        private String text;
        private String image;
        private ModerationModel model;
        private List<ModerationLabel> enabledLabels;
        private Boolean includeContext;

        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder text(String text) { this.text = text; return this; }
        public Builder image(String image) { this.image = image; return this; }
        public Builder model(ModerationModel model) { this.model = model; return this; }
        public Builder enabledLabels(List<ModerationLabel> labels) { this.enabledLabels = labels; return this; }
        public Builder includeContext(boolean includeContext) { this.includeContext = includeContext; return this; }

        public PlatformModerationRequest build() {
            return new PlatformModerationRequest(userEmail, text, image, model, enabledLabels, includeContext);
        }
    }
}
