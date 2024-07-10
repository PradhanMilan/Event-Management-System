package com.eventmanagement.dto;

import java.util.ArrayList;
import java.util.List;

import com.eventmanagement.entity.Event;

import lombok.Data;

@Data
public class EventResponseDto extends CommonApiResponse {

	private List<Event> events = new ArrayList();

}
