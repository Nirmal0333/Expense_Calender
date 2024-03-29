package com.example.calendertext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

import androidx.core.content.ContextCompat;

public class CalendarViewSpan implements LineBackgroundSpan {
    private String text;
    private Context context;

    public CalendarViewSpan(String text, Context context) {
        this.text = text;
        this.context = context;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom,
                               CharSequence text, int start, int end, int lnum) {
        text = this.text;

        switch(text.length()) {
            case 4: text = "   "+text;
                break;
            case 5: text = "  "+text;
                break;
            case 6: text = " "+text;
        }

        // Set text size and style
         // Set the text size to 50 for all dates
        p.setTypeface(Typeface.DEFAULT_BOLD); // Set the text style to bold

        int color = ContextCompat.getColor(context, R.color.colorText);
        p.setColor(color);
        c.drawText(String.valueOf(text), (start+end)/2, bottom + 25, p);
    }
}
