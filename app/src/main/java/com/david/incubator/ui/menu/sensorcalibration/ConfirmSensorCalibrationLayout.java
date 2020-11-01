package com.david.incubator.ui.menu.sensorcalibration;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;

public class ConfirmSensorCalibrationLayout extends ConfirmLayout {

    @Inject
    SystemModel systemModel;
    @Inject
    SensorModelRepository sensorModelRepository;

    public ConfirmSensorCalibrationLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.MENU_CONFIRM_SENSOR_CALIBRATION);

        binding.cancel.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.MENU_SENSOR_CALIBRATION));

        binding.confirm.setOnClickListener(v -> {
            systemModel.tagId += 100;
            systemModel.showLayout(LayoutPageEnum.MENU_SENSOR_CALIBRATION);
        });
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        switch (systemModel.tagId) {
            case (0):
                binding.confirmText.setText(ContextUtil.getString(R.string.calibration_confirm_o2_21));
                break;
            case (1):
                binding.confirmText.setText(ContextUtil.getString(R.string.calibration_confirm_o2_100));
                break;
            case (2):
                SensorModel scaleModel0 = sensorModelRepository.getSensorModel(SensorModelEnum.Weight);
                binding.confirmText.setText(String.format(ContextUtil.getString(R.string.calibration_confirm_weight_0), scaleModel0.textNumber.getValue()));
                break;
            case (3):
                SensorModel scaleModel1 = sensorModelRepository.getSensorModel(SensorModelEnum.Weight);
                binding.confirmText.setText(String.format(ContextUtil.getString(R.string.calibration_confirm_weight_5000), scaleModel1.textNumber.getValue()));
                break;
        }
    }

    @Override
    public void detach() {
        super.detach();
    }
}