package com.pm.reservation.model;

/**
 * Created by pmackiewicz on 2015-09-22.
 */
public enum Status {
    NEW(0),
    CANCELLED(1),
    EXPIRED(2),
    REALIZED(3);

    public static boolean isValid(int status) {
        for(Status enumStatus : Status.values()) {
            if( enumStatus.getValue() == status ) {
                return true;
            }
        }
        return false;
    }
    private final int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
