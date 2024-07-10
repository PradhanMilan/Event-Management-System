package com.eventmanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Category;
import com.eventmanagement.entity.Event;

@Repository
public interface EventDao extends JpaRepository<Event, Integer> {

	List<Event> findByStatusOrderByIdDesc(String status);

	List<Event> findByStatusAndStartDateGreaterThan(String status, String startDate);

	List<Event> findByStatusAndCategoryAndStartDateGreaterThan(String status, Category category, String startDate);

	List<Event> findByStatusAndNameContainingIgnoreCaseAndStartDateGreaterThan(String status, String name,
			String startDate);

	List<Event> findByStatusAndCategory(String status, Category category);

}
