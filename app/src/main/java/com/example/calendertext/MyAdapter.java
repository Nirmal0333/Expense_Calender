package com.example.calendertext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<DatabaseHelper.EventData> eventDataList;
    private Context context;

    public MyAdapter(Context context, List<DatabaseHelper.EventData> eventDataList) {
        this.context = context;
        this.eventDataList = eventDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatabaseHelper.EventData eventData = eventDataList.get(position);
        holder.bind(eventData);
    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textamt, textexpense, textdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textamt = itemView.findViewById(R.id.amountTextView);
            textexpense = itemView.findViewById(R.id.expenseTextView);
            textdate = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(DatabaseHelper.EventData eventData) {
            textamt.setText(String.valueOf(eventData.getAmount()));
            textexpense.setText(eventData.getExpenseName());
            textdate.setText(eventData.getDate());
        }
    }
}
