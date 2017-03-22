package com.pm.reservation.service;

import com.pm.reservation.model.Person;
import com.pm.reservation.model.Reservation;
import com.pm.reservation.model.Status;
import com.pm.reservation.repository.ReservationRepository;

import com.pm.reservation.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by pmackiewicz on 2015-09-22.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceImplTest {

    @InjectMocks
    private ReservationService reservationService = new ReservationServiceImpl();

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private JmsTemplate jmsTemplate;

    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;
    private Reservation reservation4;
    private Reservation reservation5;

    @Before
    public void setUp() {

        HashSet<Reservation> reservations = new HashSet<>();
        Person person = new Person();
        person.setId(1L);
        person.setName("Andrzej");
        person.setSurname("Markowski");
        person.setPesel("80092205517");

        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setCreateDate(DateUtils.asDate(LocalDateTime.of(2015, Month.SEPTEMBER, 8, 12, 30)));
        reservation1.setPrice(new BigDecimal(100));
        reservation1.setTimeInterval(30L * 60L);
        reservation1.setStatus(Status.NEW.getValue());
        reservation1.setComment("comment1");
        reservation1.setPerson(person);
        reservations.add(reservation1);

        reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setCreateDate(DateUtils.asDate(LocalDateTime.of(2015, Month.SEPTEMBER, 8, 13, 00)));
        reservation2.setPrice(new BigDecimal(200));
        reservation2.setTimeInterval(40L * 60L);
        reservation2.setStatus(Status.NEW.getValue());
        reservation2.setComment("comment2");
        reservation2.setPerson(person);
        reservations.add(reservation2);

        person = new Person();
        person.setId(2L);
        person.setName("Pawel");
        person.setSurname("Kurski");
        person.setPesel("75040201624");

        reservation3 = new Reservation();
        reservation3.setId(3L);
        reservation3.setCreateDate(DateUtils.asDate(LocalDateTime.of(2015, Month.SEPTEMBER, 20, 11, 00)));
        reservation3.setPrice(new BigDecimal(250));
        reservation3.setTimeInterval(40L * 60L);
        reservation3.setStatus(Status.NEW.getValue());
        reservation3.setComment("comment3");
        reservation3.setPerson(person);
        reservations.add(reservation3);

        reservation4 = new Reservation();
        reservation4.setId(4L);
        reservation4.setCreateDate(DateUtils.asDate(LocalDateTime.of(2015, Month.SEPTEMBER, 15, 8, 30)));
        reservation4.setPrice(new BigDecimal(300));
        reservation4.setTimeInterval(50L * 60L);
        reservation4.setStatus(Status.CANCELLED.getValue());
        reservation4.setComment("comment4");
        reservation4.setPerson(person);
        reservations.add(reservation4);

        reservation5 = new Reservation();
        reservation5.setId(5L);
        reservation5.setCreateDate(DateUtils.asDate(LocalDateTime.now().minusMinutes(10)));
        reservation5.setPrice(new BigDecimal(350));
        reservation5.setTimeInterval(40L * 60L);
        reservation5.setStatus(Status.NEW.getValue());
        reservation5.setComment("expirationtime");
        reservation5.setPerson(person);
        reservations.add(reservation5);
    }

    @Test
    public void testFindById()
    {
        when(reservationRepository.findOne(1L)).thenReturn(this.reservation1);
        when(reservationRepository.findOne(2L)).thenReturn(this.reservation2);
        when(reservationRepository.findOne(3L)).thenReturn(this.reservation3);
        when(reservationRepository.findOne(4L)).thenReturn(this.reservation4);
        when(reservationRepository.findOne(5L)).thenReturn(this.reservation5);
        when(reservationRepository.findOne(6L)).thenReturn(null);

        Reservation reservation = reservationService.findById(1L);
        assertEquals(1L, reservation.getId().longValue());

        reservation = reservationService.findById(2L);
        assertEquals(2L, reservation.getId().longValue());

        reservation = reservationService.findById(3L);
        assertEquals(3L, reservation.getId().longValue());

        reservation = reservationService.findById(4L);
        assertEquals(4L, reservation.getId().longValue());

        reservation = reservationService.findById(5L);
        assertEquals(5L, reservation.getId().longValue());

        reservation = reservationService.findById(6L);
        assertNull(reservation);
    }

    @Test
    public void testFindByStatus() {
        List<Reservation> reservationListStatusNew = new ArrayList<>();
        reservationListStatusNew.add(reservation1);
        reservationListStatusNew.add(reservation2);
        reservationListStatusNew.add(reservation3);
        reservationListStatusNew.add(reservation5);
        when(reservationRepository.findByStatus(Status.NEW.getValue())).thenReturn(reservationListStatusNew);

        List<Reservation> reservationListStatusCancelled = new ArrayList<>();
        reservationListStatusCancelled.add(reservation4);
        when(reservationRepository.findByStatus(Status.CANCELLED.getValue())).thenReturn(reservationListStatusCancelled);

        List<Reservation> reservationListStatusExpired = new ArrayList<>();
        when(reservationRepository.findByStatus(Status.EXPIRED.getValue())).thenReturn(reservationListStatusExpired);

        List<Reservation> reservationListStatusRealized = new ArrayList<>();
        when(reservationRepository.findByStatus(Status.REALIZED.getValue())).thenReturn(reservationListStatusRealized);

        List<Reservation> reservationResultList = reservationService.findByStatus(Status.NEW.getValue());
        assertEquals(4, reservationResultList.size());
        assertEquals(1L, reservationResultList.get(0).getId().longValue());
        assertEquals(2L, reservationResultList.get(1).getId().longValue());
        assertEquals(3L, reservationResultList.get(2).getId().longValue());
        assertEquals(5L, reservationResultList.get(3).getId().longValue());

        reservationResultList.clear();
        reservationResultList = reservationService.findByStatus(Status.CANCELLED.getValue());
        assertEquals(1, reservationResultList.size());
        assertEquals(4L, reservationResultList.get(0).getId().longValue());

        reservationResultList.clear();
        reservationResultList = reservationService.findByStatus(Status.EXPIRED.getValue());
        assertEquals(0, reservationResultList.size());

        reservationResultList.clear();
        reservationResultList = reservationService.findByStatus(Status.REALIZED.getValue());
        assertEquals(0, reservationResultList.size());
    }

    @Test
    public void testFindByPesel() {
        List<Reservation> reservationListByPesel = new ArrayList<>();
        reservationListByPesel.add(reservation3);
        reservationListByPesel.add(reservation4);
        reservationListByPesel.add(reservation5);

        when(reservationRepository.findByPesel("75040201624")).thenReturn(reservationListByPesel);

        List<Reservation> reservationResultList = reservationService.findByPesel("75040201624");
        assertEquals(3, reservationResultList.size());

        reservationResultList = reservationService.findByPesel("11111111111");
        assertEquals(0, reservationResultList.size());
    }

    @Test
    public void testFindBySurname() {
        List<Reservation> reservationListBySurname = new ArrayList<>();
        reservationListBySurname.add(reservation3);
        reservationListBySurname.add(reservation4);
        reservationListBySurname.add(reservation5);

        when(reservationRepository.findBySurname("Kurski")).thenReturn(reservationListBySurname);

        List<Reservation> reservationResultList = reservationService.findBySurname("Kurski");
        assertEquals(3, reservationResultList.size());

        reservationResultList = reservationService.findBySurname("ABCDEF");
        assertEquals(0, reservationResultList.size());
    }

    @Test
    public void testFindByCreateDateRange() throws Exception
    {
        List<Reservation> reservationListByDateRange = new ArrayList<>();
        reservationListByDateRange.add(reservation1);
        reservationListByDateRange.add(reservation2);
        reservationListByDateRange.add(reservation4);

        Date createDateFrom = DateUtils.asDate(LocalDate.of(2015, Month.SEPTEMBER, 1));
        Date createDateTo = DateUtils.asDate(LocalDate.of(2015, Month.SEPTEMBER, 16));
        when(reservationRepository.findByCreateDateBetween(createDateFrom, createDateTo)).thenReturn(reservationListByDateRange);

        Future<List<Reservation>> reservationFutureResultList = reservationService.findByCreateDateRange(createDateFrom, createDateTo);
        // Wait until they are all done
        while ( !(reservationFutureResultList.isDone()) ) {
            Thread.sleep(10); //10-millisecond pause between each check
        }
        List<Reservation> reservationResultList = reservationFutureResultList.get();
        assertEquals(3, reservationResultList.size());

        Reservation reservationRes = reservationResultList.get(0);
        assertEquals(1L, reservationRes.getId().longValue());
        assertTrue(reservationRes.getCreateDate().after(createDateFrom));
        assertTrue(reservationRes.getCreateDate().before(createDateTo));

        reservationRes = reservationResultList.get(1);
        assertEquals(2L, reservationRes.getId().longValue());
        assertTrue(reservationRes.getCreateDate().after(createDateFrom));
        assertTrue(reservationRes.getCreateDate().before(createDateTo));

        reservationRes = reservationResultList.get(2);
        assertEquals(4L, reservationRes.getId().longValue());
        assertTrue(reservationRes.getCreateDate().after(createDateFrom));
        assertTrue(reservationRes.getCreateDate().before(createDateTo));
    }

    @Test
    public void testFindByPriceRange()
    {
        List<Reservation> reservationListByPriceRange = new ArrayList<>();
        reservationListByPriceRange.add(reservation2);
        reservationListByPriceRange.add(reservation3);

        BigDecimal priceFrom = new BigDecimal(200);
        BigDecimal priceTo = new BigDecimal(250);
        when(reservationRepository.findByPriceBetween(priceFrom, priceTo)).thenReturn(reservationListByPriceRange);

        List<Reservation> reservationResultList = reservationService.findByPriceRange(priceFrom, priceTo);
        assertEquals(2, reservationResultList.size());

        Reservation reservationRes = reservationResultList.get(0);
        assertEquals(2L, reservationRes.getId().longValue());
        assertEquals(1441710000000L, reservationRes.getCreateDate().getTime());
        assertEquals(new BigDecimal(200), reservationRes.getPrice());

        reservationRes = reservationResultList.get(1);
        assertEquals(3L, reservationRes.getId().longValue());
        assertEquals(1442739600000L, reservationRes.getCreateDate().getTime());
        assertEquals(new BigDecimal(250), reservationRes.getPrice());
    }

    @Test
    public void testFindByExpirationTime()
    {
        List<Reservation> reservationListByExpirationTime = new ArrayList<>();
        reservationListByExpirationTime.add(reservation5);
        when(reservationRepository.findAll()).thenReturn(reservationListByExpirationTime);

        List<Reservation> reservationResultList = reservationService.findByExpirationTime(30);
        assertEquals(1, reservationResultList.size());
        Reservation reservationRes = reservationResultList.get(0);
        assertEquals(5L, reservationRes.getId().longValue());

        reservationResultList = reservationService.findByExpirationTime(10);
        assertEquals(0, reservationResultList.size());

        reservationResultList = reservationService.findByExpirationTime(100);
        assertEquals(1, reservationResultList.size());
        reservationRes = reservationResultList.get(0);
        assertEquals(5L, reservationRes.getId().longValue());
    }
}
