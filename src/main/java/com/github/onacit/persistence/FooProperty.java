package com.github.onacit.persistence;

import javax.persistence.*;
import javax.persistence.criteria.Path;
import javax.validation.constraints.NotNull;
import java.util.function.Function;

import static com.github.onacit.persistence.FooProperty.COLUMN_NAME_FOO_ID;
import static com.github.onacit.persistence.OwnedProperty.ATTRIBUTE_NAME_OWNER;

@Entity
@DiscriminatorValue(FooProperty.DISCRIMINATOR_VALUE)
//@AssociationOverride(name = ATTRIBUTE_NAME_OWNER, joinColumns = @JoinColumn(name = COLUMN_NAME_FOO_ID))
public class FooProperty extends OwnedProperty<Foo> {

    // -----------------------------------------------------------------------------------------------------------------
    private static final long serialVersionUID = 7866208598640420330L;

    // -----------------------------------------------------------------------------------------------------------------
    public static final String DISCRIMINATOR_VALUE = "FOO";

    // -----------------------------------------------------------------------------------------------------------------
    public static final String COLUMN_NAME_FOO_ID = "FOO_ID";

    // -----------------------------------------------------------------------------------------------------------------
    public static final Function<Path<FooProperty>, Path<Foo>> OWNER_MAPPER = p -> p.get(FooProperty_.owner);

    // -----------------------------------------------------------------------------------------------------------------
    public FooProperty() {
        super(Foo.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public Foo getOwner() {
        return owner;
    }

    @Override
    public void setOwner(final Foo owner) {
        this.owner = owner;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    @ManyToOne//(optional = false) // hibernate
    @JoinColumn(name = COLUMN_NAME_FOO_ID, updatable = false)
    private Foo owner;
}
