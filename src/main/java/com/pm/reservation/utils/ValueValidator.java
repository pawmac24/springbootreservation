package com.pm.reservation.utils;

import com.pm.reservation.model.Status;
import com.pm.reservation.rest.exception.ArgumentValidationException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Created by pmackiewicz on 2015-10-12.
 */
public class ValueValidator {

    public final static Logger logger = Logger.getLogger(ValueValidator.class);

    public static final String DATES_ARE_NOT_VALID = "Dates are not valid";
    public static final String PRICE_IS_NOT_VALID = "Price is not valid";
    public static final String STATUS_IS_NOT_VALID = "Status is not valid";

    public static void validateISOLocalDate(String isoLocalDate) throws ArgumentValidationException {
        try {
            LocalDate.parse(isoLocalDate);
        } catch (DateTimeParseException ex) {
            logger.error(DATES_ARE_NOT_VALID, ex);
            throw new ArgumentValidationException(DATES_ARE_NOT_VALID);
        }
    }

    public static void validatePrice(String price) throws ArgumentValidationException {
        try {
            new BigDecimal(price);
        } catch (NumberFormatException ex) {
            logger.error(PRICE_IS_NOT_VALID, ex);
            throw new ArgumentValidationException(PRICE_IS_NOT_VALID);
        }
    }

    public static void validateStatus(int status) throws ArgumentValidationException {
        if (!Status.isValid(status)) {
            logger.error(STATUS_IS_NOT_VALID);
            throw new ArgumentValidationException(STATUS_IS_NOT_VALID);
        }
    }
}
