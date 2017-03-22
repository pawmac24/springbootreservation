package com.pm.reservation.service;

import com.pm.reservation.model.Reservation;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by pmackiewicz on 2015-09-21.
 */
public interface ReservationService {

    void save(Reservation reservation);

    List<Reservation> findAll();

    Reservation findById(Long id);

    List<Reservation> findByStatus(int status);

    List<Reservation> findByPesel(String pesel);

    List<Reservation> findBySurname(String surname);

    Future<List<Reservation>> findByCreateDateRange(Date createDateFrom, Date createDateTo) throws InterruptedException;

//    void receiveResults(List<Reservation> reservationList);

    List<Reservation> findByPriceRange(BigDecimal pricefrom, BigDecimal priceTo);

    List<Reservation> findByPriceRangeJMS(BigDecimal priceFrom, BigDecimal priceTo);

    DeferredResult<List<Reservation>> findByPriceRangeTask(BigDecimal priceFrom, BigDecimal priceTo);

    void updateStatusAfterExceededTime(int oldStatus, int newStatus, List<Long> idList);

    List<Reservation> findByExpirationTime(long expirationMinutes);

}
