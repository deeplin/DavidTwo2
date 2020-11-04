package com.david.core.ui.alarm;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.david.R;

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    TextView alarmTime;
    TextView alarmString;

    public AlarmViewHolder(View itemView) {
        super(itemView);
        alarmTime = itemView.findViewById(R.id.alarmTime);
        alarmString = itemView.findViewById(R.id.alarmString);
    }
}