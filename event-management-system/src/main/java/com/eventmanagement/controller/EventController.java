package com.eventmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventmanagement.dto.AddEventRequestDto;
import com.eventmanagement.dto.CommonApiResponse;
import com.eventmanagement.dto.EventResponseDto;
import com.eventmanagement.resource.EventResource;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/event")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

	@Autowired
	private EventResource eventResource;

	@PostMapping("add")
	public ResponseEntity<CommonApiResponse> addEvent(AddEventRequestDto request) {
		return this.eventResource.addEvent(request);
	}

	@PutMapping("update")
	public ResponseEntity<CommonApiResponse> updateEvent(AddEventRequestDto request) {
		return this.eventResource.updateEvent(request);
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<CommonApiResponse> deleteEvent(@RequestParam("eventId") Integer eventId) {
		return this.eventResource.deleteEvent(eventId);
	}

	@GetMapping("fetch/all/active")
	public ResponseEntity<EventResponseDto> fetchActiveEvents() {
		return this.eventResource.fetchActiveEvents();
	}

	// for admin side
	@GetMapping("fetch/all")
	public ResponseEntity<EventResponseDto> fetchAllEvents(@RequestParam("status") String status) {
		return this.eventResource.fetchAllEventsByStatus(status);
	}

	@GetMapping("fetch/category-wise")
	public ResponseEntity<EventResponseDto> fetchActiveEventsByCategory(
			@RequestParam("categoryId") Integer categoryId) {
		return this.eventResource.fetchActiveEventsByCategory(categoryId);
	}

	@GetMapping("fetch/name-wise")
	public ResponseEntity<EventResponseDto> searchActiveEventsByName(@RequestParam("eventName") String eventName) {
		return this.eventResource.searchActiveEventsByName(eventName);
	}
	
	@GetMapping("fetch")
	public ResponseEntity<EventResponseDto> fetchEventByEventId(@RequestParam("eventId") Integer eventId) {
		return this.eventResource.fetchEventByEventId(eventId);
	}
	
	@GetMapping(value = "/{eventImageName}", produces = "image/*")
	public void fetchProductImage(@PathVariable("eventImageName") String productImageName, HttpServletResponse resp) {
		this.eventResource.fetchEventImage(productImageName, resp);
	}

}
