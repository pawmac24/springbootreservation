package com.pm.reservation.service;

import com.pm.reservation.model.ParameterModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by pmackiewicz on 2015-10-19.
 */
@Component
public class JMSProducer {

    public final static Logger logger = Logger.getLogger(JMSProducer.class);
    private static final String SIMPLE_IN_QUEUE = "simple_in.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(ParameterModel parameterModel) {
        // Send a message
        jmsTemplate.convertAndSend(SIMPLE_IN_QUEUE, parameterModel);
        logger.info("Sending parameterModel = " + parameterModel + " into queue " + SIMPLE_IN_QUEUE);
    }
}
