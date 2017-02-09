package com.ajasuja.codepath.todo;

import android.content.Intent;
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
    private static final int REQEUST_CODE = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQEUST_CODE && resultCode == RESULT_OK) {
            System.out.println("passed to parent activity successfully ...... ");
            String todoItemUpdated = data.getStringExtra("todoItemUpdated");
            int position = data.getIntExtra("position", 0);
            System.out.println("on Main activity ... " + todoItemUpdated + " at position ... " + position);
            todoItems.set(position, todoItemUpdated);
            itemsAdapter.notifyDataSetChanged();
            writeItemsToFile();
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
        itemsAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        listViewTodoItems.setAdapter(itemsAdapter);
        editTextNewItem = (EditText) findViewById(R.id.editTextNewItem);
        listViewTodoItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("on item long clicked at position ... " + position);
                todoItems.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItemsToFile();
                return true;
            }
        });

        listViewTodoItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("on item clicked at position ........" + position);
                Intent main2EditItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
                main2EditItemIntent.putExtra("todoItem", todoItems.get(position));
                main2EditItemIntent.putExtra("position", position);
//                startActivity(main2EditItemIntent);
                startActivityForResult(main2EditItemIntent, REQEUST_CODE);
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
