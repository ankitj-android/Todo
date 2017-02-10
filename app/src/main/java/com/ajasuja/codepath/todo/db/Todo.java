package com.ajasuja.codepath.todo.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by ajasuja on 2/9/17.
 */
@Table(database = CodepathDatabase.class)
public class Todo extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    long id;

    @Column
    private String todoName;

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }
}