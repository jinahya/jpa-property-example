package com.github.onacit.persistence;

import javax.persistence.AssociationOverride;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import static com.github.onacit.persistence.OFooProperty.COLUMN_NAME_FOO_ID;
import static com.github.onacit.persistence.OwnedProperty.ATTRIBUTE_NAME_OWNER;

@Entity
@DiscriminatorValue(OFooProperty.DISCRIMINATOR_VALUE)
@AssociationOverride(name = ATTRIBUTE_NAME_OWNER, joinColumns = @JoinColumn(name = COLUMN_NAME_FOO_ID, updatable = false))
public class OFooProperty extends OwnedProperty<OFoo> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final long serialVersionUID = 7866208598640420330L;

    // -----------------------------------------------------------------------------------------------------------------
    public static final String DISCRIMINATOR_VALUE = "FOO";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_NAME_FOO_ID = "FOO_ID";

    // -----------------------------------------------------------------------------------------------------------------
    public OFooProperty() {
        super(OFoo.class);
    }
}
