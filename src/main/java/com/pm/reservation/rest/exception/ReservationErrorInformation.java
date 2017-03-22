package com.pm.reservation.rest.exception;

/**
 * Created by pmackiewicz on 2015-09-18.
 */
public class ReservationErrorInformation {
    private String errorMessage;

    public ReservationErrorInformation(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
