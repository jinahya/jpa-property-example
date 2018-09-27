package com.github.onacit.persistence;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class UnownedPropertyTest<T extends UnownedProperty> extends PropertyTest<T> {


    // -----------------------------------------------------------------------------------------------------------------
    static <T extends UnownedProperty> BiPredicate<EntityManager, T> entityFinder(final Class<T> entityClass) {
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        return (m, e) -> UnownedProperty.find(entityClass, m, e.getKey()).isPresent();
    }

    static <T extends UnownedProperty> T persistInstance(final Class<T> entityClass,
                                                         final Supplier<EntityManager> managerSupplier,
                                                         final Supplier<T> entitySupplier) {
        return persistInstance(entityClass, managerSupplier, entitySupplier, entityFinder(entityClass));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public UnownedPropertyTest(final Class<T> propertyClass) {
        super(propertyClass);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testPersist() {
        final T persisted = applyPersistenceContext(m -> persistInstance(entityClass, () -> m, this::entityInstance));
    }

    @Test
    void testPersistAndFindById() {
        final T persisted = applyPersistenceContext(m -> persistInstance(entityClass, () -> m, this::entityInstance));
        final T found = applyPersistenceContext(m -> m.find(entityClass, persisted.getId()));
        assertNotNull(found);
        assertEquals(persisted.getId(), found.getId());
        assertEquals(persisted.getKey(), found.getKey());
        assertEquals(persisted.getValue(), found.getValue());
    }

    @Test
    void testPersistAndFindByKey() {
        final T persisted = applyPersistenceContext(m -> persistInstance(entityClass, () -> m, this::entityInstance));
        final String key = persisted.getKey();
        final T found = applyPersistenceContext(v -> UnownedProperty.find(entityClass, v, key).orElse(null));
        assertNotNull(found);
        assertEquals(persisted.getId(), found.getId());
        assertEquals(persisted.getKey(), found.getKey());
        assertEquals(persisted.getValue(), found.getValue());
    }
}
