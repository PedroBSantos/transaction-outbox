package br.com.pedro.web.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.pedro.core.domain.Event;
import br.com.pedro.core.domain.EventRepository;

public interface EventJpaRepository extends JpaRepository<Event, Long>, EventRepository {

    @Override
    default void create(Event event) {
        save(event);
    }

    @Query("SELECT e FROM Event e WHERE e.type = :type")
    List<Event> findByType(String type);

    @Override
    default List<Event> readAllByType(String type) {
        return findByType(type);
    }

    default void remove(Event event) {
        delete(event);
    }
}
