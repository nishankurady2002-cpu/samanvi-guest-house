package com.guesthouse.repository;

import com.guesthouse.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomNumberAndStatus(int roomNumber, String status);

    @Query("SELECT b FROM Booking b WHERE b.roomNumber = :roomNumber AND b.status = 'Booked' AND " +
           "((b.checkIn < :checkOut) AND (b.checkOut > :checkIn))")
    List<Booking> findConflictingBookings(
        @Param("roomNumber") int roomNumber,
        @Param("checkIn") String checkIn,
        @Param("checkOut") String checkOut
    );
}