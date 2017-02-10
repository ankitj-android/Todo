package com.ajasuja.codepath.todo.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by ajasuja on 2/9/17.
 */
@Database(name = CodepathDatabase.NAME, version = CodepathDatabase.VERSION)
public class CodepathDatabase {
    public static final String NAME = "codepath";

    public static final int VERSION = 1;
}
