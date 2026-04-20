package gg.supervisor.sdk.exceptions;

/** Base exception for Supervisor API errors. */
public class SupervisorException extends RuntimeException {
    private final int statusCode;
    private final String details;

    public SupervisorException(int statusCode, String message, String details) {
        super("[" + statusCode + "] " + message + (details != null ? ": " + details : ""));
        this.statusCode = statusCode;
        this.details = details;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDetails() {
        return details;
    }

    public boolean isAuthError() {
        return statusCode == 401;
    }

    public boolean isRateLimit() {
        return statusCode == 429;
    }

    public boolean isValidationError() {
        return statusCode == 400 || statusCode == 422;
    }
}
