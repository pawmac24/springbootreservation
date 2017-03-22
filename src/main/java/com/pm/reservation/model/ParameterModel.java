package com.pm.reservation.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by pmackiewicz on 2015-10-13.
 */
public class ParameterModel implements Serializable {

    private static final long serialVersionUID = -8145154295582085302L;

    private BigDecimal priceFrom;
    private BigDecimal priceTo;

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    @Override
    public String toString() {
        return "ParameterModel{" +
                "priceFrom=" + priceFrom +
                ", priceTo=" + priceTo +
                '}';
    }
}
