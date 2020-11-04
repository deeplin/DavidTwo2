package com.david.incubator.ui.system.print;

import android.content.Context;
import android.widget.Button;

import com.david.R;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.AlarmEntity;
import com.david.core.database.entity.EcgEntity;
import com.david.core.database.entity.IncubatorEntity;
import com.david.core.database.entity.Spo2Entity;
import com.david.core.enumeration.LanguageEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.FormatUtil;
import com.david.core.util.LoggerUtil;
import com.david.core.util.TimeUtil;
import com.david.core.util.ViewUtil;
import com.david.incubator.serial.print.PrintCommandControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SystemPrintLayout extends BaseLayout {

    private static final String DATE_LINE = "Date: %s\n";
    private static final String INCUBATOR_LINE0 = "Skin:%s℃ Skin2:%s℃ Air:%s℃\n\n";
    private static final String INCUBATOR_LINE1 = "Humidity:%s Oxygen:%s\n";
    private static final String INCUBATOR_LINE2 = "Inc:%s Warmer:%s\n";
    private static final String ENTER = "\n\n\n";

    private static final String ECG_LINE = "Ecg Hr:%s Rr:%s\n\n";

    private static final String SPO2_LINE0 = "Spo2:%s Pr:%s\n\n";
    private static final String SPO2_LINE1 = "Pi:%s Sphb:%s\n";
    private static final String SPO2_LINE2 = "Spoc:%s Spmet:%s\n";
    private static final String SPO2_LINE3 = "Spco:%s Pvi:%s\n";

    @Inject
    PrintCommandControl printCommandControl;
    @Inject
    DaoControl daoControl;

    private final KeyButtonView categorySpinnerView;
    private final KeyButtonView timeSpinnerView;
    private final Button printButton;
    private final List<String> categoryList = new ArrayList<>();
    private final List<String> timeList = new ArrayList<>();

    private int originCategoryOption;
    private int originTimeOption;

    public SystemPrintLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_PRINT);

        categorySpinnerView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(0, categorySpinnerView);
        categorySpinnerView.getValue().setOnClickListener(v -> {
            optionPopupView.init();
            for (int index = 0; index < categoryList.size(); index++) {
                optionPopupView.setOption(index, categoryList.get(index));
            }
            optionPopupView.setSelectedId(originCategoryOption);
            optionPopupView.setCallback(this::categoryCallback);
            optionPopupView.show(this, categorySpinnerView.getId(), true);
        });
        timeSpinnerView = ViewUtil.buildKeyButtonView(getContext());
        addInnerView(1, timeSpinnerView);
        timeSpinnerView.getValue().setOnClickListener(v -> {
            optionPopupView.init();
            for (int index = 0; index < timeList.size(); index++) {
                optionPopupView.setOption(index, timeList.get(index));
            }
            optionPopupView.setSelectedId(originTimeOption);
            optionPopupView.setCallback(this::timeCallback);
            optionPopupView.show(this, timeSpinnerView.getId(), true);
        });

        printButton = ViewUtil.buildButton(getContext());
        printButton.setOnClickListener(v -> {
            startPrint();
        });
        addInnerButton(2, printButton);
    }

    @Override
    public void attach() {
        super.attach();

        originCategoryOption = 0;
        originTimeOption = 0;

        categoryList.add("培养箱");
        categoryList.add("心电");
        categoryList.add("血氧");
        categoryList.add("报警");

        categorySpinnerView.setKeyId(R.string.category);
        categorySpinnerView.setValue(categoryList.get(originCategoryOption));

        timeList.add("1小时");
        timeList.add("2小时");
        timeList.add("8小时");

        timeSpinnerView.setKeyId(R.string.category);
        timeSpinnerView.setValue(timeList.get(originTimeOption));

        printButton.setText(ContextUtil.getString(R.string.start_print));
    }

    @Override
    public void detach() {
        super.detach();
        categoryList.clear();
        timeList.clear();
    }

    private void categoryCallback(int optionId) {
        if (optionId != originCategoryOption) {
            originTimeOption = optionId;
            categorySpinnerView.setValue(LanguageEnum.values()[optionId].toString());
            categorySpinnerView.setSelected(true);
        }
    }

    private void timeCallback(int optionId) {
        if (optionId != originCategoryOption) {
            originTimeOption = optionId;
            categorySpinnerView.setValue(LanguageEnum.values()[optionId].toString());
            categorySpinnerView.setSelected(true);
        }
    }

    private void startPrint() {
        categorySpinnerView.setEnabled(false);
        timeSpinnerView.setEnabled(false);
        printButton.setText(ContextUtil.getString(R.string.stop_print));

        int lastTimeInSecond = 0;
        switch (originTimeOption) {
            case (0):
                lastTimeInSecond = 3600;
                break;
            case (1):
                lastTimeInSecond = 2 * 3600;
                break;
            case (2):
                lastTimeInSecond = 8 * 3600;
        }

        long currentEndTime = TimeUtil.getCurrentTimeInSecond();
        long startTime = currentEndTime - lastTimeInSecond;

        Observable.just(lastTimeInSecond).observeOn(Schedulers.io())
                .subscribe(integer -> {
                    switch (originCategoryOption) {
                        case (0):
                            printIncubator(startTime, currentEndTime);
                            break;
                        case (1):
                            printEcg(startTime, currentEndTime);
                            break;
                        case (2):
                            printSpo2(startTime, currentEndTime);
                            break;
                        case (3):
                            printAlarm(startTime, currentEndTime);
                            break;
                    }
                });

        categorySpinnerView.setEnabled(true);
        timeSpinnerView.setEnabled(true);
    }

//    private void stopPrint() {
//        printButton.setText(ContextUtil.getString(R.string.print));
//    }

    private void printIncubator(long startTime, long endTime) {
        SystemPrintCommand startCommand = new SystemPrintCommand(Integer.MIN_VALUE, ENTER);
        printCommandControl.produce(true, startCommand);

        IncubatorEntity[] incubatorEntities = daoControl.getIncubatorDaoOperation().get(startTime, endTime);
        for (int index = 0; index < incubatorEntities.length; index++) {
            IncubatorEntity incubatorEntity = incubatorEntities[index];
            String dateString = TimeUtil.getTimeFromSecond(incubatorEntity.timeStamp, TimeUtil.FullTime);
            SystemPrintCommand dateCommand = new SystemPrintCommand(index * 10 + 3,
                    String.format(Locale.US, DATE_LINE, dateString));
            printCommandControl.produce(true, dateCommand);

            SystemPrintCommand line2Command = new SystemPrintCommand(index * 10 + 2,
                    String.format(Locale.US, INCUBATOR_LINE2, getHumidityValue(incubatorEntity.inc),
                            getHumidityValue(incubatorEntity.warmer)));
            printCommandControl.produce(true, line2Command);

            SystemPrintCommand line1Command = new SystemPrintCommand(index * 10 + 1,
                    String.format(Locale.US, INCUBATOR_LINE1, getHumidityValue(incubatorEntity.humidity),
                            getHumidityValue(incubatorEntity.oxygen)));
            printCommandControl.produce(true, line1Command);

            SystemPrintCommand line0Command = new SystemPrintCommand(index * 10,
                    String.format(Locale.US, INCUBATOR_LINE0, getTempValue(incubatorEntity.skin1),
                            getTempValue(incubatorEntity.skin2), getTempValue(incubatorEntity.air)));
            printCommandControl.produce(true, line0Command);

            try {
                Thread.sleep(500);
            } catch (Exception e) {
                LoggerUtil.e(e);
            }
        }

        SystemPrintCommand endCommand = new SystemPrintCommand(Integer.MAX_VALUE, ENTER);
        printCommandControl.produce(true, endCommand);
    }

    private void printEcg(long startTime, long endTime) {
        SystemPrintCommand startCommand = new SystemPrintCommand(Integer.MIN_VALUE, ENTER);
        printCommandControl.produce(true, startCommand);

        EcgEntity[] ecgEntities = daoControl.getEcgDaoControl().get(startTime, endTime);
        for (int index = 0; index < ecgEntities.length; index++) {
            EcgEntity ecgEntity = ecgEntities[index];
            String dateString = TimeUtil.getTimeFromSecond(ecgEntity.timeStamp, TimeUtil.FullTime);
            SystemPrintCommand dateCommand = new SystemPrintCommand(index * 10 + 1,
                    String.format(Locale.US, DATE_LINE, dateString));
            printCommandControl.produce(true, dateCommand);

            SystemPrintCommand line0Command = new SystemPrintCommand(index * 10,
                    String.format(Locale.US, ECG_LINE, getValue(ecgEntity.hr, 1, "0", "bpm"),
                            getValue(ecgEntity.rr, 1, "0", "")));
            printCommandControl.produce(true, line0Command);
        }

        SystemPrintCommand endCommand = new SystemPrintCommand(Integer.MAX_VALUE, ENTER);
        printCommandControl.produce(true, endCommand);
    }

    private void printSpo2(long startTime, long endTime) {
        SystemPrintCommand startCommand = new SystemPrintCommand(Integer.MIN_VALUE, ENTER);
        printCommandControl.produce(true, startCommand);

        Spo2Entity[] spo2Entities = daoControl.getSpo2DaoOperation().get(startTime, endTime);
        for (int index = 0; index < spo2Entities.length; index++) {
            Spo2Entity spo2Entity = spo2Entities[index];
            String dateString = TimeUtil.getTimeFromSecond(spo2Entity.timeStamp, TimeUtil.FullTime);
            SystemPrintCommand dateCommand = new SystemPrintCommand(index * 10 + 3,
                    String.format(Locale.US, DATE_LINE, dateString));
            printCommandControl.produce(true, dateCommand);

            SystemPrintCommand line3Command = new SystemPrintCommand(index * 10 + 3,
                    String.format(Locale.US, SPO2_LINE3, getValue(spo2Entity.spco, 10, "0.0", "%"),
                            getValue(spo2Entity.pvi, 1, "0", "%")));
            printCommandControl.produce(true, line3Command);

            SystemPrintCommand line2Command = new SystemPrintCommand(index * 10 + 2,
                    String.format(Locale.US, SPO2_LINE2, getValue(spo2Entity.spoc, 10, "0.0", "mL/dL"),
                            getValue(spo2Entity.spmet, 10, "0.0", "%")));
            printCommandControl.produce(true, line2Command);

            SystemPrintCommand line1Command = new SystemPrintCommand(index * 10 + 2,
                    String.format(Locale.US, SPO2_LINE1, getValue(spo2Entity.pi, 1000, "0.0", "%"),
                            getValue(spo2Entity.sphb, 100, "0.0", "g/dL")));
            printCommandControl.produce(true, line1Command);

            SystemPrintCommand line0Command = new SystemPrintCommand(index * 10 + 2,
                    String.format(Locale.US, SPO2_LINE0, getValue(spo2Entity.spo2, 10, "0", "%"),
                            getValue(spo2Entity.pr, 1, "0.0", "bpm")));
            printCommandControl.produce(true, line0Command);
        }

        SystemPrintCommand endCommand = new SystemPrintCommand(Integer.MAX_VALUE, ENTER);
        printCommandControl.produce(true, endCommand);
    }

    private void printAlarm(long startTime, long endTime) {
        SystemPrintCommand startCommand = new SystemPrintCommand(Integer.MIN_VALUE, ENTER);
        printCommandControl.produce(true, startCommand);

        AlarmEntity[] alarmEntities = daoControl.getAlarmDaoOperation().get(startTime, endTime);
        for (int index = 0; index < alarmEntities.length; index++) {
            AlarmEntity alarmEntity = alarmEntities[index];
            String dateString = TimeUtil.getTimeFromSecond(alarmEntity.timeStamp, TimeUtil.FullTime);
            SystemPrintCommand dateCommand = new SystemPrintCommand(index * 10 + 1,
                    String.format(Locale.US, DATE_LINE, dateString));
            printCommandControl.produce(true, dateCommand);

            SystemPrintCommand line0Command = new SystemPrintCommand(index * 10, alarmEntity.alarmId + "\n");
            printCommandControl.produce(true, line0Command);
        }

        SystemPrintCommand endCommand = new SystemPrintCommand(Integer.MAX_VALUE, ENTER);
        printCommandControl.produce(true, endCommand);
    }

    private String getTempValue(int value) {
        if (value >= 0) {
            return FormatUtil.formatValue(value, 10, "0.0");
        } else {
            return "NA";
        }
    }

    private String getHumidityValue(int value) {
        if (value >= 0) {
            return FormatUtil.formatValue(value, 10, "0") + "%";
        } else {
            return "NA";
        }
    }

    private String getValue(int value, int dominator, String formatString, String unit) {
        if (value >= 0) {
            return FormatUtil.formatValue(value, dominator, formatString) + unit;
        } else {
            return "NA";
        }
    }
}