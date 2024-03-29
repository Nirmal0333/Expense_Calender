package com.example.calendertext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SecondActivity extends AppCompatActivity {
    FloatingActionButton button;
    RecyclerView recyclerView;
    MyAdapter adapter;
    List<DatabaseHelper.EventData> eventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        button = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);

        // Set layout manager for RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch data from the database using DatabaseHelper
        fetchDataFromDatabase();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = getIntent().getStringExtra(DateDetailsActivity.SELECTED_DATE);
                if (selectedDate != null && !selectedDate.isEmpty()) {
                    // Pass the selected date to the DateDetailsActivity
                    Intent intent = new Intent(SecondActivity.this, DateDetailsActivity.class);
                    intent.putExtra(DateDetailsActivity.SELECTED_DATE, selectedDate);
                    startActivity(intent);
                } else {
                    Toast.makeText(SecondActivity.this, "Error: Selected date is null or empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchDataFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String selectedDate = getIntent().getStringExtra(DateDetailsActivity.SELECTED_DATE);
        if (selectedDate != null && !selectedDate.isEmpty()) {
            eventDataList = dbHelper.getDataForDate(selectedDate);
            // Create an adapter and attach it to the RecyclerView
            adapter = new MyAdapter(this, eventDataList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(SecondActivity.this, "Error: Selected date is null or empty", Toast.LENGTH_SHORT).show();
        }
    }

}
