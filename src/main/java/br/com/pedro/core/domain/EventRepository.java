package br.com.pedro.core.domain;

import java.util.List;

public interface EventRepository {

	void create(Event event);

	List<Event> readAllByType(String type);

	void remove(Event event);
}
