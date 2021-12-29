package com.tomboja.springdatajpa.repository;

import com.tomboja.springdatajpa.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

/**
 * @ProjectName: spring-data-JPA
 * @Author: tdessalegn
 * @Date: 12/28/21
 */

public interface FlightRepository extends JpaRepository<Flight, Long>, DeleteByOriginRepository {

    List<Flight> findByOrigin(String origin);

    List<Flight> findFlightByOriginAndDestination(String origin, String destination);

    List<Flight> findByOriginIn(Collection<String> origin);

    // Can also be defined as varargs with three dots like below
    // and meaning: zero or many string arguments
    //List<Flight> findByOriginIn(String ... origin);

    List<Flight> findByOriginIgnoreCase(String origin);

    Page<Flight> findByOrigin(String london, Pageable pageable);
}
