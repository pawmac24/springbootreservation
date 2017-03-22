package com.pm.reservation.rest.exception;

/**
 * Created by pmackiewicz on 2015-09-22.
 */
public class ArgumentValidationException extends Exception {
    private static final long serialVersionUID = 100L;

    public ArgumentValidationException(String message) {
        super(message);
    }
}
