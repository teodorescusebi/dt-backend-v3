package com.debtstracker.dtv3.exceptions;

public class CustomConflictException extends BaseException {

    private final String message;

    public CustomConflictException(Type cause) {
        this.message = cause.toString();
    }

    public String getErrorMessage() {
        return this.message;
    }

    public enum Type {
        BAD_CREDENTIALS
    }
}
