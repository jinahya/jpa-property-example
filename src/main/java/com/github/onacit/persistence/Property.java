package com.github.onacit.persistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = Property.TABLE_NAME,
       indexes = {
               @Index(columnList = Property.COLUMN_NAME_DTYPE + ", " + Property.COLUMN_NAME_KEY,
                      name = "INDEXED_DTYPE_KEY"),
               @Index(columnList = Property.COLUMN_NAME_DTYPE + ", " + OFooProperty.COLUMN_NAME_FOO_ID + ", "
                       + Property.COLUMN_NAME_KEY, name = "INDEXED_DTYPE_FOO_ID_KEY"),
               @Index(columnList = Property.COLUMN_NAME_DTYPE + ", " + OBarProperty.COLUMN_NAME_BAR_ID + ", "
                       + Property.COLUMN_NAME_KEY, name = "INDEXED_DTYPE_BAR_ID_KEY")
       }
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = Property.COLUMN_NAME_DTYPE)
public abstract class Property extends BaseEntity {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String TABLE_NAME = "PROPERTY";

    public static final String COLUMN_NAME_DTYPE = "DTYPE";

    // ---------------------------------------------------------------------------------------------------------- VALUE_
    public static final String COLUMN_NAME_VALUE = "VALUE_";

    public static final String ATTRIBUTE_NAME_VALUE = "value";

    public static final int SIZE_MIN_VALUE = 0;

    public static final int SIZE_MAX_VALUE = 255;

    // ------------------------------------------------------------------------------------------------------------ KEY_
    public static final String COLUMN_NAME_KEY = "KEY_";

    public static final String ATTRIBUTE_NAME_KEY = "key";

    public static final int SIZE_MIN_KEY = 1;

    public static final int SIZE_MAX_KEY = 255;


    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public Property() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
                + "value=" + value
                + ",key=" + key
                + "}";
    }

    // ----------------------------------------------------------------------------------------------------------- value
    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    // ------------------------------------------------------------------------------------------------------------- key
    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Size(min = SIZE_MIN_VALUE, max = SIZE_MAX_VALUE)
    @NotNull
    @Basic(optional = false)
    @Column(name = COLUMN_NAME_VALUE, nullable = false)
    private String value;

    @Size(min = SIZE_MIN_KEY, max = SIZE_MAX_KEY)
    @NotNull
    @Basic(optional = false)
    @Column(name = COLUMN_NAME_KEY, nullable = false, updatable = false)
    private String key;
}
