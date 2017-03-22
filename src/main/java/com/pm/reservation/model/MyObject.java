package com.pm.reservation.model;

/**
 * Created by pmackiewicz on 2015-10-15.
 */
public class MyObject {
    private String x;
    private long y;

    public MyObject() {
    }

    public MyObject(String x, long y) {
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return "MyObject{" +
                "x='" + x + '\'' +
                ", y=" + y +
                '}';
    }
}
