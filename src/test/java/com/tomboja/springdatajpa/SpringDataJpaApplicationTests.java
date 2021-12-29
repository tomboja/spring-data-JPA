package com.tomboja.springdatajpa;

import com.tomboja.springdatajpa.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SpringDataJpaApplicationTests {

    /* BY USING FLIGHT CRUD REPOSITORY **/
    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldPerformCrudOperations() {
        final Flight flight = new Flight();
        flight.setOrigin("Amsterdam");
        flight.setDestination("Helsinki");
        flight.setScheduledAt(LocalDateTime.parse("2021-12-13T10:10:00"));

        flightRepository.save(flight);

        assertThat(flightRepository.findAll())
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(flight);

        flightRepository.deleteById(flight.getId());

        assertThat(flightRepository.findAll())
                .hasSize(0);
        assertThat(flightRepository.count()).isEqualTo(0);
        assertThat(flightRepository.count()).isZero();
    }

    /* BY DIRECTLY USING JPA ENTITY MANAGER **/
//    @Autowired
//    private EntityManager entityManager;
//
//    @Test
//    void verifyFlightCanBeSaved() {
//        final Flight flight = new Flight();
//        flight.setOrigin("Amsterdam");
//        flight.setDestination("Helsinki");
//        flight.setScheduledAt(LocalDateTime.parse("2021-12-13T10:10:00"));
//
//        entityManager.persist(flight);
//
//        final TypedQuery<Flight> results = entityManager
//                .createQuery("SELECT f FROM Flight f", Flight.class);
//        final List<Flight> flights = results.getResultList();
//
//        Assertions.assertThat(flights)
//                .hasSize(1)
//                .first()
//                .isEqualTo(flight);
//    }

}
