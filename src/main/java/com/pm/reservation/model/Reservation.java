package com.pm.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by pmackiewicz on 2015-09-17.
 */

@Entity
@Table(name = "reservation")
public class Reservation implements Serializable{

    private static final long serialVersionUID = 4022428607833152571L;
    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @SequenceGenerator(name="reservation_seq", sequenceName="reservation_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="reservation_seq")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private BigDecimal price;

    private Long timeInterval; //in seconds 30 minutes = 30 * 60 seconds = 1800 seconds

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    private String comment;

    private int status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", price=" + price +
                ", timeInterval=" + timeInterval +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
