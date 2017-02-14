package com.ajasuja.codepath.todo.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by ajasuja on 2/9/17.
 */
@Table(database = CodepathDatabase.class)
public class Todo extends BaseModel implements Serializable {

    @PrimaryKey(autoincrement = true)
    @Column
    long id;

    @Column
    private String todoName;

    @Column
    private Boolean isComplete;

    @Column
    private int priority;

    @Column
    private long timeInMillis;

    public long getId() {
        return id;
    }

    public String getTodoName() {
        return todoName;
    }

    public void setTodoName(String todoName) {
        this.todoName = todoName;
    }

    public Boolean isComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format("%d, %s, %s, %d, %d", id, todoName, isComplete, priority, timeInMillis);
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}