package com.guesthouse.service;

import com.guesthouse.model.Booking;
import com.guesthouse.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking bookRoom(Booking booking) {
        // Validate admin password for new bookings
        if (!"samanvi@123".equals(booking.getAdminPassword())) {
            throw new RuntimeException("Invalid admin password. Authorization failed.");
        }

        // Strict overlap validation
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            booking.getRoomNumber(),
            booking.getCheckIn(),
            booking.getCheckOut()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room " + booking.getRoomNumber() + " is already booked during this time window.");
        }

        booking.setStatus("Booked");
        return bookingRepository.save(booking);
    }

    public boolean cancelBooking(Long id, String password) {
        if (!"samanvi@123".equals(password)) {
            return false;
        }

        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus("Cancelled");
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }
}