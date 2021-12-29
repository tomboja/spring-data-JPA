package com.tomboja.springdatajpa;

import com.tomboja.springdatajpa.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * @ProjectName: spring-data-JPA
 * @Author: tdessalegn
 * @Date: 12/29/21
 */

@SpringBootTest
@Transactional
public class CustomRepoImplTest {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldSaveCustomImpl() {
        final Flight toDelete = createFlight("London");
        final Flight toKeep = createFlight("Paris");

        flightRepository.save(toDelete);
        flightRepository.save(toKeep);

        flightRepository.deleteByOrigin("London");

        Assertions.assertThat(flightRepository.findAll())
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(toKeep);
    }

    private Flight createFlight(String origin) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination("Tokyo");
        flight.setScheduledAt(LocalDateTime.parse("2021-10-10T20:20:00"));
        return flight;
    }
}
