package com.pm.reservation.service;

import com.pm.reservation.model.ParameterModel;
import com.pm.reservation.model.Reservation;
import com.pm.reservation.model.Status;
import com.pm.reservation.repository.ReservationRepository;
import com.pm.reservation.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by pmackiewicz on 2015-09-21.
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    public final static Logger logger = Logger.getLogger(ReservationServiceImpl.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TaskServicePrice taskServicePrice;

    @Autowired
    private SimpleOutQueueListener queueReservationReader;

    @Autowired
    private JMSProducer jmsProducer;

    public ReservationServiceImpl() {
    }

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        logger.info("===> findById id = " + id);
        return reservationRepository.findOne(id);
    }

    @Override
    public List<Reservation> findByStatus(int status) {
        logger.info("===> findByStatus status = " + status);
        return reservationRepository.findByStatus(status);
    }

    @Override
    public List<Reservation> findByPesel(String pesel) {
        logger.info("===> findByPesel pesel = " + pesel);
        return reservationRepository.findByPesel(pesel);
    }

    @Override
    public List<Reservation> findBySurname(String surname) {
        logger.info("===> findBySurname surname = " + surname);
        return reservationRepository.findBySurname(surname);
    }

    @Override
    @Async
    public Future<List<Reservation>> findByCreateDateRange(Date createDateFrom, Date createDateTo) throws InterruptedException {
        logger.info("===> findByCreateDateRange createDateFrom = " + createDateFrom + " createDateTo = " + createDateTo);
        List<Reservation> reservationList = reservationRepository.findByCreateDateBetween(createDateFrom, createDateTo);
        Thread.sleep(10000L);
        return new AsyncResult<List<Reservation>>(reservationList);
    }

    @Override
    public List<Reservation> findByPriceRange(BigDecimal priceFrom, BigDecimal priceTo) {
        logger.info("===> findByPriceRange priceFrom = " + priceFrom + " priceTo = " + priceTo);
        return reservationRepository.findByPriceBetween(priceFrom, priceTo);
    }

    @Override
    public List<Reservation> findByPriceRangeJMS(BigDecimal priceFrom, BigDecimal priceTo) {
        logger.info("===> findByPriceRangeJMS priceFrom = " + priceFrom + " priceTo = " + priceTo);

        // Send a message
        ParameterModel parameterModel = new ParameterModel();
        parameterModel.setPriceFrom(priceFrom);
        parameterModel.setPriceTo(priceTo);
        jmsProducer.sendMessage(parameterModel);

        while(queueReservationReader.getReservationList().isEmpty())
        {
            logger.info("Waiting");
        }

        return queueReservationReader.getReservationList();
    }

    @Override
    public DeferredResult<List<Reservation>> findByPriceRangeTask(BigDecimal priceFrom, BigDecimal priceTo) {
        logger.info("(1) Request received");
        DeferredResult<List<Reservation>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            logger.info("(2) Before execute");
            return taskServicePrice.execute(priceFrom, priceTo);
        }).whenComplete((result, throwable) -> {
            logger.info("(3) Complete");
            deferredResult.setResult(result);
        });
        //
        logger.info("(4) Servlet thread released");

        return deferredResult;
    }

    @Override
    public void updateStatusAfterExceededTime(int oldStatus, int newStatus, List<Long> idList) {
        logger.info("===> updateStatusAfterExceededTime [" + oldStatus + " -> " + newStatus + "] " + idList);
        reservationRepository.updateStatusAfterExceededTime(oldStatus, newStatus, idList);
    }

    @Override
    public List<Reservation> findByExpirationTime(long expirationMinutes) {
        logger.info("===> findByExpirationTime expirationMinutes = " + expirationMinutes);

        List<Reservation> reservationList = reservationRepository.findAll();
        List<Reservation> filteredReservationList = reservationList.stream()
                .filter(r -> Status.NEW.getValue() == r.getStatus())
                .filter(r -> {
                            LocalDateTime expirationDate = LocalDateTime.now()
                                    .plusMinutes(expirationMinutes)
                                    .minusSeconds(r.getTimeInterval().longValue());
                            LocalDateTime createDate = DateUtils.asLocalDateTime(r.getCreateDate());
                            createDate.isBefore(expirationDate);
                            logger.info("===> " + createDate + " < " + expirationDate);
                            return createDate.isBefore(expirationDate);
                        }
                )
                .collect(Collectors.toList());
        filteredReservationList.forEach(logger::info);
        return filteredReservationList;
    }
}
