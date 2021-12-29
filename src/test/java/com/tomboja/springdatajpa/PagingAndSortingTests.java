package com.tomboja.springdatajpa;

import com.tomboja.springdatajpa.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @ProjectName: spring-data-JPA
 * @Author: tdessalegn
 * @Date: 12/28/21
 */

@SpringBootTest
@Transactional
public class PagingAndSortingTests {
    @Autowired
    private FlightRepository flightRepository;

    @BeforeEach
    public void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    public void shouldSortByDestination() {
        final Flight flight1 = createFlight("Madrid", "Amsterdam", "2020-10-12T20:20:00");
        final Flight flight2 = createFlight("London", "Oslo", "2020-12-12T20:00:00");
        final Flight flight3 = createFlight("Helsinki", "New York", "2021-10-10T00:20:00");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        final List<Flight> orderedByOrigin = flightRepository.findAll(Sort.by("origin"));
        final List<Flight> orderedByDestination = flightRepository.findAll(Sort.by("destination"));
        final List<Flight> orderedByScheduledDate = flightRepository.findAll(Sort.by("scheduledAt"));

        // Sorted By flight origin
        assertThat(orderedByOrigin.get(0)).usingRecursiveComparison().isEqualTo(flight3);
        assertThat(orderedByOrigin.get(1)).usingRecursiveComparison().isEqualTo(flight2);
        assertThat(orderedByOrigin.get(2)).usingRecursiveComparison().isEqualTo(flight1);

        // Sorted By flight destination
        assertThat(orderedByDestination.get(0)).usingRecursiveComparison().isEqualTo(flight1);
        assertThat(orderedByDestination.get(1)).usingRecursiveComparison().isEqualTo(flight3);
        assertThat(orderedByDestination.get(2)).usingRecursiveComparison().isEqualTo(flight2);

        // Sorted By flight scheduled date
        assertThat(orderedByScheduledDate.get(0)).usingRecursiveComparison().isEqualTo(flight1);
        assertThat(orderedByScheduledDate.get(1)).usingRecursiveComparison().isEqualTo(flight2);
        assertThat(orderedByScheduledDate.get(2)).usingRecursiveComparison().isEqualTo(flight3);
    }

    @Test
    public void shouldSortFlightsByScheduleThenByName() {
        final LocalDateTime now = LocalDateTime.now();
        final Flight flight1 = createFlight2("Amsterdam", now);
        final Flight flight2 = createFlight2("Oslo", now.plusHours(2));
        final Flight flight3 = createFlight2("New York", now.minusHours(3));
        final Flight flight4 = createFlight2("Helsinki", now.plusHours(6));
        final Flight flight5 = createFlight2("Helsinki", now.minusHours(5));
        final Flight flight6 = createFlight2("Helsinki", now.minusHours(1));

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
        flightRepository.save(flight4);
        flightRepository.save(flight5);
        flightRepository.save(flight6);

        final List<Flight> orderedByDestinationThenScheduledAt =
                flightRepository.findAll(Sort.by("destination", "scheduledAt"));
        assertThat(orderedByDestinationThenScheduledAt.get(0))
                .usingRecursiveComparison().isEqualTo(flight1);
        assertThat(orderedByDestinationThenScheduledAt.get(1))
                .usingRecursiveComparison().isEqualTo(flight5);
        assertThat(orderedByDestinationThenScheduledAt.get(2))
                .usingRecursiveComparison().isEqualTo(flight6);
        assertThat(orderedByDestinationThenScheduledAt.get(3))
                .usingRecursiveComparison().isEqualTo(flight4);
        assertThat(orderedByDestinationThenScheduledAt.get(4))
                .usingRecursiveComparison().isEqualTo(flight3);
        assertThat(orderedByDestinationThenScheduledAt.get(5))
                .usingRecursiveComparison().isEqualTo(flight2);
    }

    @Test
    public void shouldPageResults() {
        for (int i = 0; i < 50; i++) {
            String destination = "Flight - " + i;
            flightRepository.save(createFlight2(destination, LocalDateTime.now()));
        }

        final Page<Flight> page = flightRepository.findAll(PageRequest.of(2, 5));
        assertThat(page.getTotalElements()).isEqualTo(50);
        assertThat(page.getTotalPages()).isEqualTo(10);
        assertThat(page.getNumberOfElements()).isEqualTo(5);

        final List<String> page2Flights = new ArrayList<>();
        for (Flight flight : page.getContent()) {
            String destination = flight.getDestination();
            page2Flights.add(destination);
        }
        assertThat(page2Flights.get(0)).isEqualTo("Flight - 10");
        assertThat(page2Flights.get(1)).isEqualTo("Flight - 11");
        assertThat(page2Flights.get(2)).isEqualTo("Flight - 12");
        assertThat(page2Flights.get(3)).isEqualTo("Flight - 13");
        assertThat(page2Flights.get(4)).isEqualTo("Flight - 14");
    }

    @Test
    public void shouldPageAndSortResults() {
        for (int i = 0; i < 50; i++) {
            String destination = "Flight - " + i;
            flightRepository.save(createFlight2(destination, LocalDateTime.now()));
        }

        final Page<Flight> page = flightRepository
                .findAll(PageRequest.of(2, 5, Sort.by(DESC, "destination")));
        assertThat(page.getTotalElements()).isEqualTo(50);
        assertThat(page.getTotalPages()).isEqualTo(10);
        assertThat(page.getNumberOfElements()).isEqualTo(5);

        final List<String> page2Flights = new ArrayList<>();
        for (Flight flight : page.getContent()) {
            String destination = flight.getDestination();
            page2Flights.add(destination);
        }
        assertThat(page2Flights.get(0)).isEqualTo("Flight - 44");
        assertThat(page2Flights.get(1)).isEqualTo("Flight - 43");
        assertThat(page2Flights.get(2)).isEqualTo("Flight - 42");
        assertThat(page2Flights.get(3)).isEqualTo("Flight - 41");
        assertThat(page2Flights.get(4)).isEqualTo("Flight - 40");
    }

    @Test
    public void shouldPageAndSortWithDerivedQuery() {
        for (int i = 0; i < 10; i++) {
            // 10 flights from Paris
            String destination = "Flight - " + i;
            final Flight flight = createFlight2(destination, LocalDateTime.now());
            flight.setOrigin("Paris");
            flightRepository.save(flight);
        }

        for (int i = 10; i < 20; i++) {
            // 10 flights from London
            String destination = "Flight - " + i;
            flightRepository.save(createFlight2(destination, LocalDateTime.now()));
        }

        final Page<Flight> page = flightRepository
                .findByOrigin("London", PageRequest.of(0, 5, Sort.by(DESC, "destination")));
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumberOfElements()).isEqualTo(5);

        final List<String> page2Flights = new ArrayList<>();
        for (Flight flight : page.getContent()) {
            String destination = flight.getDestination();
            page2Flights.add(destination);
        }
        assertThat(page2Flights.get(0)).isEqualTo("Flight - 19");
        assertThat(page2Flights.get(1)).isEqualTo("Flight - 18");
        assertThat(page2Flights.get(2)).isEqualTo("Flight - 17");
        assertThat(page2Flights.get(3)).isEqualTo("Flight - 16");
        assertThat(page2Flights.get(4)).isEqualTo("Flight - 15");
    }

    private Flight createFlight2(String destination, LocalDateTime time) {
        final Flight flight = new Flight();
        flight.setOrigin("London");
        flight.setDestination(destination);
        flight.setScheduledAt(time);
        return flight;
    }

    private Flight createFlight(String origin, String destination, String localDateTime) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setScheduledAt(LocalDateTime.parse(localDateTime));
        return flight;
    }
}
