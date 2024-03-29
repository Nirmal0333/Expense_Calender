package com.example.calendertext;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class DateDetailsActivity extends AppCompatActivity {
    public static final String SELECTED_DATE = "selected_date";
    private EditText expenseEdittext, amountEdittext;
    private Button saveButton;
    final DatabaseHelper dbHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_date_details);

        amountEdittext = findViewById(R.id.amountEditText);
        expenseEdittext = findViewById(R.id.expenseEditText);
        Spinner frequency = findViewById(R.id.frequencySpinner);

        String[] frequencies = {"Select Frequency", "One Time", "Monthly"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frequencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequency.setAdapter(adapter);

        saveButton = findViewById(R.id.saveButton);

        String selectedDate = getIntent().getStringExtra(SELECTED_DATE);
        Toast.makeText(this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();

        saveButton.setOnClickListener(v -> {
            String amountText = amountEdittext.getText().toString().trim();
            String expenseName = expenseEdittext.getText().toString().trim(); // Assuming expenseEdittext is the EditText for expense name
            String selectedFrequency = frequency.getSelectedItem().toString();

            if (!amountText.isEmpty() && !selectedFrequency.equals("Select Frequency")) {
                double amount = Double.parseDouble(amountText);
                dbHelper.insertData(selectedDate, expenseName, amount); // Pass expense name to insertData method
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                // If frequency is monthly, add data for subsequent months until December
                if (selectedFrequency.equals("Monthly")) {
                    int month = Integer.parseInt(selectedDate.substring(5, 7));
                    int year = Integer.parseInt(selectedDate.substring(0, 4));

                    for (int i = month + 1; i <= 12+1; i++) {
                        String nextMonthDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, i, Integer.parseInt(selectedDate.substring(8, 10)));
                        dbHelper.insertData(nextMonthDate, expenseName, amount);
                    }
                }

                Intent intent = new Intent(DateDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please enter an amount and select a frequency", Toast.LENGTH_SHORT).show();
            }
        });


    }
}