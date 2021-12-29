package com.tomboja.springdatajpa;

import com.tomboja.springdatajpa.repository.FlightRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @ProjectName: spring-data-JPA
 * @Author: tdessalegn
 * @Date: 12/28/21
 */

@Transactional
@SpringBootTest
public class DerivedQueryTests {
    @Autowired
    private FlightRepository flightRepository;

    @Before("")
    public void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    public void shouldFindFlightsFromLondon() {
        final Flight flight = createFlight("London");
        final Flight flight2 = createFlight("London");
        final Flight flight3 = createFlight("Amsterdam");
        flightRepository.save(flight);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        List<Flight> flightsFromLondon = flightRepository.findByOrigin("London");

        assertThat(flightsFromLondon)
                .hasSize(2)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(flight);
        assertThat(flightsFromLondon.get(1))
                .usingRecursiveComparison()
                .isEqualTo(flight2);
    }

    @Test
    public void shouldFindFlightFromLondonToParis() {
        final Flight flight1 = createFlightFromLondonToParis("London", "Paris");
        final Flight flight2 = createFlightFromLondonToParis("London", "Paris");
        final Flight flight3 = createFlightFromLondonToParis("Amsterdam", "Helsinki");
        final Flight flight5 = createFlightFromLondonToParis("London", "Rome");
        final Flight flight4 = createFlightFromLondonToParis("London", "Paris");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
        flightRepository.save(flight4);
        flightRepository.save(flight5);

        List<Flight> flightsFromLondonToParis = flightRepository
                .findFlightByOriginAndDestination("London", "Paris");
        assertThat(flightsFromLondonToParis)
                .hasSize(3);
        assertThat(flightsFromLondonToParis)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(flight1);
        assertThat(flightsFromLondonToParis.get(1))
                .usingRecursiveComparison()
                .isEqualTo(flight2);

        assertThat(flightsFromLondonToParis.get(2))
                .usingRecursiveComparison()
                .isEqualTo(flight4);
    }

    @Test
    public void shouldFindFlightsFromLondonIgnoringCase() {
        final Flight flight1 = createFlightIgnoreCase("London");
        final Flight flight2 = createFlightIgnoreCase("london");
        final Flight flight3 = createFlightIgnoreCase("LONDON");
        final Flight flight4 = createFlightIgnoreCase("Helsinki");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
        flightRepository.save(flight4);

        assertThat(flightRepository.findByOriginIgnoreCase("London"))
                .hasSize(3);

        assertThat(flightRepository.findByOriginIgnoreCase("London"))
                .hasSize(3)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(flight1);

        assertThat(flightRepository.findByOriginIgnoreCase("London").get(1))
                .usingRecursiveComparison()
                .isEqualTo(flight2);

        assertThat(flightRepository.findByOriginIgnoreCase("London").get(2))
                .usingRecursiveComparison()
                .isEqualTo(flight3);
    }

    private Flight createFlightIgnoreCase(String origin) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination("Oslo");
        flight.setScheduledAt(LocalDateTime.parse("2021-12-28T12:12:00"));

        return flight;
    }

    @Test
    public void shouldFindFlightFromLondonOrMadrid() {
        final Flight flight1 = createFlightFromLondonOrMadrid("London", "Madrid");
        final Flight flight2 = createFlightFromLondonOrMadrid("Madrid", "London");
        final Flight flight3 = createFlightFromLondonOrMadrid("Fairfield", "Helsinki");
        final Flight flight4 = createFlightFromLondonOrMadrid("Chicago", "London");
        final Flight flight5 = createFlightFromLondonOrMadrid("Madrid", "London");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);
        flightRepository.save(flight4);
        flightRepository.save(flight5);

        ArrayList<String> origins = new ArrayList<>();
        origins.add("London");
        origins.add("Madrid");
        assertThat(flightRepository.findByOriginIn(origins))
                .hasSize(3);

        assertThat(flightRepository.findByOriginIn(origins))
                .first()
                .usingRecursiveComparison()
                .isEqualTo(flight1);

        assertThat(flightRepository.findByOriginIn(origins).get(1))
                .usingRecursiveComparison()
                .isEqualTo(flight2);

        assertThat(flightRepository.findByOriginIn(origins).get(2))
                .usingRecursiveComparison()
                .isEqualTo(flight5);

    }

    private Flight createFlightFromLondonOrMadrid(String origin, String destination) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setScheduledAt(LocalDateTime.parse("2020-10-13T20:20:00"));
        return flight;
    }

    private Flight createFlightFromLondonToParis(String origin, String destination) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setScheduledAt(LocalDateTime.parse("2021-12-12T20:20:00"));

        return flight;
    }

    private Flight createFlight(String origin) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination("Helsinki");
        flight.setScheduledAt(LocalDateTime.parse("2021-12-28T12:12:00"));
        return flight;
    }
}
