package com.tomboja.springdatajpa.repository;

import javax.persistence.EntityManager;

/**
 * @ProjectName: spring-data-JPA
 * @Author: tdessalegn
 * @Date: 12/29/21
 */

public class DeleteByOriginRepositoryImpl implements DeleteByOriginRepository {

    // Let's implement the deletion by using Entity manager
    private final EntityManager entityManager;

    public DeleteByOriginRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deleteByOrigin(String origin) {
        entityManager
                .createNativeQuery("DELETE from flight WHERE origin=?")
                .setParameter(1, origin)
                .executeUpdate();
    }
}
