package com.pm.reservation.rest.controller;

import com.pm.reservation.ExternalConfigComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pmackiewicz on 2015-09-28.
 */
@RestController
public class HelloRestController {

    private static final Logger logger = LoggerFactory.getLogger(HelloRestController.class);

    @Autowired
    private ExternalConfigComponent externalConfigComponent;

    @RequestMapping({"/hello", "/"})
    public String getHello() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("\n Property One = " + externalConfigComponent.getPropertyOne())
                .append("\n Property Two = " + externalConfigComponent.getPropertyTwo())
                .append("\n Property Three = " + externalConfigComponent.getPropertyThree())
                .append("\n Property Four = " + externalConfigComponent.getPropertyFour())
                .append("\n Property Five = " + externalConfigComponent.getPropertyFive())
                .append("\n Property Six = " + externalConfigComponent.getPropertySix())
                .append("\n Property Seven = " + externalConfigComponent.getPropertySeven());
        logger.info(stringBuilder.toString());

        return "Hello User :) !";
    }

}
