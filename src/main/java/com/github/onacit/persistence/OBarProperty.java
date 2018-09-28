package com.github.onacit.persistence;

import javax.persistence.AssociationOverride;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import static com.github.onacit.persistence.OBarProperty.COLUMN_NAME_BAR_ID;
import static com.github.onacit.persistence.OwnedProperty.ATTRIBUTE_NAME_OWNER;

@Entity
@DiscriminatorValue(OBarProperty.DISCRIMINATOR_VALUE)
@AssociationOverride(name = ATTRIBUTE_NAME_OWNER,
                     joinColumns = @JoinColumn(name = COLUMN_NAME_BAR_ID, updatable = false))
public class OBarProperty extends OwnedProperty<OBar> {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String DISCRIMINATOR_VALUE = "BAR";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_NAME_BAR_ID = "BAR_ID";

    // -----------------------------------------------------------------------------------------------------------------
    public OBarProperty() {
        super(OBar.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = COLUMN_NAME_BAR_ID, updatable = false)
//    @Override
//    public OBar getOwner() {
//        return super.getOwner();
//    }
}
