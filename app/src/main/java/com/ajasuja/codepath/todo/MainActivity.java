package com.ajasuja.codepath.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.ajasuja.codepath.todo.db.Todo;
import com.ajasuja.codepath.todo.db.TodoDAO;
import com.ajasuja.codepath.todo.fragment.DatePickerFragment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.DatePickerDialogListener {

    private List<Todo> todoItems;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listViewTodoItems;
    private EditText editTextNewItem;
    private TodoAdapter todoAdapter;
    private Spinner spinnerPriority;

    private static final String TODO_FILE_NAME =  "todo.txt";
    private static final int REQEUST_CODE = 0;
    private boolean toFile = false;

    private List<String> toTodoItemNames(List<Todo> todoItems) {
        List<String> todoItemNames = new ArrayList<>();
        for (Todo todoItem : todoItems) {
            String todoName = todoItem.getTodoName();
            if (todoName != null) {
                todoItemNames.add(todoName);
            } else {
                todoItemNames.add("");
            }
        }
        return todoItemNames;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQEUST_CODE && resultCode == RESULT_OK) {
            System.out.println("passed to parent activity successfully ...... ");
            String todoItemUpdated = data.getStringExtra("todoItemUpdated");
            int position = data.getIntExtra("position", 0);
            System.out.println("on Main activity ... " + todoItemUpdated + " at position ... " + position);
//            todoItems.set(position, todoItemUpdated);
            todoItems.get(position).setTodoName(todoItemUpdated);
            todoAdapter.notifyDataSetChanged();
//            itemsAdapter.notifyDataSetChanged();
//            writeItemsToFile();
            writeItems();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Todo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewTodoItems = (ListView) findViewById(R.id.listViewTodoItems);
        todoItems = new ArrayList<>();
        bootstrapTodoItems();
        itemsAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toTodoItemNames(todoItems));
        todoAdapter = new TodoAdapter(this, todoItems);
//        listViewTodoItems.setAdapter(itemsAdapter);
        listViewTodoItems.setAdapter(todoAdapter);
        editTextNewItem = (EditText) findViewById(R.id.editTextNewItem);

        listViewTodoItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("on item long clicked at position ... " + position);
                todoItems.remove(position);
                itemsAdapter.notifyDataSetChanged();
//                writeItemsToFile();
                writeItems();
                return true;
            }
        });

        listViewTodoItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("on item clicked at position ........" + position);
                Intent main2EditItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
                main2EditItemIntent.putExtra("todoItem", toTodoItemNames(todoItems).get(position));
                main2EditItemIntent.putExtra("position", position);
//                startActivity(main2EditItemIntent);
                startActivityForResult(main2EditItemIntent, REQEUST_CODE);
            }
        });
    }

    private void bootstrapTodoItems() {
        readItems();
        populatePrioritySpinner();
//        readItemsFromFile();
    }

    private void populatePrioritySpinner() {
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        List<String> priorities = new ArrayList<>();
        priorities.add("P1");
        priorities.add("P2");
        priorities.add("P3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(dataAdapter);
    }

    public void onAddItem(View view) {
        Todo todo = new Todo();
        todo.setTodoName(editTextNewItem.getText().toString());
        todo.setPriority(spinnerPriority.getSelectedItemPosition() + 1);
        todo.setComplete(false);
        todoItems.add(todo);
        todoAdapter.notifyDataSetChanged();
//        itemsAdapter.add(editTextNewItem.getText().toString());
        editTextNewItem.setText("");
//        writeItemsToFile();
        writeItems();
    }


    // ------- Persistence ------------
    private void writeItems() {
        if (toFile) {
            writeItemsToFile();
        } else {
            writeItemsToDB();
        }
    }

    private void readItems() {
        if (toFile) {
            readItemsFromFile();
        } else {
            readItemsFromDB();
        }
    }

    // ------- DB Persistence ------------
    private void writeItemsToDB() {
        TodoDAO todoDAO = new TodoDAO();
        todoDAO.deleteAll();
        for (Todo todoItem : todoItems) {
//            Todo todo = new Todo();
//            todo.setTodoName(todoItem);
            todoDAO.upsert(todoItem);
        }
    }

    private void readItemsFromDB() {
        TodoDAO todoDAO = new TodoDAO();
        List<Todo> allTodos = todoDAO.getAllTodos();
        for (Todo todo : allTodos) {
            todoItems.add(todo);
        }
    }

    // ------ File Persistence ------------
    @NonNull
    private File getTodoFile() {
        File filesDir = getFilesDir();
        return new File(filesDir, TODO_FILE_NAME);
    }

    private void readItemsFromFile() {
        File file = getTodoFile();
        try {
            List<String> fileLines = FileUtils.readLines(file);
            todoItems = deserializeTodoItems(fileLines);
//            todoItems = new ArrayList<>(fileLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Todo> deserializeTodoItems(List<String> fileLines) {
        return null;
    }

    private void writeItemsToFile() {
        File file = getTodoFile();
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDelete(View view) {
        TodoDAO todoDAO = new TodoDAO();
        Todo todoToDelete = (Todo) view.getTag();
        todoDAO.delete(todoToDelete);
        todoItems.remove(todoToDelete);
        todoAdapter.notifyDataSetChanged();
    }

    public void onCalendar(View view) {
        System.out.println("on calendar click ...");
        Todo todoToDelete = (Todo) view.getTag();
        Bundle mainActivity2DatePickerFragmentBundle = new Bundle();
        mainActivity2DatePickerFragmentBundle.putSerializable("TodoItem", todoToDelete);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(mainActivity2DatePickerFragmentBundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSelected(Todo todo, int year, int month, int day) {
        System.out.println("back to main activity with dates ... " + todo + year + month + day);
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.set(year, month, day);
        todo.setTimeInMillis(calendar.getTimeInMillis());
        TodoDAO todoDAO = new TodoDAO();
        todoDAO.upsert(todo);
        todoAdapter.notifyDataSetChanged();
    }
}
