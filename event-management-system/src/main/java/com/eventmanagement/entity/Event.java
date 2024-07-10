package com.eventmanagement.entity;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String description;

	private String venueName;

	private String venueType;

	private String location;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id")
	private Category category;

	private int noOfTickets;

	private int availableTickets;

	private BigDecimal ticketPrice;

	private String addedDate; // current millis time

	private String startDate; // event start date in current millis time

	private String endDate; // event end date in current millis time

	private String image;

	private String status;

}
