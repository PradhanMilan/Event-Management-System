package com.eventmanagement.dto;

import lombok.Data;

@Data
public class BookingRequestDto {

	private int eventId;

	private int customerId;

	private int noOfTickets;
	
	private String cardNo;

	private String nameOnCard;

	private String cvv;

	private String expiryDate;

}
