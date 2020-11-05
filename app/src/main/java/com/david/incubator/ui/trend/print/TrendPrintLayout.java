package com.david.incubator.ui.trend.print;

import android.content.Context;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.database.DaoControl;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingLayout;
import com.david.core.ui.model.IntegerButtonModel;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.TimeUtil;
import com.david.core.util.ViewUtil;
import com.david.databinding.LayoutPrintBinding;
import com.david.incubator.ui.trend.TrendControlModel;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

public class TrendPrintLayout extends BindingLayout<LayoutPrintBinding> {

    @Inject
    DaoControl daoControl;
    @Inject
    TrendControlModel trendControlModel;
    @Inject
    SystemModel systemModel;

    private final IntegerButtonModel yearSpinnerModel;
    private final IntegerButtonModel monthSpinnerModel;
    private final IntegerButtonModel daySpinnerModel;
    private final IntegerButtonModel hourSpinnerModel;
    private final IntegerButtonModel minuteSpinnerModel;
    private final IntegerButtonModel secondSpinnerModel;

    private final Button printButton;
    private boolean printing;

    public TrendPrintLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.TREND_PRINT);

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
        hourSpinnerModel = new IntegerButtonModel(binding.hour);
        hourSpinnerModel.setMin(0);
        hourSpinnerModel.setMax(23);
        hourSpinnerModel.setConverter(this::formatValue);
        minuteSpinnerModel = new IntegerButtonModel(binding.minute);
        minuteSpinnerModel.setMin(0);
        minuteSpinnerModel.setMax(59);
        minuteSpinnerModel.setConverter(this::formatValue);
        secondSpinnerModel = new IntegerButtonModel(binding.second);
        secondSpinnerModel.setMin(0);
        secondSpinnerModel.setMax(59);
        secondSpinnerModel.setConverter(this::formatValue);

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

        super.initPopup(1);
        super.setRowId(0, 2);
        super.setPopup(0, ListUtil.monitorIntervalList.toArray(), null, true, null);

        printButton = ViewUtil.buildButton(getContext());
        addInnerButton(3, printButton);

        printButton.setOnClickListener(v -> {
            keyButtonViews[0].getValue().setSelected(false);
            binding.year.setSelected(false);
            binding.month.setSelected(false);
            binding.day.setSelected(false);
            binding.hour.setSelected(false);
            binding.minute.setSelected(false);
            binding.second.setSelected(false);
            keyButtonViews[0].getValue().setEnabled(printing);
            binding.year.setEnabled(printing);
            binding.month.setEnabled(printing);
            binding.day.setEnabled(printing);
            binding.hour.setEnabled(printing);
            binding.minute.setEnabled(printing);
            binding.second.setEnabled(printing);

            if (printing) {
                printing = false;
                printButton.setText(ContextUtil.getString(R.string.start_print));

//                printControl.detach();
//                View view = printControl.getPrintWaveView();
//                super.removeView(view);
            } else {
                printing = true;
                printButton.setText(ContextUtil.getString(R.string.stop_print));
//                printControl.attach(binding.getLifecycleOwner());
//                View view = printControl.getPrintWaveView();
//                view.setVisibility(View.INVISIBLE);
//                super.addView(view);
            }
        });
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        systemModel.disableLock();

        super.setConfig(0, ConfigEnum.DataPrintInterval);
        super.setText(0, R.string.data_print, ListUtil.monitorIntervalList.toArray());
        printButton.setText(ContextUtil.getString(R.string.start_print));
        printing = false;

        reload();
    }

    @Override
    public void detach() {
        super.detach();

        if (printing) {
            printing = false;
//            printControl.detach();
//            View view = printControl.getPrintWaveView();
//            super.removeView(view);
        }
        systemModel.enableLock();
    }

    //todo
//    @Override
//    protected int getPopupWidth() {
//        return 146;
//    }
//
//    @Override
//    protected int getPopupHeight() {
//        return 128;
//    }

    private void reload() {
        numberPopupView.close();

        binding.year.setSelected(false);
        binding.month.setSelected(false);
        binding.day.setSelected(false);
        binding.hour.setSelected(false);
        binding.minute.setSelected(false);
        binding.second.setSelected(false);

        long lastTime = trendControlModel.getLastTime();
        int interval = trendControlModel.getIntervalValue();
        long currentFirstTime = lastTime - interval * trendControlModel.getSamplePoint();

        Timestamp timestamp = new Timestamp(currentFirstTime * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        yearSpinnerModel.setOriginValue(calendar.get(Calendar.YEAR));
        monthSpinnerModel.setOriginValue(calendar.get(Calendar.MONTH) + 1);
        daySpinnerModel.setOriginValue(calendar.get(Calendar.DAY_OF_MONTH));

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
    }

    private String formatValue(int value) {
        return String.format(Locale.US, "%02d", value);
    }
}