package com.pm.reservation.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by pmackiewicz on 2015-09-18.
 */
public class ReservationNotFoundException extends Exception {
    private static final long serialVersionUID = 100L;

    public ReservationNotFoundException(String message) {
        super(message);
    }
}