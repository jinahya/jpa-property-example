package com.github.onacit.persistence;

import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@MappedSuperclass
public abstract class UnownedProperty extends Property {

    // -----------------------------------------------------------------------------------------------------------------
    static <T extends UnownedProperty> Optional<T> find(final Class<T> entityClass, final EntityManager entityManager,
                                                        final String propertyKey) {
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        if (entityManager == null) {
            throw new NullPointerException("entityManager is null");
        }
        if (propertyKey == null) {
            throw new NullPointerException("propertyKey is null");
        }
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        final Root<T> root = criteria.from(entityClass);
        criteria.where(builder.equal(root.get(Property_.KEY), propertyKey));
        final TypedQuery<T> typed = entityManager.createQuery(criteria);
        try {
            return Optional.of(typed.getSingleResult());
        } catch (final NoResultException nre) {
            return Optional.empty();
        }
    }
}
