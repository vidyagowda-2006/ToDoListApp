package com.example.myapplication;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText editTask;
    Button btnAdd;
    RecyclerView recyclerView;

    ArrayList<Task> taskList;
    TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editTask = findViewById(R.id.editTask);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);

        loadTasks();
        adapter = new TaskAdapter(taskList, this::saveTasks,
                position -> {
                    taskList.remove(position);
                    saveTasks();
                    adapter.notifyDataSetChanged();
                });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String task = editTask.getText().toString();

            if (!task.isEmpty()) {
                taskList.add(new Task(task, false));
                saveTasks();
                adapter.notifyDataSetChanged();
                editTask.setText("");
            }
        });


    }
    private void saveTasks() {
        SharedPreferences prefs = getSharedPreferences("todo_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(taskList);

        editor.putString("tasks", json);
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences prefs = getSharedPreferences("todo_prefs", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString("tasks", null);

        if (json != null) {
            java.lang.reflect.Type type =
                    new TypeToken<ArrayList<Task>>() {}.getType();

            taskList = gson.fromJson(json, type);
        } else {
            taskList = new ArrayList<>();
        }
    }
}