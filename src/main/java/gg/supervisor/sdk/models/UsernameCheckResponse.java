package gg.supervisor.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Result of a username check. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsernameCheckResponse(boolean flagged, double score) {

    /** Returns true if the username was flagged. */
    public boolean isFlagged() {
        return flagged;
    }
}
