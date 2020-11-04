package com.david.core.ui.alarm;

import android.content.Context;
import android.widget.ScrollView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.david.R;
import com.david.core.alarm.AlarmControl;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutAlarmListBinding;

import javax.inject.Inject;

public class PhysiologicalAlarmListLayout extends BindingBasicLayout<LayoutAlarmListBinding> {

    @Inject
    AlarmControl alarmControl;
    @Inject
    AlarmAdapter alarmAdapter;

    private final RecyclerView recyclerView;

    private final Observer<Boolean> updateAlarmObserver;

    public PhysiologicalAlarmListLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        alarmAdapter.setAlarmList(alarmControl.getPhysiologicalAll());

        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(alarmAdapter);

        final ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(recyclerView);
        binding.alarmLayout.addView(scrollView);

        updateAlarmObserver = aBoolean -> {
            if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!recyclerView.isComputingLayout())) {
                alarmAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_alarm_list;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        alarmControl.physiologicalUpdate.observeForever(updateAlarmObserver);
        binding.titleView.set(R.string.physiological_alarm, null, true);
    }

    @Override
    public void detach() {
        super.detach();
        alarmControl.physiologicalUpdate.removeObserver(updateAlarmObserver);
    }
}