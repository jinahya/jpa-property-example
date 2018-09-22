package com.github.onacit.persistence;

import java.util.UUID;

public abstract class PropertyTest<T extends Property> extends BaseTest<T> {

    // -----------------------------------------------------------------------------------------------------------------
    public PropertyTest(final Class<T> propertyClass) {
        super(propertyClass);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    protected T entityInstance() {
        final T entityInstance = super.entityInstance();
        entityInstance.setKey(UUID.randomUUID().toString());
        entityInstance.setValue(UUID.randomUUID().toString());
        return entityInstance;
    }
}
