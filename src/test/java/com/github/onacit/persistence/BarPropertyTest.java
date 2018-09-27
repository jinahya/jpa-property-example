package com.github.onacit.persistence;

public class BarPropertyTest extends OwnedPropertyTest<BarProperty, Bar> {

    // -----------------------------------------------------------------------------------------------------------------
    public BarPropertyTest() {
        super(BarProperty.class, Bar.class); // FooProperty.OWNER_MAPPER);
    }
}
