package com.eventmanagement.service;

import java.util.List;

import com.eventmanagement.entity.Category;
import com.eventmanagement.entity.Event;

public interface EventService {

	Event addEvent(Event event);

	Event updateEvent(Event event);

	Event getEventById(int eventId);

	List<Event> getEventsByStatus(String status);

	List<Event> getEventByStatusAndStartDateGreaterThan(String status, String startDate);

	List<Event> getEventByStatusAndCategoryAndStartDateGreaterThan(String status, Category category, String startDate);

	List<Event> getEventByStatusAndNameContainingIgnoreCaseAndStartDateGreaterThan(String status, String name,
			String startDate);
	
	List<Event> getEventByStatusAndCategory(String status, Category category);
	
	List<Event> updateEvents(List<Event> events);
	
}
