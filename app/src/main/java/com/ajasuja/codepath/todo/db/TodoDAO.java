package com.ajasuja.codepath.todo.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by ajasuja on 2/9/17.
 */

public class TodoDAO {

    public void upsert(Todo todo) {
        todo.save();
    }

    public void delete(Todo todo) {
        todo.delete();
    }

    public void deleteAll() {
        List<Todo> allTodos = SQLite.select().from(Todo.class).queryList();
        for (Todo todo : allTodos) {
            todo.delete();
        }
    }

    public List<Todo> getAllTodos() {
        return SQLite.select().from(Todo.class).queryList();
    }


}
