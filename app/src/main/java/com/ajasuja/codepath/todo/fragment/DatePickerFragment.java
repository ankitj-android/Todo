package com.ajasuja.codepath.todo.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.ajasuja.codepath.todo.activity.MainActivity;
import com.ajasuja.codepath.todo.db.Todo;

import java.util.Calendar;

/**
 * Created by ajasuja on 2/13/17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        System.out.println("on date set ... ");
        Todo todoItem = (Todo) getArguments().getSerializable("TodoItem");
        DatePickerDialogListener datePickerDialogListener = (MainActivity) getActivity();
        datePickerDialogListener.onDateSelected(todoItem, year, month, day);
    }

    public interface DatePickerDialogListener {
        void onDateSelected(Todo todo, int year, int month, int day);
    }
}