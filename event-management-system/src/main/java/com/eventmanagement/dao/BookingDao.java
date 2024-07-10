package com.eventmanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Booking;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.User;

@Repository
public interface BookingDao extends JpaRepository<Booking, Integer> {

	List<Booking> findByCustomer(User customer);

	List<Booking> findByEvent(Event event);

	List<Booking> findByBookingIdContainingIgnoreCase(String bookingId);

}
