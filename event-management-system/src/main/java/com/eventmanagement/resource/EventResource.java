package com.eventmanagement.resource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import com.eventmanagement.dto.AddEventRequestDto;
import com.eventmanagement.dto.CommonApiResponse;
import com.eventmanagement.dto.EventResponseDto;
import com.eventmanagement.entity.Category;
import com.eventmanagement.entity.Event;
import com.eventmanagement.exception.EventSaveFailedException;
import com.eventmanagement.service.CategoryService;
import com.eventmanagement.service.EventService;
import com.eventmanagement.service.StorageService;
import com.eventmanagement.service.UserService;
import com.eventmanagement.utility.Constants.ActiveStatus;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EventResource {

	private final Logger LOG = LoggerFactory.getLogger(EventResource.class);

	@Autowired
	private EventService eventService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	public ResponseEntity<CommonApiResponse> addEvent(AddEventRequestDto request) {

		LOG.info("request received for Event add");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null || request.getCategoryId() == 0) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getName() == null || request.getDescription() == null || request.getLocation() == null
				|| request.getNoOfTickets() <= 0 || request.getVenueName() == null || request.getVenueType() == null
				|| request.getStartDate() == null || request.getEndDate() == null) {
			response.setResponseMessage("bad request - missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		String addedDateTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		Long addedTime = Long.valueOf(addedDateTime);

		Long eventStartTime = Long.valueOf(request.getStartDate());

		if (eventStartTime < addedTime) {
			response.setResponseMessage("Event Start Time should be Future Date!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Event event = AddEventRequestDto.toEntity(request);

		Category category = this.categoryService.getCategoryById(request.getCategoryId());

		if (category == null) {
			response.setResponseMessage("Event Category not found");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// store event image in Image Folder and give event name to store in database
		String eventImageName = storageService.store(request.getImage());

		event.setAvailableTickets(request.getNoOfTickets());
		event.setImage(eventImageName);
		event.setCategory(category);
		event.setStatus(ActiveStatus.ACTIVE.value());
		event.setAddedDate(addedDateTime);

		Event savedEvent = this.eventService.addEvent(event);

		if (savedEvent == null) {
			throw new EventSaveFailedException("Failed to save the Event");
		}

		response.setResponseMessage("Event added successful");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<CommonApiResponse> updateEvent(AddEventRequestDto request) {

		LOG.info("request received for Event upate");

		CommonApiResponse response = new CommonApiResponse();

		if (request == null || request.getCategoryId() == 0) {
			response.setResponseMessage("missing input");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getName() == null || request.getDescription() == null || request.getLocation() == null
				|| request.getNoOfTickets() <= 0 || request.getVenueName() == null || request.getVenueType() == null
				|| request.getStartDate() == null || request.getEndDate() == null) {
			response.setResponseMessage("bad request - missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Event dbEvent = this.eventService.getEventById(request.getId());

		if (dbEvent == null) {
			response.setResponseMessage("Event not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Event event = AddEventRequestDto.toEntity(request);
		event.setId(dbEvent.getId());
		event.setCategory(dbEvent.getCategory());
		event.setStartDate(dbEvent.getStartDate());
		event.setEndDate(dbEvent.getEndDate());
		event.setAddedDate(dbEvent.getAddedDate());
		event.setAvailableTickets(request.getAvailableTickets());

		String currentTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		Long updatedTime = Long.valueOf(currentTime);

		if (request.getStartDate() != null && request.getStartDate() != "" && !request.getStartDate().equals("NaN")) {
			Long eventStartTime = Long.valueOf(request.getStartDate());

			if (eventStartTime < updatedTime) {
				response.setResponseMessage("Event Start Time should be Future Date!!!");
				response.setSuccess(false);

				return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
			}
			event.setStartDate(request.getStartDate());
		}
		
		if (request.getEndDate() != null && request.getEndDate() != "" && !request.getEndDate().equals("NaN")) {
			event.setEndDate(request.getEndDate());
		}

		// store event image in Image Folder and give event name to store in database
		String eventImageName = storageService.store(request.getImage());

		event.setImage(eventImageName);

		// it will update the event category if changed
		if (event.getCategory().getId() != request.getCategoryId()) {
			Category category = this.categoryService.getCategoryById(request.getCategoryId());
			event.setCategory(category);
		}

		event.setStatus(ActiveStatus.ACTIVE.value());

		Event updatedEvent = this.eventService.updateEvent(event);

		if (updatedEvent == null) {
			throw new EventSaveFailedException("Failed to update the Event");
		}

		response.setResponseMessage("Event updated successful");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<EventResponseDto> fetchActiveEvents() {

		String currentTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		EventResponseDto response = new EventResponseDto();

		List<Event> events = this.eventService.getEventByStatusAndStartDateGreaterThan(ActiveStatus.ACTIVE.value(),
				currentTime);

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> fetchAllEventsByStatus(String status) {

		EventResponseDto response = new EventResponseDto();

		if (status == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Event> events = this.eventService.getEventsByStatus(status);

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> fetchEventByEventId(Integer eventId) {

		EventResponseDto response = new EventResponseDto();

		if (eventId == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Event event = this.eventService.getEventById(eventId);

		if (event == null) {
			response.setResponseMessage("event not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		response.setEvents(Arrays.asList(event));
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> fetchActiveEventsByCategory(Integer categoryId) {

		EventResponseDto response = new EventResponseDto();

		if (categoryId == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Category category = this.categoryService.getCategoryById(categoryId);

		if (category == null) {
			response.setResponseMessage("category not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String currentTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<Event> events = this.eventService
				.getEventByStatusAndCategoryAndStartDateGreaterThan(ActiveStatus.ACTIVE.value(), category, currentTime);

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<EventResponseDto> searchActiveEventsByName(String eventName) {

		EventResponseDto response = new EventResponseDto();

		if (eventName == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		String currentTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<Event> events = this.eventService.getEventByStatusAndNameContainingIgnoreCaseAndStartDateGreaterThan(
				ActiveStatus.ACTIVE.value(), eventName, currentTime);

		if (CollectionUtils.isEmpty(events)) {
			response.setResponseMessage("Events not found");
			response.setSuccess(false);

			return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
		}

		response.setEvents(events);
		response.setResponseMessage("Events fetched successful!!");
		response.setSuccess(true);

		return new ResponseEntity<EventResponseDto>(response, HttpStatus.OK);
	}

	public void fetchEventImage(String eventImageName, HttpServletResponse resp) {
		Resource resource = storageService.load(eventImageName);
		if (resource != null) {
			try (InputStream in = resource.getInputStream()) {
				ServletOutputStream out = resp.getOutputStream();
				FileCopyUtils.copy(in, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ResponseEntity<CommonApiResponse> deleteEvent(Integer eventId) {

		CommonApiResponse response = new CommonApiResponse();

		if (eventId == null) {
			response.setResponseMessage("missing input!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Event event = this.eventService.getEventById(eventId);

		if (event == null) {
			response.setResponseMessage("event not found!!!");
			response.setSuccess(false);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		event.setStatus(ActiveStatus.DEACTIVATED.value());
		this.eventService.updateEvent(event);

		response.setResponseMessage("Events Deleted successful!!");
		response.setSuccess(true);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

}
