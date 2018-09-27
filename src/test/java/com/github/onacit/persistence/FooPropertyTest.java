package com.github.onacit.persistence;

public class FooPropertyTest extends OwnedPropertyTest<FooProperty, Foo> {

    // -----------------------------------------------------------------------------------------------------------------
    public FooPropertyTest() {
        super(FooProperty.class, Foo.class); // FooProperty.OWNER_MAPPER);
    }
}
