package com.github.onacit.persistence;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(USystemProperty.DISCRIMINATOR_VALUE)
public class USystemProperty extends UnownedProperty {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String DISCRIMINATOR_VALUE = "SYSTEM";
}
