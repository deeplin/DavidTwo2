package com.david.incubator.ui.user;

import android.content.Context;
import android.view.View;

import androidx.core.util.Consumer;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class UserModuleSetupLayout extends BaseLayout {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    SystemModel systemModel;

    private KeyButtonView[] keyButtonViews;
    private int[] moduleSettings;

    public UserModuleSetupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.USER_MODULE_SETUP);

        initPopup(8);

        setRowId(0, 0);
        setRowId(1, 1);
        setRowId(2, 2);
        setRowId(3, 3);
        setRowId(4, 4);
        setRowId(5, 5);
        setRowId(6, 6);
        setRowId(7, 7);
    }

    @Override
    public void attach() {
        super.attach();

        int index = 0;

        if (incubatorModel.isCabin()) {
            if (moduleHardware.isInstalled(ModuleEnum.Hum)) {
                moduleSettings[index] = moduleHardware.isActive(ModuleEnum.Hum) ? 1 : 0;
                setText(index, R.string.hum_setting, ListUtil.statusList.toArray());
                setPopup(index++, ListUtil.statusList.toArray(), true, this::humidityCallback);
            }

            if (moduleHardware.isInstalled(ModuleEnum.Oxygen)) {
                moduleSettings[index] = moduleHardware.isActive(ModuleEnum.Oxygen) ? 1 : 0;
                setText(index, R.string.oxygen_setting, ListUtil.statusList.toArray());
                setPopup(index++, ListUtil.statusList.toArray(), true, this::oxygenCallback);
            }
        }

        if (moduleHardware.isInstalled(ModuleEnum.Spo2)) {
            moduleSettings[index] = moduleHardware.isActive(ModuleEnum.Spo2) ? 1 : 0;
            setText(index, R.string.spo2_setting, ListUtil.statusList.toArray());
            setPopup(index++, ListUtil.statusList.toArray(), true, this::spo2Callback);
        }

        if (moduleHardware.isInstalled(ModuleEnum.Ecg)) {
            moduleSettings[index] = moduleHardware.isActive(ModuleEnum.Ecg) ? 1 : 0;
            setText(index, R.string.ecg_setting, ListUtil.statusList.toArray());
            setPopup(index++, ListUtil.statusList.toArray(), true, this::ecgCallback);

            moduleSettings[index] = moduleHardware.isActive(ModuleEnum.Resp) ? 1 : 0;
            setText(index, R.string.resp_setting, ListUtil.statusList.toArray());
            setPopup(index++, ListUtil.statusList.toArray(), true, this::respCallback);
        }

        if (moduleHardware.isInstalled(ModuleEnum.Co2)) {
            moduleSettings[index] = moduleHardware.isActive(ModuleEnum.Co2) ? 1 : 0;
            setText(index, R.string.co2_setting, ListUtil.statusList.toArray());
            setPopup(index++, ListUtil.statusList.toArray(), true, this::co2Callback);
        }

        if (moduleHardware.isInstalled(ModuleEnum.Nibp)) {
            moduleSettings[index] = moduleHardware.isActive(ModuleEnum.Nibp) ? 1 : 0;
            setText(index, R.string.nibp_setting, ListUtil.statusList.toArray());
            setPopup(index++, ListUtil.statusList.toArray(), true, this::nibpCallback);
        }

        if (moduleHardware.isInstalled(ModuleEnum.Wake)) {
            moduleSettings[index] = moduleHardware.isActive(ModuleEnum.Wake) ? 1 : 0;
            setText(index, R.string.wake_setting, ListUtil.statusList.toArray());
            setPopup(index++, ListUtil.statusList.toArray(), false, this::wakeCallback);
        }

        for (; index < 8; index++) {
            keyButtonViews[index].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    protected void initPopup(int rowSum) {
        moduleSettings = new int[rowSum];
        keyButtonViews = new KeyButtonView[rowSum];
        for (int index = 0; index < rowSum; index++) {
            keyButtonViews[index] = ViewUtil.buildKeyButtonView(getContext());
        }
    }

    protected void setRowId(int index, int rowId) {
        KeyButtonView keyButtonView = keyButtonViews[index];
        addInnerView(rowId, keyButtonView);
    }

    protected void setText(int index, int textId, Object[] valueArray) {
        KeyButtonView keyButtonView = keyButtonViews[index];
        keyButtonView.setVisibility(View.VISIBLE);
        keyButtonView.setSelected(false);
        keyButtonView.setKeyId(textId);
        keyButtonView.setValue(ContextUtil.getString((int) valueArray[moduleSettings[index]]));
    }

    protected void setPopup(final int index, final Object[] valueArray,
                            final boolean downward, final Consumer<Integer> callback) {
        final KeyButtonView keyButtonView = keyButtonViews[index];
        keyButtonView.getValue().setOnClickListener(v -> {
            optionPopupView.init();

            for (int i = 0; i < valueArray.length; i++) {
                optionPopupView.setOption(i, ContextUtil.getString((int) valueArray[i]));
            }

            optionPopupView.setSelectedId(moduleSettings[index]);
            optionPopupView.setCallback(integer -> {
                if (integer.intValue() != moduleSettings[index]) {
                    moduleSettings[index] = integer;
                    keyButtonView.setValue(ContextUtil.getString((int) valueArray[integer]));
                    keyButtonView.setSelected(true);
                    if (callback != null)
                        callback.accept(integer);
                }
            });
            optionPopupView.show(this, keyButtonViews[index].getId(), downward);
        });
    }

    private void humidityCallback(Integer position) {
        setValue(position, SensorModelEnum.Humidity.getCommandName());
    }

    private void oxygenCallback(Integer position) {
        setValue(position, SensorModelEnum.Oxygen.getCommandName());
    }

    private void spo2Callback(Integer position) {
        setValue(position, SensorModelEnum.Spo2.getCommandName());
        if (position == 0) {
            systemModel.setStandardLayout();
        }
    }

    private void ecgCallback(Integer position) {
        setValue(position, ContextUtil.getString(R.string.ecg_id));
        if (position == 0) {
            systemModel.setStandardLayout();
        }
    }

    private void nibpCallback(Integer position) {
        setValue(position, SensorModelEnum.Nibp.getCommandName());
        if (position == 0) {
            systemModel.setStandardLayout();
        }
    }


    private void co2Callback(Integer position) {
        setValue(position, SensorModelEnum.Co2.getCommandName());
        if (position == 0) {
            systemModel.setStandardLayout();
        }
    }

    private void respCallback(Integer position) {
        setValue(position, "RESP");
        if (position == 0) {
            systemModel.setStandardLayout();
        }
    }

    private void wakeCallback(Integer position) {
        setValue(position, SensorModelEnum.Wake.getCommandName());
        if (position == 0) {
            systemModel.setStandardLayout();
        }
    }

    private void setValue(Integer position, String commandName) {
        incubatorCommandSender.setModule(commandName, true, position == 1,
                null);
    }
}