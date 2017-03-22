package com.pm.reservation.rest.controller;

import com.pm.reservation.model.MyObject;
import com.pm.reservation.service.TaskService;
import com.pm.reservation.service.TaskServiceObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by pmackiewicz on 2015-10-14.
 */
@RestController
public class AsyncDeferredController {
    private final Logger logger = Logger.getLogger(this.getClass());
    private final TaskService taskService;
    private final TaskServiceObject taskServiceObject;

    @Autowired
    public AsyncDeferredController(TaskService taskService, TaskServiceObject taskServiceObject) {
        this.taskService = taskService;
        this.taskServiceObject = taskServiceObject;
    }

    //@RequestMapping(value = "/deferred", method = RequestMethod.GET, produces = "text/html")
    @RequestMapping(value = "/deferred", produces = "application/json")
    public DeferredResult<String> executeSlowTask() {
        logger.info("Request received");
        DeferredResult<String> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            logger.info("Before execute");
            return taskService.execute();
        }).whenCompleteAsync((result, throwable) -> {
            logger.info("Complete");
            deferredResult.setResult(result);
        });
        logger.info("Servlet thread released");

        return deferredResult;
    }

    //@RequestMapping(value = "/object", produces = "application/json")
    @RequestMapping(value = "/restasync")
    public DeferredResult<List<MyObject>> executeSlowTask2() {
        logger.info("Request received");
        DeferredResult<List<MyObject>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            logger.info("Before execute");
            return taskServiceObject.execute();
        }).whenComplete((result, throwable) -> {
            logger.info("Complete");
            deferredResult.setResult(result);
        }).supplyAsync(() -> {
            logger.info("Before execute 2");
            return taskServiceObject.execute();
        }).whenComplete((result, throwable) -> {
            logger.info("Complete 2");
            deferredResult.setResult(result);
        }).thenApply(k -> k.add(new MyObject("z55", 55))).thenAccept(k -> System.out.println(k));
        //
        logger.info("Servlet thread released");

        return deferredResult;
    }

}