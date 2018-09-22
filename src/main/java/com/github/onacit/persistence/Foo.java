package com.github.onacit.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = Foo.TABLE_NAME)
public class Foo extends Base {

    public static final String TABLE_NAME = "FOO";
}
