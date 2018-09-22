package com.github.onacit.persistence;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(SystemProperty.DISCRIMINATOR_VALUE)
public class SystemProperty extends UnownedProperty {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String DISCRIMINATOR_VALUE = "SYSTEM";
}
