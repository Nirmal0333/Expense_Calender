package com.example.calendertext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expense.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "detail";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_EXPENSE_NAME = "expenseName";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " TEXT," +
                COLUMN_EXPENSE_NAME + " TEXT," +
                COLUMN_AMOUNT + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void insertData(String date, String expenseName, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_EXPENSE_NAME, expenseName);
        values.put(COLUMN_AMOUNT, amount);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public List<EventData> getAllEventData() {
        List<EventData> eventDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_DATE + ", SUM(" + COLUMN_AMOUNT + ") AS total_amount FROM " + TABLE_NAME +
                " GROUP BY " + COLUMN_DATE;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("total_amount"));
            // Assuming there's no expense name associated with the total sum for the date
            EventData eventData = new EventData(date, totalAmount, null);
            eventDataList.add(eventData);
        }

        cursor.close();
        return eventDataList;
    }
    public List<DatabaseHelper.EventData> getDataForDate(String date) {
        List<DatabaseHelper.EventData> eventDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date});

        while (cursor.moveToNext()) {
            String expenseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPENSE_NAME));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
            DatabaseHelper.EventData eventData = new DatabaseHelper.EventData(date, amount, expenseName);
            eventDataList.add(eventData);
        }

        cursor.close();
        return eventDataList;
    }
    public List<EventData> getDataForMonth(int year, int month) {
        List<EventData> eventDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct the query to fetch entries for the specified year and month
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE strftime('%Y', " + COLUMN_DATE + ") = ?" +
                " AND strftime('%m', " + COLUMN_DATE + ") = ?";
        String[] selectionArgs = {String.valueOf(year), String.format(Locale.getDefault(), "%02d", month)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        // Iterate over the cursor to retrieve data
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
            String expenseName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXPENSE_NAME));
            EventData eventData = new EventData(date, amount, expenseName);
            eventDataList.add(eventData);
        }

        cursor.close();
        return eventDataList;
    }
    public static class EventData {
        private String date;
        private double amount;
        private String expenseName;

        public EventData(String date, double amount, String expenseName) {
            this.date = date;
            this.amount = amount;
            this.expenseName = expenseName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getExpenseName() {
            return expenseName;
        }

        public void setExpenseName(String expenseName) {
            this.expenseName = expenseName;
        }


    }
}

