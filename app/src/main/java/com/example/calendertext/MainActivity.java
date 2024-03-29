package com.example.calendertext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private MaterialCalendarView calendarView;
    private List<DatabaseHelper.EventData> eventDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        calendarView = findViewById(R.id.calendarView);
        TextView monthlyExpenseTextView = findViewById(R.id.monthlyexpense);

        int minYear = 2024;
        int maxYear = 2024;
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(minYear, 1, 1))
                .setMaximumDate(CalendarDay.from(maxYear, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        TodayDecorator todayDecorator = new TodayDecorator(context);
        calendarView.addDecorator(todayDecorator);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        eventDates = dbHelper.getAllEventData();

        for (DatabaseHelper.EventData event : eventDates) {
            CustomDayDecorator decorator = new CustomDayDecorator(event.getDate(), event.getAmount(), context);
            calendarView.addDecorator(decorator);
        }

//        double totalAmount = dbHelper.getTotalAmount();

//        TextView priceDisplayTextView = findViewById(R.id.priceDisplay);
//        priceDisplayTextView.setText("Total: ₹" + totalAmount);


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                SecondActivity(date);
            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                // Retrieve the selected year and month
                int selectedYear = date.getYear();
                int selectedMonth = date.getMonth() + 1; // Month is zero-based, so add 1

                // Retrieve data for the selected month from the database
                List<DatabaseHelper.EventData> dataForSelectedMonth = dbHelper.getDataForMonth(selectedYear, selectedMonth);

                // Calculate the total expense for the selected month
                double totalExpense = 0.0;
                for (DatabaseHelper.EventData eventData : dataForSelectedMonth) {
                    totalExpense += eventData.getAmount();
                }

                // Update the monthly expense TextView with the total expense for the selected month
                monthlyExpenseTextView.setText(getString(R.string.monthly_expense, totalExpense));
            }
        });

    }
    private void SecondActivity(CalendarDay date) {
        String selectedDateString = formatDate(date);
        if (selectedDateString != null && !selectedDateString.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(DateDetailsActivity.SELECTED_DATE, selectedDateString);
            startActivity(intent);
        } else {
            Toast.makeText(context, "Error converting date format", Toast.LENGTH_SHORT).show();
        }
    }
    private String formatDate(CalendarDay date) {
        int year = date.getYear();
        int month = date.getMonth() + 1; // Month is zero-based, so we add 1
        int day = date.getDay();

        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
    }
}
