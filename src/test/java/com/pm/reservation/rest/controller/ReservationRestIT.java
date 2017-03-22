package com.pm.reservation.rest.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.pm.reservation.MainApplication;
import com.pm.reservation.model.Person;
import com.pm.reservation.model.Reservation;
import com.pm.reservation.model.Status;
import com.pm.reservation.repository.PersonRepository;
import com.pm.reservation.repository.ReservationRepository;
import com.pm.reservation.rest.controller.ReservationRestController;
import com.pm.reservation.service.PersonService;
import com.pm.reservation.service.ReservationService;
import com.pm.reservation.service.ReservationServiceImpl;
import com.pm.reservation.utils.DateUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
//import static com.jayway.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;


/**
 * Created by pmackiewicz on 2015-09-21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@TestPropertySource("classpath:application-it.properties")
public class ReservationRestIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PersonService personService;

    @Value("${local.server.port}")
    private int port;

    private Reservation reservation1;
    private Reservation reservation2;

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
    public void testGetByReservationById() {
        when().
                get("/reservations/id/1").
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("id", equalTo(1)).
                body("createDate", equalTo(1441708200000L)).
                body("price", equalTo(100.00f)).
                body("timeInterval", equalTo(1800)).
                body("comment", equalTo("comment1")).
                body("status", equalTo(2))
        ;
    }

    @Test
    public void testGetByReservationDates() {
        String jsonAsString = when().
                get("/reservations/datefrom/1441058400000/dateto/1443564000000").
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response().asString();

        // first we put our 'jsonAsString' into an ArrayList of Maps of type <String, ?>
        List<Map<String,?>> jsonAsArrayList = from(jsonAsString).get("");

        // now we count the number of entries in the JSON file, each entry is 1 reservation
        assertThat(jsonAsArrayList.size(), equalTo(5));

        assertThat((Integer)jsonAsArrayList.get(0).get("id"), equalTo(3));
        assertThat((Long)jsonAsArrayList.get(0).get("createDate"), equalTo(1441710000000L));
        assertThat((Float)jsonAsArrayList.get(0).get("price"), equalTo(200.00f));
        assertThat((Integer)jsonAsArrayList.get(0).get("timeInterval"), equalTo(2400));
        assertThat((String)jsonAsArrayList.get(0).get("comment"), equalTo("comment ..."));
        assertThat((Integer)jsonAsArrayList.get(0).get("status"), equalTo(Status.EXPIRED.getValue()));

        assertThat((Integer)jsonAsArrayList.get(1).get("id"), equalTo(6));
        assertThat((Long)jsonAsArrayList.get(1).get("createDate"), equalTo(1442298600000L));
        assertThat((Float)jsonAsArrayList.get(1).get("price"), equalTo(400.00f));
        assertThat((Integer)jsonAsArrayList.get(1).get("timeInterval"), equalTo(2400));
        assertThat((String)jsonAsArrayList.get(1).get("comment"), equalTo("comment ..."));
        assertThat((Integer)jsonAsArrayList.get(1).get("status"), equalTo(Status.CANCELLED.getValue()));

        assertThat((Integer)jsonAsArrayList.get(2).get("id"), equalTo(1));
        assertThat((Long)jsonAsArrayList.get(2).get("createDate"), equalTo(1441708200000L));
        assertThat((Float)jsonAsArrayList.get(2).get("price"), equalTo(100.00f));
        assertThat((Integer)jsonAsArrayList.get(2).get("timeInterval"), equalTo(1800));
        assertThat((String)jsonAsArrayList.get(2).get("comment"), equalTo("comment1"));
        assertThat((Integer)jsonAsArrayList.get(2).get("status"), equalTo(Status.EXPIRED.getValue()));

        assertThat((Integer)jsonAsArrayList.get(3).get("id"), equalTo(2));
        assertThat((Long)jsonAsArrayList.get(3).get("createDate"), equalTo(1441710000000L));
        assertThat((Float)jsonAsArrayList.get(3).get("price"), equalTo(200.00f));
        assertThat((Integer)jsonAsArrayList.get(3).get("timeInterval"), equalTo(2400));
        assertThat((String)jsonAsArrayList.get(3).get("comment"), equalTo("comment2"));
        assertThat((Integer)jsonAsArrayList.get(3).get("status"), equalTo(Status.EXPIRED.getValue()));

        assertThat((Integer)jsonAsArrayList.get(4).get("id"), equalTo(5));
        assertThat((Long)jsonAsArrayList.get(4).get("createDate"), equalTo(1442739600000L));
        assertThat((Float)jsonAsArrayList.get(4).get("price"), equalTo(300.00f));
        assertThat((Integer)jsonAsArrayList.get(4).get("timeInterval"), equalTo(2400));
        assertThat((String)jsonAsArrayList.get(4).get("comment"), equalTo("comment ..."));
        assertThat((Integer)jsonAsArrayList.get(4).get("status"), equalTo(Status.EXPIRED.getValue()));
    }

    @Test
    public void testGetReservationByPesel() {
        String pesel = "77777755555";
        Date createDate1 = DateUtils.asDate(LocalDateTime.of(2015, Month.SEPTEMBER, 8, 12, 30));
        Date createDate2 = DateUtils.asDate(LocalDateTime.of(2015, Month.JUNE, 1, 10, 00));

        personService.deleteByPesel(pesel);

        Person person = new Person();
        person.setName("Igrek");
        person.setSurname("Zibowski");
        person.setPesel(pesel);
        personService.save(person);

        reservation1 = new Reservation();
        reservation1.setCreateDate(createDate1);
        reservation1.setPrice(new BigDecimal(100));
        reservation1.setTimeInterval(30L * 60L);
        reservation1.setStatus(Status.NEW.getValue());
        reservation1.setComment("comment ...");
        reservation1.setPerson(person);
        reservationService.save(reservation1);

        reservation2 = new Reservation();
        reservation2.setCreateDate(createDate2);
        reservation2.setPrice(new BigDecimal(250));
        reservation2.setTimeInterval(30L * 60L);
        reservation2.setStatus(Status.EXPIRED.getValue());
        reservation2.setComment("comment ...");
        reservation2.setPerson(person);
        reservationService.save(reservation2);

        String jsonAsString = when().
                get("/reservations/pesel/" + pesel).
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response().asString();

        // first we put our 'jsonAsString' into an ArrayList of Maps of type <String, ?>
        List<Map<String,?>> jsonAsArrayList = from(jsonAsString).get("");

        // now we count the number of entries in the JSON file, each entry is 1 reservation
        assertThat(jsonAsArrayList.size(), equalTo(2));

        assertThat((Long) jsonAsArrayList.get(0).get("createDate"), equalTo(1441708200000L));
        assertThat((Long) jsonAsArrayList.get(0).get("createDate"), equalTo(createDate1.getTime()));
        assertThat((Float) jsonAsArrayList.get(0).get("price"), equalTo(100.00f));
        assertThat((Integer) jsonAsArrayList.get(0).get("timeInterval"), equalTo(1800));
        assertThat((String) jsonAsArrayList.get(0).get("comment"), equalTo("comment ..."));
        assertThat((Integer) jsonAsArrayList.get(0).get("status"), equalTo(Status.NEW.getValue()));

        assertThat((Long) jsonAsArrayList.get(1).get("createDate"), equalTo(1433145600000L));
        assertThat((Long) jsonAsArrayList.get(1).get("createDate"), equalTo(createDate2.getTime()));
        assertThat((Float) jsonAsArrayList.get(1).get("price"), equalTo(250.00f));
        assertThat((Integer) jsonAsArrayList.get(1).get("timeInterval"), equalTo(1800));
        assertThat((String) jsonAsArrayList.get(1).get("comment"), equalTo("comment ..."));
        assertThat((Integer) jsonAsArrayList.get(1).get("status"), equalTo(Status.EXPIRED.getValue()));

        personService.deleteByPesel(pesel);
    }

    @Test
    public void testGetByPriceRange(){
        String jsonAsString = when().
                get("/reservations/pricefrom/200/priceto/250").
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response().asString();

        // first we put our 'jsonAsString' into an ArrayList of Maps of type <String, ?>
        List<Map<String,?>> jsonAsArrayList = from(jsonAsString).get("");

        // now we count the number of entries in the JSON file, each entry is 1 reservation
        assertThat(jsonAsArrayList.size(), equalTo(2));

        assertThat((Integer)jsonAsArrayList.get(0).get("id"), equalTo(3));
        assertThat((Long)jsonAsArrayList.get(0).get("createDate"), equalTo(1441710000000L));
        assertThat((Float)jsonAsArrayList.get(0).get("price"), equalTo(200.00f));
        assertThat((Integer)jsonAsArrayList.get(0).get("timeInterval"), equalTo(2400));
        assertThat((String)jsonAsArrayList.get(0).get("comment"), equalTo("comment ..."));
        assertThat((Integer)jsonAsArrayList.get(0).get("status"), equalTo(Status.EXPIRED.getValue()));

        assertThat((Integer)jsonAsArrayList.get(1).get("id"), equalTo(2));
        assertThat((Long)jsonAsArrayList.get(1).get("createDate"), equalTo(1441710000000L));
        assertThat((Float)jsonAsArrayList.get(1).get("price"), equalTo(200.00f));
        assertThat((Integer)jsonAsArrayList.get(1).get("timeInterval"), equalTo(2400));
        assertThat((String)jsonAsArrayList.get(1).get("comment"), equalTo("comment2"));
        assertThat((Integer)jsonAsArrayList.get(1).get("status"), equalTo(Status.EXPIRED.getValue()));

    }

    @Test
    public void testFindBySurname() {
        String pesel = "66666611111";
        String surname = "Malinowski";
        Date createDate1 = DateUtils.asDate(LocalDateTime.of(2015, Month.SEPTEMBER, 8, 12, 30));
        Date createDate2 = DateUtils.asDate(LocalDateTime.of(2015, Month.JUNE, 1, 10, 00));

        personService.deleteByPesel(pesel);

        Person person = new Person();
        person.setName("Andrzej");
        person.setSurname(surname);
        person.setPesel(pesel);
        personService.save(person);

        reservation1 = new Reservation();
        reservation1.setCreateDate(createDate1);
        reservation1.setPrice(new BigDecimal(100));
        reservation1.setTimeInterval(30L * 60L);
        reservation1.setStatus(Status.NEW.getValue());
        reservation1.setComment("comment ...");
        reservation1.setPerson(person);
        reservationService.save(reservation1);

        reservation2 = new Reservation();
        reservation2.setCreateDate(createDate2);
        reservation2.setPrice(new BigDecimal(250));
        reservation2.setTimeInterval(30L * 60L);
        reservation2.setStatus(Status.EXPIRED.getValue());
        reservation2.setComment("comment ...");
        reservation2.setPerson(person);
        reservationService.save(reservation2);

        String jsonAsString = when().
                get("/reservations/surname/" + surname).
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response().asString();

        // first we put our 'jsonAsString' into an ArrayList of Maps of type <String, ?>
        List<Map<String,?>> jsonAsArrayList = from(jsonAsString).get("");

        // now we count the number of entries in the JSON file, each entry is 1 reservation
        assertThat(jsonAsArrayList.size(), equalTo(2));

        assertThat((Long) jsonAsArrayList.get(0).get("createDate"), equalTo(1441708200000L));
        assertThat((Long) jsonAsArrayList.get(0).get("createDate"), equalTo(createDate1.getTime()));
        assertThat((Float) jsonAsArrayList.get(0).get("price"), equalTo(100.00f));
        assertThat((Integer) jsonAsArrayList.get(0).get("timeInterval"), equalTo(1800));
        assertThat((String) jsonAsArrayList.get(0).get("comment"), equalTo("comment ..."));
        assertThat((Integer) jsonAsArrayList.get(0).get("status"), equalTo(Status.NEW.getValue()));

        assertThat((Long) jsonAsArrayList.get(1).get("createDate"), equalTo(1433145600000L));
        assertThat((Long) jsonAsArrayList.get(1).get("createDate"), equalTo(createDate2.getTime()));
        assertThat((Float) jsonAsArrayList.get(1).get("price"), equalTo(250.00f));
        assertThat((Integer) jsonAsArrayList.get(1).get("timeInterval"), equalTo(1800));
        assertThat((String) jsonAsArrayList.get(1).get("comment"), equalTo("comment ..."));
        assertThat((Integer) jsonAsArrayList.get(1).get("status"), equalTo(Status.EXPIRED.getValue()));

        personService.deleteByPesel(pesel);
    }
}
