package com.eventmanagement.resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.eventmanagement.dto.BookingRequestDto;
import com.eventmanagement.dto.BookingResponseDto;
import com.eventmanagement.dto.CommonApiResponse;
import com.eventmanagement.entity.Booking;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Payment;
import com.eventmanagement.entity.User;
import com.eventmanagement.service.BookingService;
import com.eventmanagement.service.EventService;
import com.eventmanagement.service.PaymentService;
import com.eventmanagement.service.UserService;
import com.eventmanagement.utility.Constants.BookingStatus;
import com.eventmanagement.utility.Helper;

import jakarta.transaction.Transactional;

@Component
public class BookingResource {

	private final Logger LOG = LoggerFactory.getLogger(EventResource.class);

	@Autowired
	private BookingService bookingService;

	@Autowired
	private UserService userService;

	@Autowired
	private EventService eventService;

	@Autowired
	private PaymentService paymentService;

    @Transactional
	public ResponseEntity<CommonApiResponse> addBooking(BookingRequestDto request) {

		LOG.info("request received for adding customer booking");

		CommonApiResponse response = new CommonApiResponse();

		String bookingTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		if (request == null) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getEventId() == 0 || request.getCustomerId() == 0 || request.getNoOfTickets() == 0
				|| request.getCvv() == null || request.getExpiryDate() == null || request.getNameOnCard() == null
				|| request.getCardNo() == null) {

			response.setResponseMessage("missing input or invalid details");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);

		}

		User customer = this.userService.getUserById(request.getCustomerId());

		if (customer == null) {
			response.setResponseMessage("customer not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Event event = this.eventService.getEventById(request.getEventId());

		if (event == null) {
			response.setResponseMessage("event not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		int eventAvailableTickets = event.getAvailableTickets();

		int noOfTicketsToBook = request.getNoOfTickets();

		if (noOfTicketsToBook > eventAvailableTickets) {
			response.setResponseMessage("Only " + eventAvailableTickets + " left for the Event!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BigDecimal eventTicketPrice = event.getTicketPrice();

		BigDecimal totalAmountToPay = eventTicketPrice.multiply(new BigDecimal(noOfTicketsToBook));

		String bookingId = Helper.generateEventBookingId();
		String paymentBookingId = Helper.generateBookingPaymentId();

		Payment payment = new Payment();
		payment.setCardNo(request.getCardNo());
		payment.setBookingId(bookingId);
		payment.setAmount(totalAmountToPay);
		payment.setCustomer(customer);
		payment.setCvv(request.getCvv());
		payment.setExpiryDate(request.getExpiryDate());
		payment.setNameOnCard(request.getNameOnCard());
		payment.setPaymentId(paymentBookingId);

		Payment savedPayment = this.paymentService.addPayment(payment);

		if (savedPayment == null) {
			response.setResponseMessage("Failed to Book Event, Payment Failure!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Booking booking = new Booking();
		booking.setBookingId(bookingId);
		booking.setPayment(savedPayment);
		booking.setAmount(totalAmountToPay);
		booking.setBookingTime(bookingTime);
		booking.setCustomer(customer);
		booking.setEvent(event);
		booking.setNoOfTickets(noOfTicketsToBook);
		booking.setStatus(BookingStatus.CONFIRMED.value());

		Booking savedBooking = this.bookingService.addBooking(booking);

		if (savedBooking == null) {
			response.setResponseMessage("Failed to Book Event, Internal Error");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		event.setAvailableTickets(event.getAvailableTickets() - noOfTicketsToBook);
		this.eventService.updateEvent(event);
		
		response.setResponseMessage("Your Booking is Confirmed, Booking ID: " + bookingId);
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<BookingResponseDto> fetchAllBookings() {

		BookingResponseDto response = new BookingResponseDto();

		List<Booking> bookings = this.bookingService.getAllBookings();

		if (CollectionUtils.isEmpty(bookings)) {
			response.setResponseMessage("Bookings not found");
			response.setSuccess(false);

			return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);
		}

		response.setBookings(bookings);
		response.setResponseMessage("Booking fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<BookingResponseDto> fetchAllBookingsByEvent(Integer eventId) {

		BookingResponseDto response = new BookingResponseDto();

		if (eventId == null) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<BookingResponseDto>(response, HttpStatus.BAD_REQUEST);

		}

		Event event = this.eventService.getEventById(eventId);

		if (event == null) {
			response.setResponseMessage("event not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<BookingResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Booking> bookings = this.bookingService.getBookingByEvent(event);

		if (CollectionUtils.isEmpty(bookings)) {
			response.setResponseMessage("Bookings not found");
			response.setSuccess(false);

			return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);
		}

		response.setBookings(bookings);
		response.setResponseMessage("Booking fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<BookingResponseDto> fetchAllBookingsByCustomer(Integer customerId) {

		BookingResponseDto response = new BookingResponseDto();

		if (customerId == null) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<BookingResponseDto>(response, HttpStatus.BAD_REQUEST);

		}

		User customer = this.userService.getUserById(customerId);

		if (customer == null) {
			response.setResponseMessage("customer not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<BookingResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Booking> bookings = this.bookingService.getBookingByCustomer(customer);

		if (CollectionUtils.isEmpty(bookings)) {
			response.setResponseMessage("Bookings not found");
			response.setSuccess(false);

			return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);
		}

		response.setBookings(bookings);
		response.setResponseMessage("Booking fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<BookingResponseDto>(response, HttpStatus.OK);
	}

}
