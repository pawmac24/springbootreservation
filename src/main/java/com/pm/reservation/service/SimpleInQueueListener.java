package com.pm.reservation.service;

import com.pm.reservation.model.ParameterModel;
import com.pm.reservation.model.Reservation;
import com.pm.reservation.repository.ReservationRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by pmackiewicz on 2015-10-13.
 */
@Component
public class SimpleInQueueListener {

    public final static Logger logger = Logger.getLogger(SimpleInQueueListener.class);
    private static final String SIMPLE_IN_QUEUE = "simple_in.queue";
    private static final String SIMPLE_OUT_QUEUE = "simple_out.queue";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = SIMPLE_IN_QUEUE)
    public void receiveParameters(ParameterModel parameterModel) throws InterruptedException {
        logger.info("Received <" + parameterModel + ">");
        Thread.sleep(5000);
        List<Reservation> reservationList = reservationRepository.findByPriceBetween(parameterModel.getPriceFrom(), parameterModel.getPriceTo());

        jmsTemplate.convertAndSend(SIMPLE_OUT_QUEUE, reservationList);
        logger.info("Sending " + reservationList);
    }
}
