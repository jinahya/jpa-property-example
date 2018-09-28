package com.github.onacit.persistence;

import io.github.benas.randombeans.api.ObjectGenerationException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Constructor;
import java.util.function.*;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Objects.requireNonNull;
import static javax.persistence.Persistence.createEntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class BaseEntityTest<T extends BaseEntity> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final Logger logger = getLogger(lookup().lookupClass());

    // -----------------------------------------------------------------------------------------------------------------
    static final String PERSISTENCE_UNIT_NAME = "testPU";

    private static final EntityManagerFactory PERSISTENCE_UNIT;

    static {
        try {
            PERSISTENCE_UNIT = createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            logger.info("persistence unit created: {}", PERSISTENCE_UNIT);
            final Metamodel metamodel = PERSISTENCE_UNIT.getMetamodel();
            for (final ManagedType<?> managedType : metamodel.getManagedTypes()) {
                logger.debug("managedType: {}", managedType);
                managedType.getAttributes().forEach(a -> logger.debug("\tattribute: {}", a));
            }
            for (final EntityType<?> entityType : metamodel.getEntities()) {
                logger.debug("entityType: {}", entityType);
                entityType.getAttributes().forEach(a -> logger.debug("\tattribute: {}", a));
            }
        } catch (final Exception e) {
            e.printStackTrace();
            throw new InstantiationError(e.getMessage());
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <R> R applyPersistenceContext(final Function<EntityManager, R> function) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        final EntityManager persistenceContext = PERSISTENCE_UNIT.createEntityManager();
        try {
            persistenceContext.getTransaction().begin();
            try {
                return function.apply(persistenceContext);
            } finally {
                persistenceContext.getTransaction().commit();
            }
        } finally {
            persistenceContext.close();
        }
    }

    static <U, R> R applyPersistenceContext(final BiFunction<EntityManager, U, R> function,
                                            final Supplier<U> supplier) {
        if (function == null) {
            throw new NullPointerException("function is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        return applyPersistenceContext(persistenceContext -> function.apply(persistenceContext, supplier.get()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    static void acceptPersistenceContext(final Consumer<EntityManager> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        applyPersistenceContext(persistenceContext -> {
            consumer.accept(persistenceContext);
            return null;
        });
    }

    static <U> void acceptPersistenceContext(final BiConsumer<EntityManager, U> consumer, final Supplier<U> supplier) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }
        acceptPersistenceContext(persistenceContext -> consumer.accept(persistenceContext, supplier.get()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Deprecated
    static <T extends BaseEntity> T persistInstance(final Class<T> entityClass, final EntityManager entityManager,
                                                    final Supplier<T> entitySupplier, final Predicate<T> entityFinder) {
        if (true) {
            return persistInstance(entityClass, () -> entityManager, entitySupplier, (m, e) -> entityFinder.test(e));
        }
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        if (entityManager == null) {
            throw new NullPointerException("entityManager is null");
        }
        if (entitySupplier == null) {
            throw new NullPointerException("entitySupplier is null");
        }
        if (entityFinder == null) {
            throw new NullPointerException("entityFinder is null");
        }
        for (T entityInstance; true; ) {
            entityInstance = entitySupplier.get();
            try {
                entityManager.persist(entityInstance);
                entityManager.flush();
                return entityInstance;
            } catch (final PersistenceException pe) {
                if (entityFinder.test(entityInstance)) {
                    logger.debug("found existing for {}", entityInstance);
                    continue;
                }
                throw pe;
            }
        }
    }

    static <T extends BaseEntity> T persistInstance(final Class<T> entityClass,
                                                    final Supplier<EntityManager> managerSupplier,
                                                    final Supplier<T> entitySupplier,
                                                    final BiPredicate<EntityManager, T> entityFinder) {
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        if (managerSupplier == null) {
            throw new NullPointerException("managerSupplier is null");
        }
        if (entitySupplier == null) {
            throw new NullPointerException("entitySupplier is null");
        }
        if (entityFinder == null) {
            throw new NullPointerException("entityFinder is null");
        }
        final EntityManager entityManager = managerSupplier.get();
        while (true) {
            final T entityInstance = entitySupplier.get();
            try {
                entityManager.persist(entityInstance);
                return entityInstance;
            } catch (final PersistenceException pe) {
                if (entityFinder.test(entityManager, entityInstance)) {
                    continue;
                }
                throw pe;
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance for testing specified entity class.
     *
     * @param entityClass the entity class to test.
     */
    BaseEntityTest(final Class<T> entityClass) {
        super();
        this.entityClass = requireNonNull(entityClass, "entityClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    Constructor<T> entityConstructor() throws ReflectiveOperationException {
        if (entityConstructor == null) {
            entityConstructor = entityClass.getDeclaredConstructor();
            if (entityConstructor.isSynthetic()) {
                entityConstructor.setAccessible(true);
            }
        }
        return entityConstructor;
    }

    T entityInstance() {
        try {
            return random(entityClass, BaseEntity.ATTRIBUTE_NAME_ID);
        } catch (final ObjectGenerationException oge) {
            // don't care
        }
        try {
            return entityConstructor().newInstance();
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    // -------------------------------------------------------------------------------------------------------------- id
    @Test
    void testGetId() {
        final Long id = entityInstance().getId();
        assertNull(id);
    }

    @Test
    void testSetId() {
        entityInstance().setId(null);
        entityInstance().setId(1L);
    }

    // -----------------------------------------------------------------------------------------------------------------
    final Class<T> entityClass;

    private transient Constructor<T> entityConstructor;
}
