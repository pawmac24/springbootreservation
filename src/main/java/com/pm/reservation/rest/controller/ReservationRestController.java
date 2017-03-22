package com.pm.reservation.rest.controller;

import com.pm.reservation.model.Reservation;
import com.pm.reservation.rest.exception.ArgumentValidationException;
import com.pm.reservation.rest.exception.ReservationErrorInformation;
import com.pm.reservation.rest.exception.ReservationNotFoundException;
import com.pm.reservation.service.ReservationService;
import com.pm.reservation.service.TaskServicePrice;
import com.pm.reservation.utils.DateUtils;
import com.pm.reservation.utils.ValueValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by pmackiewicz on 2015-09-18.
 */
@RestController
@RequestMapping("reservations")
public class ReservationRestController {

    public final static Logger logger = Logger.getLogger(ReservationRestController.class);

    public static final String RESERVATION_NOT_FOUND = "This reservation is not found in the system";

    @Autowired
    private ReservationService reservationService;

//    @Autowired
//    private TaskServicePrice taskServicePrice;

    public ReservationRestController() {
    }

    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping("/id/{id}")
    public Reservation getReservationById(@PathVariable("id") Long id) throws ReservationNotFoundException {
        logger.info("getReservationById - id = " + id);
        Reservation reservation = reservationService.findById(id);
        if (reservation == null) {
            logger.error(RESERVATION_NOT_FOUND);
            throw new ReservationNotFoundException(RESERVATION_NOT_FOUND);
        }
        return reservation;
    }

    /**
     * Example http://localhost:8080/springbootreservation/reservations/dateISOFrom/2015-09-01/dateISOTo/2015-09-30
     *
     * @param createDateFrom in ISO-8601 format (YYYY-MM-DD)
     * @param createDateTo   in ISO-8601 format (YYYY-MM-DD)
     * @return Reservation List
     */
    @RequestMapping("/dateISOFrom/{dateFrom}/dateISOTo/{dateTo}")
    public List<Reservation> getByReservationDates(@PathVariable("dateFrom") String createDateFrom,
                                                   @PathVariable("dateTo") String createDateTo)
            throws InterruptedException, ExecutionException, ArgumentValidationException {
        ValueValidator.validateISOLocalDate(createDateFrom);
        ValueValidator.validateISOLocalDate(createDateTo);
        Date dateFrom = DateUtils.asDate(LocalDate.parse(createDateFrom));
        Date dateTo = DateUtils.asDate(LocalDate.parse(createDateTo));

        long start = System.currentTimeMillis();
        logger.info("getByReservationDates - start = " + start);

        Future<List<Reservation>> reservationResult = reservationService.findByCreateDateRange(dateFrom, dateTo);

        logger.info("getByReservationDates - elapsed time = " + (System.currentTimeMillis() - start));
        List<Reservation> reservations = reservationResult.get();
        logger.info("getByReservationDates xxx");

        return reservations;
    }

    /**
     * Example http://localhost:8080/springbootreservation/reservations/datefrom/1441058400000/dateto/1443564000000
     * <p>
     * 2015-09-01 <=> 1441058400000
     * 2015-09-30 <=> 1443564000000
     * 2015-09-20 <=> 1442700000000
     *
     * @param createDateFrom
     * @param createDateTo
     * @return Reservation List
     * @throws ArgumentValidationException
     */
    @RequestMapping("/datefrom/{dateFrom}/dateto/{dateTo}")
    public List<Reservation> getByReservationDates(@PathVariable("dateFrom") long createDateFrom,
                                                   @PathVariable("dateTo") long createDateTo) throws Exception {
        Date dateFrom = new Date(createDateFrom);
        Date dateTo = new Date(createDateTo);

        long start = System.currentTimeMillis();
        logger.info("getByReservationDates - start = " + start);

        Future<List<Reservation>> reservationResult = reservationService.findByCreateDateRange(dateFrom, dateTo);

        logger.info("getByReservationDates - elapsed time = " + (System.currentTimeMillis() - start));
        List<Reservation> reservations = reservationResult.get();
        logger.info("getByReservationDates yyy");
        return reservations;
    }

    @RequestMapping("/pricefrom/{pricefrom}/priceto/{priceto}")
    public List<Reservation> getByPriceRange(@PathVariable("pricefrom") String priceFromArg,
                                             @PathVariable("priceto") String priceToArg) throws ArgumentValidationException {
        ValueValidator.validatePrice(priceFromArg);
        ValueValidator.validatePrice(priceToArg);
        BigDecimal priceFrom = new BigDecimal(priceFromArg);
        BigDecimal priceTo = new BigDecimal(priceToArg);

        List<Reservation> reservations = reservationService.findByPriceRange(priceFrom, priceTo);
        return reservations;
    }

    @RequestMapping("/jms/pricefrom/{pricefrom}/priceto/{priceto}")
    public List<Reservation> getByPriceRangeJMS(@PathVariable("pricefrom") String priceFromArg,
                                             @PathVariable("priceto") String priceToArg) throws ArgumentValidationException {
        ValueValidator.validatePrice(priceFromArg);
        ValueValidator.validatePrice(priceToArg);
        BigDecimal priceFrom = new BigDecimal(priceFromArg);
        BigDecimal priceTo = new BigDecimal(priceToArg);

        List<Reservation> reservations = reservationService.findByPriceRangeJMS(priceFrom, priceTo);
        return reservations;
    }

    @RequestMapping("/slow/pricefrom/{pricefrom}/priceto/{priceto}")
    public DeferredResult<List<Reservation>> getByPriceRangeSlow(@PathVariable("pricefrom") String priceFromArg,
                                             @PathVariable("priceto") String priceToArg) throws ArgumentValidationException {
        ValueValidator.validatePrice(priceFromArg);
        ValueValidator.validatePrice(priceToArg);
        BigDecimal priceFrom = new BigDecimal(priceFromArg);
        BigDecimal priceTo = new BigDecimal(priceToArg);

        DeferredResult<List<Reservation>> deferredResult = reservationService.findByPriceRangeTask(priceFrom, priceTo);
        return deferredResult;
    }

    @RequestMapping("/surname/{surname}")
    public List<Reservation> getReservationBySurname(@PathVariable("surname") String surname) throws ReservationNotFoundException {
        List<Reservation> reservations = reservationService.findBySurname(surname);
        return reservations;
    }

    @RequestMapping("/pesel/{pesel}")
    public List<Reservation> getReservationByPesel(@PathVariable("pesel") String pesel) throws ReservationNotFoundException {
        List<Reservation> reservations = reservationService.findByPesel(pesel);
        return reservations;
    }

    @RequestMapping("/status/{status}")
    public List<Reservation> getReservationByStatus(@PathVariable("status") int status) throws ArgumentValidationException {

        ValueValidator.validateStatus(status);

        List<Reservation> reservations = reservationService.findByStatus(status);
        return reservations;
    }

    @RequestMapping("/expirationminutes/{minutes}")
    public List<Reservation> getReservationByExpirationTime(@PathVariable("minutes") int minutes) {

        List<Reservation> reservations = reservationService.findByExpirationTime(minutes);
        return reservations;
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ReservationErrorInformation> rulesForReservationNotFound(HttpServletRequest req, Exception e) {
        ReservationErrorInformation error = new ReservationErrorInformation(e.getMessage());
        return new ResponseEntity<ReservationErrorInformation>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ArgumentValidationException.class)
    public ResponseEntity<ReservationErrorInformation> rulesForArgumentNotValid(HttpServletRequest req, Exception e) {
        ReservationErrorInformation error = new ReservationErrorInformation(e.getMessage());
        return new ResponseEntity<ReservationErrorInformation>(error, HttpStatus.BAD_REQUEST);
    }
}
