package com.ajasuja.codepath.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> todoItems;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listViewTodoItems;
    private EditText editTextNewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewTodoItems = (ListView) findViewById(R.id.listViewTodoItems);
        todoItems = new ArrayList<>();
        itemsAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        listViewTodoItems.setAdapter(itemsAdapter);
        todoItems.add("a");
        todoItems.add("b");
        todoItems.add("c");
        editTextNewItem = (EditText) findViewById(R.id.editTextNewItem);
        listViewTodoItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void onAddItem(View view) {
        itemsAdapter.add(editTextNewItem.getText().toString());
        editTextNewItem.setText("");
    }
}
