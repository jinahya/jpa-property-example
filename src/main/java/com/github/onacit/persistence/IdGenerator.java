package com.github.onacit.persistence;

final class IdGenerator {

    static final String NAME = "ID_GENERATOR";

    static final String TABLE = GeneratedId.TABLE_NAME;

    static final String PK_COLUMN_NAME = "PK";

    static final String VALUE_COLUMN_NAME = "VALUE_";

    static final String PK_COLUMN_VALUE = "ID";

    static final int ALLOCATION_SIZE = 50;

    // -----------------------------------------------------------------------------------------------------------------
    private IdGenerator() {
        super();
    }
}
