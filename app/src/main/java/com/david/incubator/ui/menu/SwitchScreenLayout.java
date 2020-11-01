package com.david.incubator.ui.menu;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

import static com.david.core.enumeration.LayoutPageEnum.SWITCH_SCREEN;

public class SwitchScreenLayout extends BaseLayout {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ConfigRepository configRepository;
    @Inject
    SystemModel systemModel;

    private final Button[] buttonArray;

    public SwitchScreenLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(SWITCH_SCREEN);

        buttonArray = new Button[8];
        for (int index = 0; index < buttonArray.length; index++) {
            buttonArray[index] = ViewUtil.buildButton(getContext());
            addInnerButton(index, buttonArray[index]);
        }

        buttonArray[0].setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.LAYOUT_STANDARD));

        buttonArray[1].setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.LAYOUT_BASIC));

        buttonArray[2].setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.LAYOUT_TEMP_CURVE));
    }

    @Override
    public void attach() {
        super.attach();

        for (int index = 0; index < buttonArray.length; index++) {
            buttonArray[index].setSelected(false);
        }

        buttonArray[0].setText(ContextUtil.getString(R.string.standard_interface));
        if (systemModel.getCurrentLayoutPage() == LayoutPageEnum.LAYOUT_STANDARD) {
            buttonArray[0].setSelected(true);
        }
        buttonArray[1].setText(ContextUtil.getString(R.string.standard_basic_interface));
        if (systemModel.getCurrentLayoutPage() == LayoutPageEnum.LAYOUT_BASIC) {
            buttonArray[1].setSelected(true);
        }
        buttonArray[2].setText(ContextUtil.getString(R.string.temp_curve));
        if (systemModel.getCurrentLayoutPage() == LayoutPageEnum.LAYOUT_TEMP_CURVE) {
            buttonArray[2].setSelected(true);
        }

        int rowId = 3;
        if (moduleHardware.isInstalled(ModuleEnum.Weight)) {
            setRow(rowId++, R.string.weight_curve, LayoutPageEnum.LAYOUT_WEIGHT_CURVE);
        }

        if (moduleHardware.isActive(ModuleEnum.Ecg) || moduleHardware.isActive(ModuleEnum.Spo2)
                || moduleHardware.isActive(ModuleEnum.Co2)) {
            setRow(rowId++, R.string.body_wave, LayoutPageEnum.LAYOUT_BODY_WAVE);
        }

        if (configRepository.getConfig(ConfigEnum.EcgLeadSetting).getValue() == 0) {
            setRow(rowId++, R.string.same_screen, LayoutPageEnum.LAYOUT_SAME_SCREEN);
        }

        if (moduleHardware.isActive(ModuleEnum.Spo2)) {
            setRow(rowId++, R.string.rainbow, LayoutPageEnum.LAYOUT_SPO2);
        }

        if (moduleHardware.isInstalled(ModuleEnum.Camera)) {
            setRow(rowId++, R.string.camera, LayoutPageEnum.LAYOUT_CAMERA);
        }

        for (int index = rowId; index < buttonArray.length; index++) {
            buttonArray[index].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void setRow(int rowId, int textId, final LayoutPageEnum layoutPageEnum) {
        buttonArray[rowId].setText(ContextUtil.getString(textId));
        buttonArray[rowId].setVisibility(View.VISIBLE);
        buttonArray[rowId].setOnClickListener(v -> systemModel.showLayout(layoutPageEnum));
        if (systemModel.getCurrentLayoutPage() == layoutPageEnum) {
            buttonArray[rowId].setSelected(true);
        }
    }
}