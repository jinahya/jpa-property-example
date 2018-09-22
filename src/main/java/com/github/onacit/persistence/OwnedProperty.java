package com.github.onacit.persistence;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

@MappedSuperclass
public abstract class OwnedProperty<T extends Base> extends Property {

    // -----------------------------------------------------------------------------------------------------------------
    //public static final String PROPERTY_NAME_OWNER = "owner";

    public static final String ATTRIBUTE_NAME_OWNER = "owner";

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends OwnedProperty<U>, U extends Base> Optional<T> find(
            final Class<T> entityClass, final EntityManager entityManager,
            final String propertyKey, final Function<Path<T>, Path<U>> ownerMapper, final long ownerId) {
        if (entityManager == null) {
            throw new NullPointerException("entityManager is null");
        }
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        if (propertyKey == null) {
            throw new NullPointerException("propertyKey is null");
        }
        if (ownerMapper == null) {
            throw new NullPointerException("ownerMapper is null");
        }
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        final Root<T> root = criteria.from(entityClass);
        criteria.select(root);
        //https://stackoverflow.com/q/32328333/330457
        //https://stackoverflow.com/q/3854687/330457
        criteria.where(builder.equal(ownerMapper.apply(root).get(Base_.id), ownerId),
                       builder.equal(root.get(Property_.key), propertyKey));
        final TypedQuery<T> typed = entityManager.createQuery(criteria);
        try {
            return Optional.of(typed.getSingleResult());
        } catch (final NoResultException nre) {
            return Optional.empty();
        }
    }

    public static <T extends OwnedProperty<U>, U extends Base> Optional<T> find(
            final Class<T> entityClass, final EntityManager entityManager,
            final String propertyKey, final Function<Path<T>, Path<U>> ownerMapper, final U ownerInstance) {
        if (ownerInstance == null) {
            throw new NullPointerException("ownerInstance is null");
        }
        final Long ownerId = ownerInstance.getId();
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerInstance.id is null");
        }
        return find(entityClass, entityManager, propertyKey, ownerMapper, ownerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends OwnedProperty<U>, U extends Base> boolean update(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyValue,
            final String propertyKey, final Function<Path<T>, Path<U>> ownerMapper, final long ownerId) {
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
        if (ownerMapper == null) {
            throw new NullPointerException("ownerMapper is null");
        }
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<T> criteria = builder.createCriteriaUpdate(entityClass);
        final Root<T> root = criteria.from(entityClass);
        criteria.set(Property_.value, propertyValue);
        criteria.where(builder.equal(ownerMapper.apply(root).get(Base_.id), ownerId),
                       builder.equal(root.get(Property_.key), propertyKey));
        final Query query = entityManager.createQuery(criteria);
        final int updated = query.executeUpdate();
        assert updated <= 1;
        return updated == 1;
    }

    public static <T extends OwnedProperty<U>, U extends Base> boolean update(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyValue,
            final String propertyKey, final Function<Path<T>, Path<U>> ownerMapper, final U ownerInstance) {
        if (ownerInstance == null) {
            throw new NullPointerException("ownerInstance is null");
        }
        final Long ownerId = ownerInstance.getId();
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerInstance.id is null");
        }
        return update(entityClass, entityManager, propertyValue, propertyKey, ownerMapper, ownerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends OwnedProperty<U>, U extends Base> boolean delete(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyKey,
            final Function<Path<T>, Path<U>> ownerMapper, final long ownerId) {
        if (entityManager == null) {
            throw new NullPointerException("entityManager is null");
        }
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        if (propertyKey == null) {
            throw new NullPointerException("propertyKey is null");
        }
        if (ownerMapper == null) {
            throw new NullPointerException("ownerMapper is null");
        }
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaDelete<T> criteria = builder.createCriteriaDelete(entityClass);
        final Root<T> root = criteria.from(entityClass);
        criteria.where(builder.equal(ownerMapper.apply(root).get(Base_.id), ownerId),
                       builder.equal(root.get(Property_.key), propertyKey));
        final Query query = entityManager.createQuery(criteria);
        final int deleted = query.executeUpdate();
        assert deleted <= 1;
        return deleted == 1;
    }

    public static <T extends OwnedProperty<U>, U extends Base> boolean delete(
            final Class<T> entityClass, final EntityManager entityManager, final String propertyKey,
            final Function<Path<T>, Path<U>> ownerMapper, final U ownerInstance) {
        if (ownerInstance == null) {
            throw new NullPointerException("ownerInstance is null");
        }
        final Long ownerId = ownerInstance.getId();
        if (ownerId == null) {
            throw new IllegalArgumentException("ownerInstance.id is null");
        }
        return delete(entityClass, entityManager, propertyKey, ownerMapper, ownerId);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public OwnedProperty(final Class<T> ownerClass) {
        super();
        this.ownerClass = requireNonNull(ownerClass, "ownerClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + "ownerClass=" + ownerClass
               + ",ownerId=" + ofNullable(getOwner()).map(Base::getId).orElse(null)
               + "}";
    }

    // ------------------------------------------------------------------------------------------------------ ownerClass
    public Class<T> getOwnerClass() {
        return ownerClass;
    }

    // ----------------------------------------------------------------------------------------------------------- owner
    public abstract T getOwner();

    public abstract void setOwner(T owner);
//    public T getOwner() {
//        return owner;
//    }
//
//    public void setOwner(final T owner) {
//        this.owner = owner;
//    }

    // -----------------------------------------------------------------------------------------------------------------
    @Transient
    protected final Class<T> ownerClass;

//    @NotNull
//    @ManyToOne//(optional = false)
//    private T owner;
}
