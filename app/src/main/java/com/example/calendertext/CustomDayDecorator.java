package com.example.calendertext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class CustomDayDecorator implements DayViewDecorator {
    private String thisDateString;
    private double price;
    private Context mainContext;

    public CustomDayDecorator(String thisDateString, double price, Context mainContext) {
        this.thisDateString = thisDateString;
        this.price = price;
        this.mainContext = mainContext;

        // Round the price to 2 decimal places
//        this.price = Math.round(this.price * 100.0) / 100.0;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        int year = day.getYear();
        int month = day.getMonth() + 1; // Month is zero-based
        int dayOfMonth = day.getDay();

        String currentDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, dayOfMonth);

        return currentDate.equals(thisDateString);
    }

    @Override
    public void decorate(DayViewFacade view) {
        CalendarViewSpan cvs = new CalendarViewSpan("₹" + String.valueOf(price), mainContext);
        view.addSpan(cvs);
        view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(mainContext, R.color.colorDates)));
    }
}
