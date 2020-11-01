package com.david.incubator.ui.menu.sensorcalibration;

import android.content.Context;
import android.widget.Button;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class MenuSensorCalibrationLayout extends BaseLayout {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    IncubatorCommandSender incubatorCommandSender;

    private final Button o221Button;
    private final Button o2100Button;
    private final Button scale0Button;
    private final Button scale5000Button;

    public MenuSensorCalibrationLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.MENU_SENSOR_CALIBRATION);

        o221Button = ViewUtil.buildButton(getContext());
        o2100Button = ViewUtil.buildButton(getContext());
        scale0Button = ViewUtil.buildButton(getContext());
        scale5000Button = ViewUtil.buildButton(getContext());

        o221Button.setOnClickListener(v -> {
            systemModel.tagId = 0;
            systemModel.showLayout(LayoutPageEnum.MENU_CONFIRM_SENSOR_CALIBRATION);
        });

        o2100Button.setOnClickListener(v -> {
            systemModel.tagId = 1;
            systemModel.showLayout(LayoutPageEnum.MENU_CONFIRM_SENSOR_CALIBRATION);
        });

        scale0Button.setOnClickListener(v -> {
            systemModel.tagId = 2;
            systemModel.showLayout(LayoutPageEnum.MENU_CONFIRM_SENSOR_CALIBRATION);
        });

        scale5000Button.setOnClickListener(v -> {
            systemModel.tagId = 3;
            systemModel.showLayout(LayoutPageEnum.MENU_CONFIRM_SENSOR_CALIBRATION);
        });
    }

    @Override
    public void attach() {
        super.attach();

        int rowId = 0;
        if (moduleHardware.isActive(ModuleEnum.Oxygen) && incubatorModel.isCabin()) {
            o221Button.setText(ContextUtil.getString(R.string.calibration_o2_21));
            o2100Button.setText(ContextUtil.getString(R.string.calibration_o2_100));
            addInnerButton(rowId++, o221Button);
            addInnerButton(rowId++, o2100Button);
        }

        if (moduleHardware.isInstalled(ModuleEnum.Weight)) {
            scale0Button.setText(ContextUtil.getString(R.string.calibration_weight_0));
            scale5000Button.setText(ContextUtil.getString(R.string.calibration_weight_5000));
            addInnerButton(rowId++, scale0Button);
            addInnerButton(rowId, scale5000Button);
        }

        if (systemModel.tagId >= 100) {
            systemModel.tagId %= 100;
            switch (systemModel.tagId) {
                case (0):
                    setO2Calibration(o221Button, 210);
                    break;
                case (1):
                    setO2Calibration(o2100Button, 999);
                    break;
                case (2):
                    setScaleCalibration(scale0Button, 0);
                    break;
                case (3):
                    setScaleCalibration(scale5000Button, 5000);
                    break;
            }
        } else {
            o221Button.setSelected(false);
            o2100Button.setSelected(false);
            scale0Button.setSelected(false);
            scale5000Button.setSelected(false);
        }
    }

    @Override
    public void detach() {
        super.detach();
        removeView(o221Button);
        removeView(o2100Button);
        removeView(scale0Button);
        removeView(scale5000Button);
    }

    private void setO2Calibration(final Button button, final int value) {
        incubatorCommandSender.setOxygen(1, value,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        incubatorCommandSender.setOxygen(2, value,
                                (aaBoolean, abaseSerialMessage) -> button.post(() -> button.setSelected(aaBoolean)));
                    } else {
                        button.setSelected(false);
                    }
                });
    }

    private void setScaleCalibration(final Button button, final int value) {
        incubatorCommandSender.setScale(value,
                (aBoolean, baseSerialMessage) -> button.post(() -> button.setSelected(aBoolean)));
    }
}