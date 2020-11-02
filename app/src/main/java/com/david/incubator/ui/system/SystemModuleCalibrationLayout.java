package com.david.incubator.ui.system;

import android.content.Context;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class SystemModuleCalibrationLayout extends BaseLayout {

    @Inject
    SystemModel systemModel;
    @Inject
    ModuleHardware moduleHardware;

    private final Button nibpCalibrationButton;

    public SystemModuleCalibrationLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_MODULE_CALIBRATION);

        nibpCalibrationButton = ViewUtil.buildButton(getContext());

        if (moduleHardware.isInstalled(ModuleEnum.Nibp)) {
            addInnerButton(0, nibpCalibrationButton);
            nibpCalibrationButton.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_NIBP_CALIBRATION));
        }
    }

    @Override
    public void attach() {
        super.attach();
        nibpCalibrationButton.setText(ContextUtil.getString(R.string.nibp_calibration));
    }

    @Override
    public void detach() {
        super.detach();
    }
}