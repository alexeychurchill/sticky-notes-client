package io.github.alexeychurchill.stickynotes.model;

/**
 * Service response
 */

public class ServiceResponse<T> {
    private int code;
    private String message;
    private boolean error;
    private T data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public static class Builder<T> {

        private int code = 0;
        private boolean error = false;
        private String message = "";
        private T data;
        public Builder setError(int code, String message) {
            this.code = code;
            this.message = message;
            error = true;
            return this;
        }

        public Builder setMessage(int code, String message) {
            this.code = code;
            this.message = message;
            error = false;
            return this;
        }

        public Builder setData(T data) {
            this.data = data;
            return this;
        }

        public ServiceResponse<T> create() {
            return new ServiceResponse<>(this);
        }

    }

    private ServiceResponse(Builder<T> builder) {
        this.code = builder.code;
        this.error = builder.error;
        this.message = builder.message;
        this.data = builder.data;
    }
}
