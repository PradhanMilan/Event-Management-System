package com.eventmanagement.service;

import java.util.List;

import com.eventmanagement.entity.Booking;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.User;

public interface BookingService {

	Booking addBooking(Booking booking);

	Booking updateBooking(Booking booking);

	Booking getBookingById(int bookingId);

	List<Booking> getAllBookings();

	List<Booking> getBookingByEvent(Event event);

	List<Booking> getBookingByCustomer(User customer);

	List<Booking> getBookingsByBookingId(String bookingId);

}
