package com.pm.reservation.schedule;

import com.pm.reservation.model.Reservation;
import com.pm.reservation.model.Status;
import com.pm.reservation.service.ReservationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledReservationTasks {

    public final static Logger logger = Logger.getLogger(ScheduledReservationTasks.class);
    public static final int MILISECONDS_IN_SECOND = 1000;

    @Autowired
    private ReservationService reservationService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(initialDelay = 5000, fixedRate = 10000) //in miliseconds
    public void updateStatusAfterExceededTime() {

        logger.info("===> updateStatusAfterExceededTime - The time is now = " + dateFormat.format(new Date()));

        StringBuilder timeValuesBuilder = new StringBuilder();
        List<Long> filteredReservationIdList = new ArrayList<>();
        long currentNumberOfSeconds = (new Date().getTime()) / MILISECONDS_IN_SECOND;

        List<Reservation> newReservationList = reservationService.findByStatus(Status.NEW.getValue());
        for (Reservation reservation : newReservationList) {
            long createDateInSeconds = reservation.getCreateDate().getTime() / MILISECONDS_IN_SECOND;
            long timeIntervalInSeconds = reservation.getTimeInterval();
            long timeDiffInSeconds = currentNumberOfSeconds - (createDateInSeconds + timeIntervalInSeconds);
             timeValuesBuilder
                    .append("\n[id = " + reservation.getId())
                    .append(" currentNumberOfSeconds = " + currentNumberOfSeconds)
                    .append(" createDateInSeconds = " + createDateInSeconds)
                    .append(" timeIntervalInSeconds = " + timeIntervalInSeconds)
                    .append(" timeDiffInSeconds = " + timeDiffInSeconds + "]");
            if (timeDiffInSeconds >= 0) {
                filteredReservationIdList.add(reservation.getId());
            }
        }
        logger.debug(timeValuesBuilder.toString());

        if (!filteredReservationIdList.isEmpty()) {
            logger.info("=== Before update: ===");
//            StringBuilder reservationBuilder = new StringBuilder();
//            for (Reservation reservation : reservationService.findAll()) {
//                reservationBuilder.append("\n" + reservation);
//            }
//            logger.debug(reservationBuilder);

            //update
            reservationService.updateStatusAfterExceededTime(
                    Status.NEW.getValue(), Status.EXPIRED.getValue(),
                    filteredReservationIdList);

            logger.info("=== After update: ===");
//            StringBuilder reservationBuilderAfter = new StringBuilder();
//            for (Reservation reservation : reservationService.findAll()) {
//                reservationBuilderAfter.append("\n" + reservation);
//            }
//            logger.debug(reservationBuilderAfter);
        }
        else {
            logger.info("=== Nothing was changed: ===");
        }
    }
}
