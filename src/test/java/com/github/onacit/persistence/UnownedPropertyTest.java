package com.github.onacit.persistence;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

public abstract class UnownedPropertyTest<T extends UnownedProperty> extends PropertyTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    public UnownedPropertyTest(final Class<T> propertyClass) {
        super(propertyClass);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testPersist() {
//        final T persisted = applyPersistenceContext(
//                entityManager -> persistInstance(entityClass, () -> entityManager, this::entityInstance));
    }

    @Test
    void testPersistAndFindById() {
//        final T persisted = applyPersistenceContext(
//                entityManager -> persistInstance(entityClass, () -> entityManager, this::entityInstance));
//        final T found = applyPersistenceContext(entityManager -> entityManager.find(entityClass, persisted.getId()));
//        assertNotNull(found);
//        assertEquals(persisted.getId(), found.getId());
//        assertEquals(persisted.getKey(), found.getKey());
//        assertEquals(persisted.getValue(), found.getValue());
    }

    @Test
    void testPersistAndFindByKey() {
//        final T persisted = applyPersistenceContext(
//                entityManager -> persistInstance(entityClass, () -> entityManager, this::entityInstance));
//        final String key = persisted.getKey();
//        final T found = applyPersistenceContext(v -> find(entityClass, v, key).orElse(null));
//        assertNotNull(found);
//        assertEquals(persisted.getId(), found.getId());
//        assertEquals(persisted.getKey(), found.getKey());
//        assertEquals(persisted.getValue(), found.getValue());
    }

    @Test
    void testPersistAndUpdateByKey() {
//        final T persisted = applyPersistenceContext(
//                entityManager -> persistInstance(entityClass, () -> entityManager, this::entityInstance));
//        final String key = persisted.getKey();
//        final String value = randomUUID().toString();
//        final boolean updated = applyPersistenceContext(v -> update(entityClass, v, value, key));
//        assertTrue(updated);
    }

    @Test
    public void testPersistAndDeleteByKey() {
//        final T persisted = applyPersistenceContext(
//                entityManager -> persistInstance(entityClass, () -> entityManager, this::entityInstance));
//        final String key = persisted.getKey();
//        final boolean deleted = applyPersistenceContext(v -> delete(entityClass, v, key));
//        assertTrue(deleted);
    }
}
