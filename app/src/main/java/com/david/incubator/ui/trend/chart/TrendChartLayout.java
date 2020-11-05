package com.david.incubator.ui.trend.chart;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ComponentControl;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.BaseEntity;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.ui.component.OptionPopupView;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.ui.view.ChartView;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.FormatUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.TimeUtil;
import com.david.core.util.rely.RangeUtil;
import com.david.databinding.LayoutTrendChartBinding;
import com.david.incubator.ui.trend.TrendCategoryEnum;
import com.david.incubator.ui.trend.TrendControlModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TrendChartLayout extends BindingBasicLayout<LayoutTrendChartBinding> {

    private static final int POINT_SUM = 120;

    @Inject
    SystemModel systemModel;
    @Inject
    TrendControlModel trendControlModel;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    DaoControl daoControl;
    @Inject
    ComponentControl componentControl;
    @Inject
    ConfigRepository configRepository;
    @Inject
    SensorModelRepository sensorModelRepository;

    private final OptionPopupView optionPopupView;
    private final int[][] destArray;

    private final ConstraintSet constraintSet;
    private volatile Disposable increaseDisposable;
    private volatile Disposable decreaseDisposable;

    public TrendChartLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        destArray = new int[3][POINT_SUM];
        constraintSet = new ConstraintSet();

        optionPopupView = componentControl.getOptionPopupView();

        binding.selectCategory.setOnClickListener(v -> {
            optionPopupView.close();
            optionPopupView.init();
            for (TrendCategoryEnum trendCategoryEnum : TrendCategoryEnum.values()) {
                optionPopupView.setOption(trendCategoryEnum.ordinal(), trendCategoryEnum.getDisplay());
            }
            optionPopupView.setSelectedId(trendControlModel.trendCategory);
            optionPopupView.setCallback(this::categoryCallback);
            optionPopupView.show(binding.rootView, R.id.selectCategory, false);
        });

        binding.selectInterval.setOnClickListener(v -> {
            optionPopupView.close();
            optionPopupView.init();
            int index = 0;
            if (trendControlModel.chooseIncubator()) {
                for (String text : ListUtil.incubatorIntervalList) {
                    optionPopupView.setOption(index++, text);
                }
            } else {
                for (String text : ListUtil.monitorIntervalList) {
                    optionPopupView.setOption(index++, text);
                }
            }
            optionPopupView.setSelectedId(trendControlModel.getIntervalId());
            optionPopupView.setCallback(this::intervalCallback);
            optionPopupView.show(binding.rootView, R.id.selectInterval, false);
        });

        binding.buttonValueView0.getButton().setOnClickListener(v -> {
            optionPopupView.close();
            optionPopupView.init();
            fillButtonValue(trendControlModel.getItemOption(0).getValue(),
                    trendControlModel.getItemOption(1).getValue(),
                    trendControlModel.getItemOption(2).getValue());

            optionPopupView.setSelectedId(trendControlModel.getItemOption(0));
            optionPopupView.setCallback(this::option0Callback);
            optionPopupView.show(binding.rootView, R.id.buttonValueView0, true);
        });

        binding.buttonValueView1.getButton().setOnClickListener(v -> {
            optionPopupView.close();
            optionPopupView.init();
            fillButtonValue(trendControlModel.getItemOption(0).getValue(),
                    trendControlModel.getItemOption(1).getValue(),
                    trendControlModel.getItemOption(2).getValue());

            optionPopupView.setSelectedId(trendControlModel.getItemOption(1));
            optionPopupView.setCallback(this::option1Callback);
            optionPopupView.show(binding.rootView, R.id.buttonValueView1, true);
        });

        binding.buttonValueView2.getButton().setOnClickListener(v -> {
            optionPopupView.close();
            optionPopupView.init();
            fillButtonValue(trendControlModel.getItemOption(0).getValue(),
                    trendControlModel.getItemOption(1).getValue(),
                    trendControlModel.getItemOption(2).getValue());

            optionPopupView.setSelectedId(trendControlModel.getItemOption(2));
            optionPopupView.setCallback(this::option2Callback);
            optionPopupView.show(binding.rootView, R.id.buttonValueView2, false);
        });

        binding.previousPage.setOnClickListener(v -> {
            long lastTime = trendControlModel.getLastTime();
            int interval = trendControlModel.getIntervalValue();
            trendControlModel.setLastTime(lastTime - interval * Constant.DOT_PER_CHART);

            setXAxis(binding.chartView0);
            setXAxis(binding.chartView1);
            setXAxis(binding.chartView2);

            refreshChart0();
            refreshChart1();
            refreshChart2();

            displayNextPageButton();
        });

        binding.nextPage.setOnClickListener(v -> {
            long lastTime = trendControlModel.getLastTime();
            int interval = trendControlModel.getIntervalValue();
            trendControlModel.setLastTime(lastTime + interval * Constant.DOT_PER_CHART);

            setXAxis(binding.chartView0);
            setXAxis(binding.chartView1);
            setXAxis(binding.chartView2);

            refreshChart0();
            refreshChart1();
            refreshChart2();

            displayNextPageButton();
        });

        binding.trendSwitch.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.TREND_TABLE));

        binding.moveLeft.setOnTouchListener(this::leftTouch);
        binding.moveRight.setOnTouchListener(this::rightTouch);

        binding.getRoot().setOnClickListener(view -> optionPopupView.close());

        //Setup Chart
        binding.chartView0.setPadding(50, 5, 20, 30);
        binding.chartView1.setPadding(50, 5, 20, 30);
        binding.chartView2.setPadding(50, 5, 20, 30);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_trend_chart;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        binding.rootView.addView(optionPopupView, 120, ViewGroup.LayoutParams.WRAP_CONTENT);
        optionPopupView.close();

        if (moduleHardware.isActive(ModuleEnum.Spo2) || moduleHardware.isActive(ModuleEnum.Ecg)
                || moduleHardware.isActive(ModuleEnum.Co2)) {
            binding.selectCategory.setText(trendControlModel.getTrendCategoryEnum().getDisplay());
            binding.selectCategory.setVisibility(View.VISIBLE);
        } else {
            binding.selectCategory.setVisibility(View.INVISIBLE);
        }

        binding.chartView0.setReady(true);
        binding.chartView1.setReady(true);
        binding.chartView2.setReady(true);
        categoryCallback(0);
    }

    @Override
    public void detach() {
        super.detach();
        binding.chartView0.setReady(false);
        binding.chartView1.setReady(false);
        binding.chartView2.setReady(false);

        binding.rootView.removeView(optionPopupView);
        optionPopupView.close();
    }

    private void categoryCallback(Integer obj) {

        TrendCategoryEnum trendCategoryEnum = trendControlModel.getTrendCategoryEnum();

        binding.selectCategory.setText(trendCategoryEnum.getDisplay());

        setSensorOptions(0, binding.buttonValueView0.getButton(), binding.chartView0);
        setSensorOptions(1, binding.buttonValueView1.getButton(), binding.chartView1);

        switch (trendCategoryEnum) {
            case Incubator:
                if (trendControlModel.activeIncubatorItemList.size() > 3) {
                    binding.buttonValueView0.getButton().setEnabled(true);
                    binding.buttonValueView1.getButton().setEnabled(true);
                    binding.buttonValueView2.getButton().setEnabled(true);
                } else {
                    binding.buttonValueView0.getButton().setEnabled(false);
                    binding.buttonValueView1.getButton().setEnabled(false);
                    binding.buttonValueView2.getButton().setEnabled(false);
                }
                binding.buttonValueView2.setVisibility(View.VISIBLE);
                binding.chartView2.setVisibility(View.VISIBLE);
                setSensorOptions(2, binding.buttonValueView2.getButton(), binding.chartView2);
                break;
            case Spo2:
                if (configRepository.getActiveSpo2Module().size() > 0) {
                    binding.buttonValueView0.getButton().setEnabled(true);
                    binding.buttonValueView1.getButton().setEnabled(true);
                    binding.buttonValueView2.getButton().setEnabled(true);
                } else {
                    binding.buttonValueView0.getButton().setEnabled(false);
                    binding.buttonValueView1.getButton().setEnabled(false);
                    binding.buttonValueView2.getButton().setEnabled(false);
                }
                binding.buttonValueView0.getButton().setEnabled(true);
                binding.buttonValueView1.getButton().setEnabled(true);
                binding.buttonValueView2.getButton().setEnabled(true);
                binding.buttonValueView2.setVisibility(View.VISIBLE);
                binding.chartView2.setVisibility(View.VISIBLE);
                setSensorOptions(2, binding.buttonValueView2.getButton(), binding.chartView2);
                break;
            case Ecg:
                binding.buttonValueView0.getButton().setEnabled(false);
                binding.buttonValueView1.getButton().setEnabled(false);
                binding.buttonValueView2.setVisibility(View.GONE);
                binding.chartView2.setVisibility(View.GONE);
                break;
            case Co2:
                binding.buttonValueView0.getButton().setEnabled(false);
                binding.buttonValueView1.getButton().setEnabled(false);
                binding.buttonValueView2.getButton().setEnabled(false);
                binding.buttonValueView2.setVisibility(View.VISIBLE);
                binding.chartView2.setVisibility(View.VISIBLE);
                setSensorOptions(2, binding.buttonValueView2.getButton(), binding.chartView2);
                break;
        }
        intervalCallback(0);
    }

    private void intervalCallback(Integer obj) {
        trendControlModel.initLastTime();

        displayNextPageButton();
        displaySamplePoint();

        if (trendControlModel.chooseIncubator()) {
            binding.selectInterval.setText(ListUtil.incubatorIntervalList.get(trendControlModel.getIntervalId().getValue()));
        } else {
            binding.selectInterval.setText(ListUtil.monitorIntervalList.get(trendControlModel.getIntervalId().getValue()));
        }

        binding.chartView0.setUnit(trendControlModel.getItemSensorModel(0).getUnit());
        binding.chartView1.setUnit(trendControlModel.getItemSensorModel(1).getUnit());
        binding.chartView2.setUnit(trendControlModel.getItemSensorModel(2).getUnit());

        setXAxis(binding.chartView0);
        setXAxis(binding.chartView1);
        setXAxis(binding.chartView2);

        refreshChart0();
        refreshChart1();
        refreshChart2();

        loadSamplePoint();
    }

    private void setSensorOptions(int itemId, Button button, ChartView chartView) {
        SensorModelEnum sensorModelEnum = trendControlModel.getItemSensorModel(itemId);
        button.setText(ContextUtil.getString(sensorModelEnum.getDisplayNameId()));
        chartView.initDataSet(1, sensorModelEnum, FormatUtil::formatBasicValue);
    }

    private void fillButtonValue(int existingId0, int existingId1, int existingId2) {
        if (trendControlModel.chooseIncubator()) {
            for (int index = 0; index < trendControlModel.activeIncubatorItemList.size(); index++) {
                if (index != existingId0 && index != existingId1 && index != existingId2) {
                    optionPopupView.setOption(index, ContextUtil.getString(trendControlModel.activeIncubatorItemList.get(index).getDisplayNameId()));
                }
            }
        } else {
            int id = 0;
            if (existingId0 != 0 && existingId1 != 0 && existingId2 != 0) {
                optionPopupView.setOption(id++, ContextUtil.getString(SensorModelEnum.Spo2.getDisplayNameId()));
            }
            if (existingId0 != 1 && existingId1 != 1 && existingId2 != 1) {
                optionPopupView.setOption(id++, ContextUtil.getString(SensorModelEnum.Pr.getDisplayNameId()));
            }
            if (existingId0 != 2 && existingId1 != 2 && existingId2 != 2) {
                optionPopupView.setOption(id++, ContextUtil.getString(SensorModelEnum.Pi.getDisplayNameId()));
            }

            for (int index = 0; index < configRepository.getActiveSpo2Module().size(); index++) {
                int offset = configRepository.getActiveSpo2Module().get(index);
                SensorModelEnum sensorModelEnum = SensorModelEnum.values()[SensorModelEnum.Sphb.ordinal() + offset];
                if (index != existingId0 && index != existingId1 && index != existingId2) {
                    optionPopupView.setOption(index, ContextUtil.getString(sensorModelEnum.getDisplayNameId()));
                }
            }
        }
    }

    private void option0Callback(Integer optionId) {
        binding.chartView0.setUnit(trendControlModel.getItemSensorModel(0).getUnit());
        setSensorOptions(0, binding.buttonValueView0.getButton(), binding.chartView0);
        refreshChart0();
        binding.chartView0.invalidate();
    }

    private void option1Callback(Integer optionId) {
        binding.chartView1.setUnit(trendControlModel.getItemSensorModel(1).getUnit());
        setSensorOptions(1, binding.buttonValueView1.getButton(), binding.chartView1);
        refreshChart1();

        binding.chartView1.invalidate();
    }

    private void option2Callback(Integer optionId) {
        binding.chartView2.setUnit(trendControlModel.getItemSensorModel(2).getUnit());
        setSensorOptions(2, binding.buttonValueView2.getButton(), binding.chartView2);
        refreshChart2();
        binding.chartView2.invalidate();
    }

    private void setXAxis(ChartView chartView) {
        chartView.setXAxis(trendControlModel.getLastTime(), trendControlModel.getIntervalValue());
    }

    private void refreshChart0() {
        Observable.just(this).observeOn(Schedulers.io())
                .subscribe(trendTableLayout -> {
                    addDataSet(binding.chartView0, trendControlModel.getItemSensorModel(0), destArray[0]);
                    binding.chartView0.postInvalidate();
                    binding.chartView0.post(() -> loadValue0());
                });
    }

    private void refreshChart1() {
        Observable.just(this).observeOn(Schedulers.io())
                .subscribe(trendTableLayout -> {
                    addDataSet(binding.chartView1, trendControlModel.getItemSensorModel(1), destArray[1]);
                    binding.chartView1.postInvalidate();
                    binding.chartView1.post(() -> loadValue1());
                });
    }

    private void refreshChart2() {
        Observable.just(this).observeOn(Schedulers.io())
                .subscribe(trendTableLayout -> {
                    addDataSet(binding.chartView2, trendControlModel.getItemSensorModel(2), destArray[2]);
                    binding.chartView2.postInvalidate();
                    binding.chartView2.post(() -> loadValue2());
                });
    }


    private void addDataSet(ChartView chartView, SensorModelEnum sensorModelEnum, int[] destList) {
        long lastTime = trendControlModel.getLastTime();
        int interval = trendControlModel.getIntervalValue();
        long startTime = lastTime - interval * Constant.DOT_PER_CHART;

        BaseEntity[] srcList;

        if (sensorModelEnum.ordinal() < SensorModelEnum.Spo2.ordinal()) {
            srcList = daoControl.getIncubatorDaoOperation().get(startTime, lastTime);
        } else if (sensorModelEnum.ordinal() < SensorModelEnum.EcgHr.ordinal()) {
            srcList = daoControl.getSpo2DaoOperation().get(startTime, lastTime);
        } else if (sensorModelEnum.ordinal() < SensorModelEnum.Co2.ordinal()) {
            srcList = daoControl.getEcgDaoControl().get(startTime, lastTime);
        } else {
            srcList = daoControl.getCo2DaoControl().get(startTime, lastTime);
        }

//        LoggerUtil.se(sensorModelEnum.name() + " " + interval + " " + TimeUtil.getTimeFromSecond(endTimeStamp, TimeUtil.FullTime) + " " + TimeUtil.getTimeFromSecond(startTimeStamp, TimeUtil.FullTime));
        RangeUtil.fillData(Constant.DOT_PER_CHART, lastTime, srcList, destList, interval, sensorModelEnum);

        chartView.clearDataSet();
        chartView.setSensor(0, sensorModelEnum);
        chartView.setDataSet(0, destList);
    }

    public synchronized boolean leftTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            playSoundEffect(SoundEffectConstants.CLICK);
            stopDisposable();
            leftSamplePoint();
            loadSamplePoint();
            loadValue0();
            loadValue1();
            loadValue2();
            increaseDisposable = Observable
                    .interval(1000, Constant.LONG_CLICK_DELAY, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Long aLong) -> {
                        systemModel.initializeTimeOut();
                        leftSamplePoint();
                        leftSamplePoint();
                        loadSamplePoint();
                        loadValue0();
                        loadValue1();
                        loadValue2();
                    });
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL ||
                event.getAction() == MotionEvent.ACTION_UP) {
            stopDisposable();
        }
        return true;
    }

    public synchronized boolean rightTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            playSoundEffect(SoundEffectConstants.CLICK);
            stopDisposable();
//            decreaseButton.setPressed(true);
            rightSamplePoint();
            loadSamplePoint();
            loadValue0();
            loadValue1();
            loadValue2();
            decreaseDisposable = Observable
                    .interval(1000, Constant.LONG_CLICK_DELAY, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Long aLong) -> {
                        systemModel.initializeTimeOut();
                        rightSamplePoint();
                        rightSamplePoint();
                        loadSamplePoint();
                        loadValue0();
                        loadValue1();
                        loadValue2();
                    });
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL ||
                event.getAction() == MotionEvent.ACTION_UP) {
            stopDisposable();
//            decreaseButton.setPressed(false);
        }
        return true;
    }

    private void leftSamplePoint() {
        int samplePoint = trendControlModel.getSamplePoint();
        if (samplePoint < 119) {
            trendControlModel.setSamplePoint(samplePoint + 1);
        }
        displaySamplePoint();
    }

    private void rightSamplePoint() {
        int samplePoint = trendControlModel.getSamplePoint();
        if (samplePoint > 0) {
            trendControlModel.setSamplePoint(samplePoint - 1);
        }
        displaySamplePoint();
    }

    private void displaySamplePoint() {
        int samplePoint = trendControlModel.getSamplePoint();
        binding.moveLeft.setEnabled(samplePoint < 119);
        binding.moveRight.setEnabled(samplePoint > 0);
    }

    private synchronized void stopDisposable() {
        if (decreaseDisposable != null) {
            decreaseDisposable.dispose();
            decreaseDisposable = null;
        }
        if (increaseDisposable != null) {
            increaseDisposable.dispose();
            increaseDisposable = null;
        }
    }

    private void loadSamplePoint() {
        int samplePoint = trendControlModel.getSamplePoint();

        constraintSet.clone(binding.rootView);
        constraintSet.setMargin(binding.sampleLine.getId(), ConstraintSet.END, 39 + (int) (4f * samplePoint));
        constraintSet.applyTo(binding.rootView);

        long lastTime = trendControlModel.getLastTime();
        int interval = trendControlModel.getIntervalValue();
        long currentTime = lastTime - interval * samplePoint;

        binding.sampleText.setText(TimeUtil.getTimeFromSecond(currentTime, TimeUtil.MonthDayHourMinute));
    }

    private void loadValue0() {
        int samplePoint = trendControlModel.getSamplePoint();
        SensorModelEnum sensorModelEnum = trendControlModel.getItemSensorModel(0);
        SensorModel sensorModel = sensorModelRepository.getSensorModel(sensorModelEnum);
        if (samplePoint < POINT_SUM) {
            int data = destArray[0][samplePoint];
            if (data >= 0) {
                binding.buttonValueView0.setValue(sensorModel.formatValueUnit(data));
                return;
            }
        }
        binding.buttonValueView0.setValue(sensorModelEnum.getErrorString() + sensorModelEnum.getDecimalErrorString());
    }

    private void loadValue1() {
        int samplePoint = trendControlModel.getSamplePoint();
        SensorModelEnum sensorModelEnum = trendControlModel.getItemSensorModel(1);
        SensorModel sensorModel = sensorModelRepository.getSensorModel(sensorModelEnum);
        if (samplePoint < POINT_SUM) {
            int data = destArray[1][samplePoint];
            if (data >= 0) {
                binding.buttonValueView1.setValue(sensorModel.formatValueUnit(data));
                return;
            }
        }
        binding.buttonValueView1.setValue(sensorModelEnum.getErrorString() + sensorModelEnum.getDecimalErrorString());
    }

    private void loadValue2() {
        int samplePoint = trendControlModel.getSamplePoint();
        SensorModelEnum sensorModelEnum = trendControlModel.getItemSensorModel(2);
        SensorModel sensorModel = sensorModelRepository.getSensorModel(sensorModelEnum);
        if (samplePoint < POINT_SUM) {
            int data = destArray[2][samplePoint];
            if (data >= 0) {
                binding.buttonValueView2.setValue(sensorModel.formatValueUnit(data));
                return;
            }
        }
        binding.buttonValueView2.setValue(sensorModelEnum.getErrorString() + sensorModelEnum.getDecimalErrorString());
    }

    private void displayNextPageButton() {
        binding.nextPage.setEnabled(!trendControlModel.isLastPage());
    }
}