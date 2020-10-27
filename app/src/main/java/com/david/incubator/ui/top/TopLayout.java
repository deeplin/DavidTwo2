package com.david.incubator.ui.top;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.alarm.AlarmControl;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutTopBinding;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/26 20:40
 * email: 10525677@qq.com
 * description:
 */
public class TopLayout extends BindingBasicLayout<LayoutTopBinding> {

    @Inject
    TopViewModel topViewModel;
    @Inject
    AlarmControl alarmControl;
    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorModel incubatorModel;

    public TopLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        binding.setViewModel(topViewModel);
        binding.setIncubatorModel(incubatorModel);

        binding.physiologicalAlarm.setOnClickListener(v -> {
            if (alarmControl.isAlarm()) {
                systemModel.showLayout(LayoutPageEnum.ALARM_LIST_PHYSIOLOGICAL);
            }
        });

        binding.technicalAlarm.setOnClickListener(v -> {
            if (alarmControl.isAlarm()) {
                systemModel.showLayout(LayoutPageEnum.ALARM_LIST_TECHNICAL);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_top;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        topViewModel.attach();
        binding.rootView.removeView(binding.firstSurfaceView);
    }

    @Override
    public void detach() {
        super.detach();
        topViewModel.detach();
    }
}