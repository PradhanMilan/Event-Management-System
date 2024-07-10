package com.eventmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventmanagement.dao.EventDao;
import com.eventmanagement.entity.Category;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.User;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;

	@Override
	public Event addEvent(Event event) {
		// TODO Auto-generated method stub
		return eventDao.save(event);
	}

	@Override
	public Event updateEvent(Event event) {
		// TODO Auto-generated method stub
		return eventDao.save(event);
	}

	@Override
	public Event getEventById(int eventId) {

		Optional<Event> optional = this.eventDao.findById(eventId);

		if (optional.isEmpty()) {
			return null;
		}
		return optional.get();

	}

	@Override
	public List<Event> getEventsByStatus(String status) {
		return this.eventDao.findByStatusOrderByIdDesc(status);
	}

	@Override
	public List<Event> getEventByStatusAndStartDateGreaterThan(String status, String startDate) {
		return this.eventDao.findByStatusAndStartDateGreaterThan(status, startDate);
	}

	@Override
	public List<Event> getEventByStatusAndCategoryAndStartDateGreaterThan(String status, Category category,
			String startDate) {
		// TODO Auto-generated method stub
		return this.eventDao.findByStatusAndCategoryAndStartDateGreaterThan(status, category, startDate);
	}

	@Override
	public List<Event> getEventByStatusAndNameContainingIgnoreCaseAndStartDateGreaterThan(String status, String name,
			String startDate) {
		// TODO Auto-generated method stub
		return this.eventDao.findByStatusAndNameContainingIgnoreCaseAndStartDateGreaterThan(status, name, startDate);
	}

	@Override
	public List<Event> getEventByStatusAndCategory(String status, Category category) {
		// TODO Auto-generated method stub
		return this.eventDao.findByStatusAndCategory(status, category);
	}

	@Override
	public List<Event> updateEvents(List<Event> events) {
		// TODO Auto-generated method stub
		return this.eventDao.saveAll(events);
	}

}
