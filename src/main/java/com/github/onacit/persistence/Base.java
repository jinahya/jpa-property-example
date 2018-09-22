package com.github.onacit.persistence;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class Base implements Serializable {

    // -------------------------------------------------------------------------------------------------------------- ID
    public static final String COLUMN_NAME_ID = "ID";

    public static final String ATTRIBUTE_NAME_ID = "id";

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
