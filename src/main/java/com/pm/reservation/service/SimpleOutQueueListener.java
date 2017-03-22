package com.pm.reservation.service;

import com.pm.reservation.model.ParameterModel;
import com.pm.reservation.model.Reservation;
import com.pm.reservation.repository.ReservationRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmackiewicz on 2015-10-13.
 */
@Component
public class SimpleOutQueueListener {

    public final static Logger logger = Logger.getLogger(SimpleOutQueueListener.class);
    private static final String SIMPLE_OUT_QUEUE = "simple_out.queue";

    private List<Reservation> reservationList = new ArrayList<>();

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    @JmsListener(destination = SIMPLE_OUT_QUEUE)
    public void receiveResults(List<Reservation> reservationList) {
        logger.info("Received results <" + reservationList + ">");
        this.reservationList = reservationList;
    }
}
