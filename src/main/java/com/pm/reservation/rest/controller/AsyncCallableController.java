package com.pm.reservation.rest.controller;

import com.pm.reservation.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;


import java.util.concurrent.Callable;

/**
 * Created by pmackiewicz on 2015-10-14.
 */
@RestController
public class AsyncCallableController {
    private final Logger logger = Logger.getLogger(this.getClass());
    private final TaskService taskService;

    @Autowired
    public AsyncCallableController(TaskService taskService) {
        this.taskService = taskService;
    }

    //@RequestMapping(value = "/callable", method = RequestMethod.GET, produces = "text/html")
    @RequestMapping(value = "/callable", produces = "application/json")
    public Callable<String> executeSlowTask() {
        logger.info("Request received");
        Callable<String> callable = taskService::execute;
        logger.info("Servlet thread released");

        return callable;
    }
}