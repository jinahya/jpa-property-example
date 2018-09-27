package com.github.onacit.persistence;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = OBar.TABLE_NAME)
public class OBar extends BaseEntity {

    public static final String TABLE_NAME = "BAR";
}
