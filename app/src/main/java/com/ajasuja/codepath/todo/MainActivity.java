package com.ajasuja.codepath.todo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> todoItems;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listViewTodoItems;
    private EditText editTextNewItem;

    private static final String TODO_FILE_NAME =  "todo.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewTodoItems = (ListView) findViewById(R.id.listViewTodoItems);
        todoItems = new ArrayList<>();
        bootstrapTodoItems();
        itemsAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        listViewTodoItems.setAdapter(itemsAdapter);
        editTextNewItem = (EditText) findViewById(R.id.editTextNewItem);
        listViewTodoItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItemsToFile();
                return true;
            }
        });
    }

    private void bootstrapTodoItems() {
        readItemsFromFile();
    }

    public void onAddItem(View view) {
        itemsAdapter.add(editTextNewItem.getText().toString());
        editTextNewItem.setText("");
        writeItemsToFile();
    }

    private void readItemsFromFile() {
        File file = getTodoFile();
        try {
            List<String> fileLines = FileUtils.readLines(file);
            todoItems = new ArrayList<>(fileLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private File getTodoFile() {
        File filesDir = getFilesDir();
        return new File(filesDir, TODO_FILE_NAME);
    }

    private void writeItemsToFile() {
        File file = getTodoFile();
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
