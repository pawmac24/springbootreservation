package com.pm.reservation.repository;

import com.pm.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by pmackiewicz on 2015-09-21.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(int status);

    @Query("select r from Reservation r where r.person.pesel = :pesel")
    List<Reservation> findByPesel(@Param("pesel") String pesel);

    @Query("select r from Reservation r where r.person.surname = :surname")
    List<Reservation> findBySurname(@Param("surname") String surname);

    List<Reservation> findByCreateDateBetween(Date createDateFrom, Date createDateTo);

    List<Reservation> findByPriceBetween(BigDecimal pricefrom, BigDecimal priceTo);

    @Modifying
    @Query("update Reservation r set r.status = :newstatus where r.status = :oldstatus and r.id IN (:idlist)")
    void updateStatusAfterExceededTime(@Param("oldstatus") int oldStatus, @Param("newstatus") int newStatus, @Param("idlist") List<Long> idList);
}
