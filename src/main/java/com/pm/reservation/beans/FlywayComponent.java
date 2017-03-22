package com.pm.reservation.beans;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by pmackiewicz on 2015-10-15.
 */
@Component
public class FlywayComponent {
    private static Logger logger = LoggerFactory.getLogger(FlywayComponent.class);

    private Flyway flyway;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    private void init()
    {
        flyway = new Flyway();
        flyway.setDataSource(url, userName, password);
    }

    public void clean()
    {
        flyway.clean();
    }

    public void migrate()
    {
        flyway.migrate();
    }

    public void startOnNew()
    {
        init();
        flyway.clean();
        flyway.migrate();
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("Url: " + url);
        logger.info("UserName: " + userName);
    }
}
