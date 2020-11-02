package com.david.incubator.ui.system;

import android.content.Context;
import android.widget.Button;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.NibpModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.BaseCommand;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.IntervalUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;
import com.david.incubator.serial.nibp.NibpCommandControl;
import com.david.incubator.serial.nibp.NibpProcessMode;
import com.david.incubator.serial.nibp.command.GetFirstCuffPressureCommand;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.Consumer;

public class SystemNibpCalibrationLayout extends BaseLayout implements Consumer<Long> {

    @Inject
    SystemModel systemModel;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    NibpCommandControl nibpCommandControl;
    @Inject
    IntervalUtil intervalUtil;
    @Inject
    NibpModel nibpModel;
    @Inject
    SensorModelRepository sensorModelRepository;

    private final Button nibpValveButton;
    private final Button nibpCalibration0Button;
    private final Button nibpCalibration250Button;

    private final KeyButtonView pressureOptionView;
    private int pressureOption;

    private boolean valveOn;

    private final Observer<Integer> currentPressureObserver;

    public SystemNibpCalibrationLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_NIBP_CALIBRATION);

        nibpValveButton = ViewUtil.buildButton(getContext());
        nibpCalibration0Button = ViewUtil.buildButton(getContext());
        nibpCalibration250Button = ViewUtil.buildButton(getContext());

        if (moduleHardware.isInstalled(ModuleEnum.Nibp)) {
            addInnerButton(0, nibpValveButton);
            addInnerButton(1, nibpCalibration0Button);
            addInnerButton(2, nibpCalibration250Button);
            nibpValveButton.setOnClickListener(view -> {
                if (valveOn) {
                    valveOn = false;
                    nibpValveButton.setText(ContextUtil.getString(R.string.on));
                    nibpCommandControl.setPneumatics(false);
                } else {
                    valveOn = true;
                    nibpValveButton.setText(ContextUtil.getString(R.string.off));
                    nibpCommandControl.setPneumatics(true);
                }
            });
            nibpCalibration0Button.setOnClickListener(v -> {
                nibpCommandControl.calibrate(true);

            });
            nibpCalibration250Button.setOnClickListener(v -> {
                nibpModel.processMode = NibpProcessMode.Calibrate;
                nibpModel.currentPressure.notifyChange();
            });
        }

        pressureOptionView = ViewUtil.buildKeyButtonView(getContext());
        pressureOptionView.getValue().setOnClickListener(v -> {
            optionPopupView.init();
            for (int index = 0; index < ListUtil.statusList.size(); index++) {
                optionPopupView.setOption(index, ContextUtil.getString(ListUtil.statusList.get(index)));
            }
            optionPopupView.setSelectedId(pressureOption);
            optionPopupView.setCallback(this::setPressure);
            optionPopupView.show(this, pressureOptionView.getId(), true);
        });

        addInnerView(3, pressureOptionView);

        currentPressureObserver = num -> {
            SensorModel nibpSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Nibp);
            if (Objects.equals(nibpModel.processMode, NibpProcessMode.PressureTest)) {
                pressureOptionView.setKey(String.format("%s: %s%s", ContextUtil.getString(R.string.pressure),
                        systemModel.nibpUnitFunction.apply(num), nibpSensorModel.unit.getValue()));
            } else if (Objects.equals(nibpModel.processMode, NibpProcessMode.Calibrate)) {
                pressureOptionView.setKey(String.format("%s: %s%s", ContextUtil.getString(R.string.pressure),
                        systemModel.nibpUnitFunction.apply(num), nibpSensorModel.unit.getValue()));

                if (num > 137 && num < 143) {
                    ViewUtil.showToast(ContextUtil.getString(R.string.calibrate_success));
                } else {
                    ViewUtil.showToast(ContextUtil.getString(R.string.calibrate_failed));
                }
            }
        };
    }

    @Override
    public void attach() {
        super.attach();
        nibpCommandControl.running.post(false);

        nibpValveButton.setText(ContextUtil.getString(R.string.on));
        valveOn = false;
        nibpCalibration0Button.setText(ContextUtil.getString(R.string.nibp_calibration0));
        nibpCalibration250Button.setText(ContextUtil.getString(R.string.nibp_calibration140));

        pressureOption = 0;
        pressureOptionView.setKey(null);
        pressureOptionView.setKeyId(R.string.pressure);
        pressureOptionView.setValue(ContextUtil.getString(R.string.off));
        nibpModel.currentPressure.observeForever(currentPressureObserver);
        nibpModel.processMode = NibpProcessMode.Complete;
        intervalUtil.addSecondConsumer(SystemNibpCalibrationLayout.class, this);
    }

    @Override
    public void detach() {
        super.detach();
        intervalUtil.removeSecondConsumer(SystemNibpCalibrationLayout.class);
        nibpModel.currentPressure.removeObserver(currentPressureObserver);
        nibpModel.processMode = NibpProcessMode.Complete;
    }

    private void setPressure(int optionId) {
        if (optionId != pressureOption) {
            pressureOption = optionId;
            pressureOptionView.setValue(ContextUtil.getString(ListUtil.statusList.get(optionId)));

            if (optionId == 0) {
                pressureOptionView.setKeyId(R.string.pressure);
                nibpModel.processMode = NibpProcessMode.Complete;
            } else {
                nibpModel.processMode = NibpProcessMode.PressureTest;
                nibpModel.currentPressure.notifyChange();
            }
        }
    }

    @Override
    public void accept(Long aLong) {
        BaseCommand getPressureCommand = new GetFirstCuffPressureCommand();
        nibpCommandControl.produce(getPressureCommand);
    }
}