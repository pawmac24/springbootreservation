package com.pm.reservation.service;

import com.pm.reservation.model.MyObject;
import com.pm.reservation.model.Reservation;
import com.pm.reservation.repository.ReservationRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by pmackiewicz on 2015-10-15.
 */
@Service
public class TaskServicePriceImpl implements TaskServicePrice{
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<Reservation> execute(BigDecimal priceFrom, BigDecimal priceTo) {
        try {
            Thread.sleep(3000);
            logger.info("Slow service price task executed");
            //
            List<Reservation> reservationList = reservationRepository.findByPriceBetween(priceFrom, priceTo);
            //
            return reservationList;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
