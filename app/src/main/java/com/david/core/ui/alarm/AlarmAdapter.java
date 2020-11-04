package com.david.core.ui.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.david.R;
import com.david.core.alarm.AlarmModel;
import com.david.core.alarm.AlarmUtil;
import com.david.core.util.TimeUtil;

import java.util.List;

import javax.inject.Inject;

public class AlarmAdapter extends RecyclerView.Adapter {

    private List<AlarmModel> alarmList;

    @Inject
    public AlarmAdapter() {
    }

    public void setAlarmList(List<AlarmModel> alarmList) {
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        AlarmModel alarmModel = alarmList.get(position);
        AlarmViewHolder holder = (AlarmViewHolder) viewHolder;
        if (alarmModel != null) {
            holder.alarmTime.setText(TimeUtil.getTimeFromSecond(alarmModel.getStartTime(), TimeUtil.FullTime));
            holder.alarmString.setText(AlarmUtil.getAlarmString(alarmModel.getAlarmWord()));
        }
        int backgroundColor = AlarmUtil.getAlarmColor(alarmModel.getAlarmPriorityEnum());
        holder.alarmTime.setBackgroundResource(backgroundColor);
        holder.alarmString.setBackgroundResource(backgroundColor);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}