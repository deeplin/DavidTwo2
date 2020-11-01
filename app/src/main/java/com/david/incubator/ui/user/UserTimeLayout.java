package com.david.incubator.ui.user;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ComponentControl;
import com.david.core.database.DaoControl;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.component.NumberPopupView;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.ui.model.IntegerButtonModel;
import com.david.core.util.ContextUtil;
import com.david.core.util.LoggerUtil;
import com.david.core.util.TimeUtil;
import com.david.databinding.LayoutUserTimeBinding;

import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

public class UserTimeLayout extends BindingBasicLayout<LayoutUserTimeBinding> {

    @Inject
    ComponentControl componentControl;
    @Inject
    DaoControl daoControl;

    private final NumberPopupView numberPopupView;

    private final IntegerButtonModel yearSpinnerModel;
    private final IntegerButtonModel monthSpinnerModel;
    private final IntegerButtonModel daySpinnerModel;
    private final IntegerButtonModel hourSpinnerModel;
    private final IntegerButtonModel minuteSpinnerModel;
    private final IntegerButtonModel secondSpinnerModel;

    public UserTimeLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        numberPopupView = componentControl.getNumberPopupView();

        yearSpinnerModel = new IntegerButtonModel(binding.year);
        yearSpinnerModel.setMax(1950);
        yearSpinnerModel.setMax(2050);
        yearSpinnerModel.setConverter(String::valueOf);
        yearSpinnerModel.setCallback(this::checkDay);
        monthSpinnerModel = new IntegerButtonModel(binding.month);
        monthSpinnerModel.setMin(1);
        monthSpinnerModel.setMax(12);
        monthSpinnerModel.setConverter(this::formatValue);
        monthSpinnerModel.setCallback(this::checkDay);
        daySpinnerModel = new IntegerButtonModel(binding.day);
        daySpinnerModel.setMin(1);
        daySpinnerModel.setMax(31);
        daySpinnerModel.setConverter(this::formatValue);
        daySpinnerModel.setCallback(this::enableFunction);
        hourSpinnerModel = new IntegerButtonModel(binding.hour);
        hourSpinnerModel.setMin(0);
        hourSpinnerModel.setMax(23);
        hourSpinnerModel.setConverter(this::formatValue);
        hourSpinnerModel.setCallback(this::enableFunction);
        minuteSpinnerModel = new IntegerButtonModel(binding.minute);
        minuteSpinnerModel.setMin(0);
        minuteSpinnerModel.setMax(59);
        minuteSpinnerModel.setConverter(this::formatValue);
        minuteSpinnerModel.setCallback(this::enableFunction);
        secondSpinnerModel = new IntegerButtonModel(binding.second);
        secondSpinnerModel.setMin(0);
        secondSpinnerModel.setMax(59);
        secondSpinnerModel.setConverter(this::formatValue);
        secondSpinnerModel.setCallback(this::enableFunction);

        binding.year.setOnClickListener(v -> {
            numberPopupView.set(yearSpinnerModel);
            numberPopupView.show(binding.rootView, R.id.year, true);
        });

        binding.month.setOnClickListener(v -> {
            numberPopupView.set(monthSpinnerModel);
            numberPopupView.show(binding.rootView, R.id.month, true);
        });

        binding.day.setOnClickListener(v -> {
            numberPopupView.set(daySpinnerModel);
            numberPopupView.show(binding.rootView, R.id.day, true);
        });

        binding.hour.setOnClickListener(v -> {
            numberPopupView.set(hourSpinnerModel);
            numberPopupView.show(binding.rootView, R.id.hour, true);
        });

        binding.minute.setOnClickListener(v -> {
            numberPopupView.set(minuteSpinnerModel);
            numberPopupView.show(binding.rootView, R.id.minute, true);
        });

        binding.second.setOnClickListener(v -> {
            numberPopupView.set(secondSpinnerModel);
            numberPopupView.show(binding.rootView, R.id.second, true);
        });

        binding.cancel.setOnClickListener(v -> reload());

        binding.confirm.setOnClickListener(v -> {
            setTime();
            daoControl.deleteTables();
            reload();
        });

        binding.getRoot().setOnClickListener(view -> numberPopupView.close());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_time;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);

        binding.titleView.set(R.string.time, LayoutPageEnum.USER_HOME, true);

        binding.rootView.addView(numberPopupView, 146, 128);
        numberPopupView.close();

        reload();
    }

    @Override
    public void detach() {
        super.detach();
        numberPopupView.close();
        binding.rootView.removeView(numberPopupView);
    }

    private void reload() {
        numberPopupView.close();

        binding.warning.setVisibility(View.INVISIBLE);
        binding.cancel.setEnabled(false);
        binding.cancel.setImageResource(R.mipmap.cancel_disabled);
        binding.confirm.setEnabled(false);
        binding.confirm.setImageResource(R.mipmap.confirm_disabled);

        binding.year.setSelected(false);
        binding.month.setSelected(false);
        binding.day.setSelected(false);
        binding.hour.setSelected(false);
        binding.minute.setSelected(false);
        binding.second.setSelected(false);

        setCurrentDate();
        setCurrentTime();
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        yearSpinnerModel.setOriginValue(calendar.get(Calendar.YEAR));
        monthSpinnerModel.setOriginValue(calendar.get(Calendar.MONTH) + 1);
        daySpinnerModel.setOriginValue(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        hourSpinnerModel.setOriginValue(calendar.get(Calendar.HOUR_OF_DAY));
        minuteSpinnerModel.setOriginValue(calendar.get(Calendar.MINUTE));
        secondSpinnerModel.setOriginValue(calendar.get(Calendar.SECOND));
    }

    private int getDaysByYearMonth() {
        return TimeUtil.getDaysByYearMonth(yearSpinnerModel.getOriginValue(), monthSpinnerModel.getOriginValue());
    }

    private void checkDay(Integer value) {
        int maxDay = getDaysByYearMonth();
        daySpinnerModel.setMax(maxDay);
        if (daySpinnerModel.getOriginValue() > maxDay) {
            daySpinnerModel.setOriginValue(maxDay);
        }
        enableFunction(0);
    }

    private void enableFunction(Integer value) {
        binding.confirm.setEnabled(true);
        binding.confirm.setImageResource(R.mipmap.confirm);
        binding.cancel.setEnabled(true);
        binding.cancel.setImageResource(R.mipmap.cancel);
        binding.warning.setVisibility(View.VISIBLE);
    }

    private boolean setTime() {
        //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
        String dateTime = String.format(Locale.US, "%04d%02d%02d.%02d%02d%02d",
                yearSpinnerModel.getOriginValue(), monthSpinnerModel.getOriginValue(), daySpinnerModel.getOriginValue(),
                hourSpinnerModel.getOriginValue(), minuteSpinnerModel.getOriginValue(), secondSpinnerModel.getOriginValue());
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes("setprop persist.sys.timezone GMT\n");
            dataOutputStream.writeBytes("/system/bin/date -s " + dateTime + "\n");
            dataOutputStream.writeBytes("clock -w\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            dataOutputStream.close();
            Thread.sleep(200);
            return true;
        } catch (Exception e) {
            LoggerUtil.e(e);
            return false;
        }
    }

    private String formatValue(int value) {
        return String.format(Locale.US, "%02d", value);
    }
}