package com.github.onacit.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = Bar.TABLE_NAME)
public class Bar extends Base {

    public static final String TABLE_NAME = "BAR";
}
