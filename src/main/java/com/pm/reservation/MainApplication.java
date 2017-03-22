package com.pm.reservation;

import com.pm.reservation.beans.FlywayComponent;
import com.pm.reservation.model.Person;
import com.pm.reservation.model.Reservation;
import com.pm.reservation.service.PersonService;
import com.pm.reservation.service.ReservationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
@EnableJms
@PropertySource("classpath:application.properties")
public class MainApplication extends SpringBootServletInitializer implements CommandLineRunner {

    public final static Logger logger = Logger.getLogger(MainApplication.class);

    @Autowired
    PersonService personService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    private ExternalConfigComponent externalConfigComponent;

    @Autowired
    private FlywayComponent flywayComponent;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("==> MainApplication BEFORE migration:");

        flywayComponent.startOnNew();

        logger.info("==> MainApplication migrate finished:");

        for (Person personElement : personService.findAll()) {
            logger.debug("Person= " + personElement);
        }

        for (Reservation reservationElement : reservationService.findAll()) {
            logger.debug("Reservation= " +reservationElement);
        }
    }
}
