package com.ajasuja.codepath.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText editTextTodoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Edit Todo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String todoItem = getIntent().getStringExtra("todoItem");
        System.out.println("passed ... " + todoItem);

        editTextTodoItem = (EditText) findViewById(R.id.editTextTodoItem);
        editTextTodoItem.setText(todoItem);
        editTextTodoItem.setSelection(todoItem.length());
    }

    public void onSave(View view) {
        System.out.println("on save button pressed ... ");
        Intent editItem2MainIntent = new Intent();
        editItem2MainIntent.putExtra("todoItemUpdated", editTextTodoItem.getText().toString());
        editItem2MainIntent.putExtra("position", getIntent().getIntExtra("position", 0));
        setResult(RESULT_OK, editItem2MainIntent);
        finish();
    }
}
