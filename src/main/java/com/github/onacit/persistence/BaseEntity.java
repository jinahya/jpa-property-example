package com.github.onacit.persistence;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    // -------------------------------------------------------------------------------------------------------------- ID
    public static final String COLUMN_NAME_ID = "ID";

    public static final String ATTRIBUTE_NAME_ID = "id";


    // -----------------------------------------------------------------------------------------------------------------
    protected static <T extends BaseEntity> int delete(
            final Class<T> entityClass, final EntityManager entityManager,
            final BiFunction<CriteriaBuilder, Path<T>, List<Predicate>> predicatesSupplier) {
        if (entityClass == null) {
            throw new NullPointerException("entityClass is null");
        }
        if (entityManager == null) {
            throw new NullPointerException("entityManager is null");
        }
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaDelete<T> criteria = builder.createCriteriaDelete(entityClass);
        final Root<T> root = criteria.from(entityClass);
        ofNullable(predicatesSupplier).map(v -> v.apply(builder, root).toArray(new Predicate[0]))
                .ifPresent(criteria::where);
        return entityManager.createQuery(criteria).executeUpdate();
    }

    public static <T extends BaseEntity> boolean delete(final Class<T> entityClass, final EntityManager entityManager,
                                                        final long entityId) {
        return delete(entityClass, entityManager,
                      (b, p) -> singletonList(b.equal(p.get(BaseEntity_.id), entityId))) == 1;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
                + "id=" + id
                + "}";
    }

    // -------------------------------------------------------------------------------------------------------------- id
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Id
    @TableGenerator(name = IdGenerator.NAME, table = IdGenerator.TABLE, pkColumnName = IdGenerator.PK_COLUMN_NAME,
                    valueColumnName = IdGenerator.VALUE_COLUMN_NAME, pkColumnValue = IdGenerator.PK_COLUMN_VALUE,
                    allocationSize = IdGenerator.ALLOCATION_SIZE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = IdGenerator.NAME)
    @Column(name = COLUMN_NAME_ID, nullable = false, updatable = false) //, unique = true)
    private Long id;
}
