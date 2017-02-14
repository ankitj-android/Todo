package com.ajasuja.codepath.todo;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ajasuja.codepath.todo.db.Todo;
import com.ajasuja.codepath.todo.db.TodoDAO;

import java.util.List;

/**
 * Created by ajasuja on 2/10/17.
 */

public class TodoAdapter extends ArrayAdapter<Todo> {
    public TodoAdapter(Context context, List<Todo> todoItems) {
        super(context, 0, todoItems);
    }

    private static class TodoViewHolder {
        TextView todoItemName;
        CheckBox todoIsComplete;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Todo todoItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_list, parent, false);
        }
        CheckBox checkBoxIsComplete = (CheckBox) convertView.findViewById(R.id.checkBoxIsComplete);
        final TextView textViewTodoItemName = (TextView) convertView.findViewById(R.id.textViewTodoItemName);
        TextView textViewDueDate = (TextView) convertView.findViewById(R.id.textViewDueDate);
        TextView textViewPriority = (TextView) convertView.findViewById(R.id.textViewPriority);
        ImageButton imageButtonDelete = (ImageButton) convertView.findViewById(R.id.imageButtonDelete);
        ImageButton imageButtonCalendar = (ImageButton) convertView.findViewById(R.id.imageButtonCalendar);
        imageButtonCalendar.setTag(todoItem);
        imageButtonDelete.setTag(todoItem);

        checkBoxIsComplete.setChecked(todoItem.isComplete() == null ? false : todoItem.isComplete());
        textViewTodoItemName.setText(todoItem.getTodoName() == null ? "default" : todoItem.getTodoName());
//        textViewDueDate.setText(String.valueOf(todoItem.getTimeInMillis()));
        textViewDueDate.setText(DateUtils.formatDateTime(getContext(), todoItem.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE));
        textViewPriority.setText("P" + todoItem.getPriority());

        checkBoxIsComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("check box state changed ... " + isChecked + ", " +todoItem) ;
                if (isChecked) {
                    textViewTodoItemName.setPaintFlags(textViewTodoItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textViewTodoItemName.setPaintFlags(0);
                }
                todoItem.setComplete(isChecked);
                TodoDAO todoDAO = new TodoDAO();
                todoDAO.upsert(todoItem);
            }
        });

        return convertView;
    }
}
