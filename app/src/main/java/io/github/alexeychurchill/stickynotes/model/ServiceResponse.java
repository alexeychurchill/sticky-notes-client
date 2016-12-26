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

    public boolean containsData() {
        return data != null;
    }

    public static class Builder<T> {
        private int code = 0;
        private boolean error = false;
        private String message = "";
        private T data;

        public Builder<T> setResponse(boolean error, int code, String message) {
            this.error = error;
            this.code = code;
            this.message = message;
            return this;
        }

        public Builder<T> setError(int code, String message) {
            setResponse(true, code, message);
            return this;
        }

        public Builder<T> setMessage(int code, String message) {
            setResponse(false, code, message);
            return this;
        }

        public Builder<T> setData(T data) {
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
