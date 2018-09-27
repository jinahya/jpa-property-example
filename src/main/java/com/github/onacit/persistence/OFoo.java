package com.github.onacit.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = OFoo.TABLE_NAME)
public class OFoo extends BaseEntity {

    public static final String TABLE_NAME = "FOO";
}
