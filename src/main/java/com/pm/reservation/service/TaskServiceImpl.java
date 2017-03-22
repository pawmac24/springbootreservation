package com.pm.reservation.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by pmackiewicz on 2015-10-14.
 */
@Service
public class TaskServiceImpl implements TaskService {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public String execute() {
        try {
            Thread.sleep(5000);
            logger.info("Slow task executed");
            return "Task finished";
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}