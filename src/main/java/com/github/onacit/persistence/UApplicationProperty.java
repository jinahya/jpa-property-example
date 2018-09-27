package com.github.onacit.persistence;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(UApplicationProperty.DISCRIMINATOR_VALUE)
public class UApplicationProperty extends UnownedProperty {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String DISCRIMINATOR_VALUE = "APPLICATION";
}
