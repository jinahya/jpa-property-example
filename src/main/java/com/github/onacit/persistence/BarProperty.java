package com.github.onacit.persistence;

import javax.persistence.AssociationOverride;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import static com.github.onacit.persistence.BarProperty.COLUMN_NAME_BAR_ID;
import static com.github.onacit.persistence.OwnedProperty.ATTRIBUTE_NAME_OWNER;

@Entity
@DiscriminatorValue(BarProperty.DISCRIMINATOR_VALUE)
@AssociationOverride(name = ATTRIBUTE_NAME_OWNER, joinColumns = @JoinColumn(name = COLUMN_NAME_BAR_ID))
public class BarProperty extends OwnedProperty<Bar> {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String DISCRIMINATOR_VALUE = "BAR";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_NAME_BAR_ID = "BAR_ID";

    // -----------------------------------------------------------------------------------------------------------------
    public BarProperty() {
        super(Bar.class);
    }
}
