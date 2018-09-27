package com.github.onacit.persistence;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.lang.reflect.Method;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class OwnedPropertyTest<T extends OwnedProperty<U>, U extends Base> extends PropertyTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = getLogger(lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    public OwnedPropertyTest(final Class<T> propertyClass, final Class<U> ownerClass) {
        super(propertyClass);
        this.ownerClass = requireNonNull(ownerClass, "ownerClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    U ownerInstance() {
        final U ownerInstance;
        try {
            final Class<?> c = Class.forName(ownerClass.getName() + "Test");
            final Method m = c.getDeclaredMethod("entityInstance");
            if (!m.isAccessible()) {
                m.setAccessible(true);
            }
            final Object i = c.newInstance();
            ownerInstance = ownerClass.cast(m.invoke(i));
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
        return ownerInstance;
    }

    U ownerInstancePersisted() {
        final U ownerInstance = ownerInstance();
        acceptPersistenceContext(entityManager -> entityManager.persist(ownerInstance));
        return ownerInstance;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    protected T entityInstance() {
        final T entityInstance = super.entityInstance();
        entityInstance.setOwner(ownerInstancePersisted());
        return entityInstance;
    }

    // -----------------------------------------------------------------------------------------------------------------
//    Optional<T> findInstance(final String key, final U owner) {
//        return applyPersistenceContext(m -> OwnedProperty.find(entityClass, m, key, ownerMapper, owner));
//    }

//    T persistInstance() {
//        return applyPersistenceContext(entityManager -> persistInstance(
//                entityClass, () -> entityManager, this::entityInstance,
//                (m, e) -> findInstance(e.getKey(), e.getOwner()).isPresent()));
//    }
//
//    boolean updateInstance(final String value, final String key, final U owner) {
//        return applyPersistenceContext(m -> OwnedProperty.update(entityClass, m, value, key, ownerMapper, owner));
//    }
//
//    boolean deleteInstance(final String key, final U owner) {
//        return applyPersistenceContext(m -> OwnedProperty.delete(entityClass, m, key, ownerMapper, owner));
//    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testPersist() {
        final T persisted = applyPersistenceContext(m -> {
            final T entityInstance = entityInstance();
            m.persist(entityInstance);
            return entityInstance;
        });
        logger.debug("persisted: {}", persisted);
    }
//
//    @Test
//    void testPersistAndFind() {
//        final T persisted = persistInstance();
//        final T found = findInstance(persisted.getKey(), persisted.getOwner()).orElse(null);
//        assertNotNull(found);
//        assertEquals(persisted.getId(), found.getId());
//        assertEquals(persisted.getKey(), found.getKey());
//        assertEquals(persisted.getValue(), found.getValue());
//    }
//
//    @Test
//    void testPersistAndUpdate() {
//        final T persisted = persistInstance();
//        final boolean updated = updateInstance(UUID.randomUUID().toString(), persisted.getKey(), persisted.getOwner());
//        assertTrue(updated);
//    }
//
//    @Test
//    void testPersistAndDelete() {
//        final T persisted = persistInstance();
//        final boolean deleted = deleteInstance(persisted.getKey(), persisted.getOwner());
//        assertTrue(deleted);
//    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<U> ownerClass;
}
