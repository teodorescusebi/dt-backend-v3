package com.debtstracker.dtv3.exceptions;

public class CustomNotFoundException extends BaseException {

    private final String cause;

    public CustomNotFoundException(Class<?> cause) {
        this.cause = cause.getSimpleName();
    }

    public CustomNotFoundException(String cause) {
        this.cause = cause;
    }

    public String getErrorMessage() {
        return this.cause.toUpperCase() + "_NOT_FOUND";
    }

}
