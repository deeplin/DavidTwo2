package com.david.incubator.ui.system;

import android.content.Context;
import android.widget.Button;

import com.david.R;
import com.david.core.database.LastUser;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class SystemHomeLayout extends BaseLayout {

    @Inject
    SystemModel systemModel;
    @Inject
    LastUser lastUser;

    private final Button deviationAlarmButton;
    private final Button overheatAlarmButton;
    private final Button rangeSetupButton;
    private final Button sensorCalibrationButton;
    private final Button debugInfoButton;
    private final Button factoryButton;
    private final Button moduleCalibrationButton;
    private final Button alarmListButton;
    private final Button printButton;

//    private final Button imageButton;
//    private final Button videoButton;


    public SystemHomeLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_HOME);

        deviationAlarmButton = ViewUtil.buildButton(getContext());
        addInnerButton(0, deviationAlarmButton);

        overheatAlarmButton = ViewUtil.buildButton(getContext());
        addInnerButton(1, overheatAlarmButton);

        rangeSetupButton = ViewUtil.buildButton(getContext());
        addInnerButton(2, rangeSetupButton);

        sensorCalibrationButton = ViewUtil.buildButton(getContext());
        addInnerButton(3, sensorCalibrationButton);

        debugInfoButton = ViewUtil.buildButton(getContext());
        addInnerButton(4, debugInfoButton);

        factoryButton = ViewUtil.buildButton(getContext());
        addInnerButton(5, factoryButton);

        moduleCalibrationButton = ViewUtil.buildButton(getContext());
        addInnerButton(6, moduleCalibrationButton);

        alarmListButton = ViewUtil.buildButton(getContext());
        addInnerButton(7, alarmListButton);

        printButton = ViewUtil.buildButton(getContext());
        addInnerButton(8, printButton);

//        imageButton = ViewUtil.buildButton(getContext());
//        addInnerButton(8, imageButton);
//
//        videoButton = ViewUtil.buildButton(getContext());
//        addInnerButton(9, videoButton);

        deviationAlarmButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_DEVIATION_ALARM));
        overheatAlarmButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_OVERHEAT_ALARM));
        rangeSetupButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_RANGE_SETUP));
        sensorCalibrationButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_SENSOR_CALIBRATION));
        debugInfoButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_DEBUG_INFO));
        factoryButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_FACTORY));
        moduleCalibrationButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_MODULE_CALIBRATION));
        alarmListButton.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.SYSTEM_ALARM_LIST));
        printButton.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.SYSTEM_PRINT));
//
//        imageButton.setOnClickListener(view -> {
//            UserImageListLayout.init(Long.MIN_VALUE, Long.MAX_VALUE);
//            systemModel.showLayout(LayoutPageEnum.SYSTEM_Image_List);
//        });
//        videoButton.setOnClickListener(view -> {
//            UserVideoListLayout.init(Long.MIN_VALUE, Long.MAX_VALUE);
//            systemModel.showLayout(LayoutPageEnum.SYSTEM_Video_List);
//        });
    }

    @Override
    public void attach() {
        super.attach();
        systemModel.tagId = Constant.NA_VALUE;

        deviationAlarmButton.setText(ContextUtil.getString(R.string.deviation_alarm));
        overheatAlarmButton.setText(ContextUtil.getString(R.string.overheat_alarm));
        rangeSetupButton.setText(ContextUtil.getString(R.string.range_setup));
        sensorCalibrationButton.setText(ContextUtil.getString(R.string.sensor_calibration));
        debugInfoButton.setText(ContextUtil.getString(R.string.debug_info));
        factoryButton.setText(ContextUtil.getString(R.string.factory_setting));
        moduleCalibrationButton.setText(ContextUtil.getString(R.string.module_calibration));
        alarmListButton.setText(ContextUtil.getString(R.string.alarm_record));
        printButton.setText(ContextUtil.getString(R.string.print_setup));
//        imageButton.setText(ContextUtil.getString(R.string.all_image));
//        videoButton.setText(ContextUtil.getString(R.string.all_video));
    }

    @Override
    public void detach() {
        super.detach();
    }
}