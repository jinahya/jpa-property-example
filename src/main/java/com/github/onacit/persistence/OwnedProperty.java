package com.github.onacit.persistence;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@MappedSuperclass
public abstract class OwnedProperty<T extends BaseEntity> extends Property {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String ATTRIBUTE_NAME_OWNER = "owner";

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends OwnedProperty<U>, U extends BaseEntity> Optional<T> find(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyKey,
            final long ownerId) {
        if (entityManager == null) {
            throw new NullPointerException("entityManager is null");
        }
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        if (propertyKey == null) {
            throw new NullPointerException("propertyKey is null");
        }
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        final Root<T> root = criteria.from(entityClass);
        criteria.select(root);
//        criteria.where(builder.equal(root.get(OwnedProperty_.owner).get(BaseEntity_.id), ownerId),
//                       builder.equal(root.get(Property_.key), propertyKey));
        criteria.where(builder.equal(root.get(ATTRIBUTE_NAME_OWNER).get(ATTRIBUTE_NAME_ID), ownerId),
                       builder.equal(root.get(Property_.key), propertyKey));
        final TypedQuery<T> typed = entityManager.createQuery(criteria);
        try {
            return Optional.of(typed.getSingleResult());
        } catch (final NoResultException nre) {
            return Optional.empty();
        }
    }

    public static <T extends OwnedProperty<U>, U extends BaseEntity> Optional<T> find(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyKey,
            final U ownerInstance) {
        if (ownerInstance == null) {
            throw new NullPointerException("ownerInstance is null");
        }
        final Long ownerId = ownerInstance.getId();
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerInstance.id is null");
        }
        return find(entityClass, entityManager, propertyKey, ownerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends OwnedProperty<U>, U extends BaseEntity> boolean update(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyValue,
            final String propertyKey, final long ownerId) {
        if (entityManager == null) {
            throw new NullPointerException("entityManager is null");
        }
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        if (propertyValue == null) {
            throw new NullPointerException("propertyValue is null");
        }
        if (propertyKey == null) {
            throw new NullPointerException("propertyKey is null");
        }
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<T> criteria = builder.createCriteriaUpdate(entityClass);
        final Root<T> root = criteria.from(entityClass);
        criteria.set(Property_.value, propertyValue);
        criteria.where(builder.equal(root.get(OwnedProperty_.owner).get(BaseEntity_.id), ownerId),
                       builder.equal(root.get(Property_.key), propertyKey));
        final Query query = entityManager.createQuery(criteria);
        final int updated = query.executeUpdate();
        assert updated <= 1;
        return updated == 1;
    }

    public static <T extends OwnedProperty<U>, U extends BaseEntity> boolean update(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyValue,
            final String propertyKey, final U ownerInstance) {
        if (ownerInstance == null) {
            throw new NullPointerException("ownerInstance is null");
        }
        final Long ownerId = ownerInstance.getId();
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerInstance.id is null");
        }
        return update(entityClass, entityManager, propertyValue, propertyKey, ownerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends OwnedProperty<U>, U extends BaseEntity> boolean delete(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyKey,
            final long ownerId) {
        if (propertyKey == null) {
            throw new NullPointerException("propertyKey is null");
        }
        final int deleted = delete(entityClass, entityManager,
                                   (b, p) -> asList(b.equal(p.get(OwnedProperty_.owner).get(BaseEntity_.id), ownerId),
                                                    b.equal(p.get(Property_.key), propertyKey)));
        assert deleted <= 1;
        return deleted == 1;
    }

    public static <T extends OwnedProperty<U>, U extends BaseEntity> boolean delete(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyKey,
            final U ownerInstance) {
        if (ownerInstance == null) {
            throw new NullPointerException("ownerInstance is null");
        }
        final Long ownerId = ownerInstance.getId();
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerInstance.id is null");
        }
        return delete(entityClass, entityManager, propertyKey, ownerId);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance of given owner class.
     *
     * @param ownerClass the owner class.
     */
    public OwnedProperty(final Class<T> ownerClass) {
        super();
        this.ownerClass = requireNonNull(ownerClass, "ownerClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
                + "ownerClass=" + ownerClass
                + ",ownerId=" + ofNullable(getOwner()).map(BaseEntity::getId).orElse(null)
                + "}";
    }

    // ------------------------------------------------------------------------------------------------------ ownerClass
    public Class<T> getOwnerClass() {
        return ownerClass;
    }

    // ----------------------------------------------------------------------------------------------------------- owner
    public T getOwner() {
        return owner;
    }

    public void setOwner(final T owner) {
        this.owner = owner;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Transient
    final Class<T> ownerClass;

    @NotNull
    @ManyToOne//(optional = false) // hibernate
    private T owner;
}
