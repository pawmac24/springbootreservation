package com.pm.reservation;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.pm.reservation.model.ParameterModel;
import com.pm.reservation.service.JMSProducer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;

/**
 * Created by pmackiewicz on 2015-10-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@TestPropertySource("classpath:application-it.properties")
public class JMSTest {

    @Autowired
    private JMSProducer jmsProducer;

    @Autowired
    private WebApplicationContext context;

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setUp() {
        RestAssuredMockMvc.webAppContextSetup(context);
        RestAssured.port = port;
    }

    @After
    public void tearDown() {
        RestAssuredMockMvc.reset();
    }
    @Test
    public void sendSimpleMessage() throws InterruptedException {
        ParameterModel parameterModel = new ParameterModel();
        parameterModel.setPriceFrom(new BigDecimal(50.00));
        parameterModel.setPriceTo(new BigDecimal(100.00));
        jmsProducer.sendMessage(parameterModel);
        Thread.sleep(1000L);
        //assertTrue(this.outputCapture.toString().contains("Test message"));
    }
}
