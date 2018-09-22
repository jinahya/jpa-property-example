package com.github.onacit.persistence;

public class FooTest extends BaseTest<Foo> {

    // -----------------------------------------------------------------------------------------------------------------
    public FooTest() {
        super(Foo.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    Foo entityInstance() {
        return super.entityInstance();
    }
}
