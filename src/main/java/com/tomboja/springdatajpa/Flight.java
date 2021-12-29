package com.tomboja.springdatajpa;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @ProjectName: spring-data-JPA
 * @Author: tdessalegn
 * @Date: 12/28/21
 */

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Flight {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
    private String destination;
    private LocalDateTime scheduledAt;
}
