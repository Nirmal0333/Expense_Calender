package com.example.calendertext;

public class DataItem {
    private String amount,expense,date;

    public DataItem(String amount, String expense, String date) {
        this.amount = amount;
        this.expense = expense;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
