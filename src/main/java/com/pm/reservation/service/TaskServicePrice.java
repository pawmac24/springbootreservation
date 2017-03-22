package com.pm.reservation.service;

import com.pm.reservation.model.MyObject;
import com.pm.reservation.model.Reservation;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by pmackiewicz on 2015-10-14.
 */
public interface TaskServicePrice {
    List<Reservation> execute(BigDecimal priceFrom, BigDecimal priceTo);
}
