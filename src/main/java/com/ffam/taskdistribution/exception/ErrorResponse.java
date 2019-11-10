package com.ffam.taskdistribution.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Long timestamp;

    private String error;

    private int status;

    private String exception;

    private String message;

    private String path;

    public static Builder error() {
        return new Builder(new ErrorResponse());
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }

    public int getStatus() {
        return status;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public static class Builder {
        private ErrorResponse errorResponse;

        private Builder(ErrorResponse errorResponse) {
            this.errorResponse = errorResponse;
        }

        public Builder timestamp() {
            errorResponse.timestamp = System.currentTimeMillis();
            return this;
        }

        public Builder status(int status) {
            errorResponse.status = status;
            return this;
        }

        public Builder error(String message) {
            errorResponse.error = message;
            return this;
        }

        public Builder exception(String exception) {
            errorResponse.exception = exception;
            return this;
        }

        public Builder message(String message) {
            errorResponse.message = message;
            return this;
        }

        public ErrorResponse build() {
            return errorResponse;
        }
    }

}
