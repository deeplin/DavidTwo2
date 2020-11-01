package com.david.incubator.ui.menu;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.database.LastUser;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutMenuHomeBinding;

import javax.inject.Inject;

public class MenuHomeLayout extends BindingBasicLayout<LayoutMenuHomeBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    LastUser lastUser;

    public MenuHomeLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        binding.comfortZoneView.imageId.set(R.mipmap.comfortzone);
        binding.sensorCalibrationView.imageId.set(R.mipmap.sensor_calibration);
        binding.versionView.imageId.set(R.mipmap.version);
        binding.functionSetupView.imageId.set(R.mipmap.function_setup);
        binding.parameterSetupView.imageId.set(R.mipmap.parameter_setup);
        binding.printSetupView.imageId.set(R.mipmap.print_setup);
        binding.demoView.imageId.set(R.mipmap.demo);
        binding.userSetupView.imageId.set(R.mipmap.user_setup);
        binding.systemSetupView.imageId.set(R.mipmap.system_setup);
        binding.addPatientView.imageId.set(R.mipmap.user_info);

        binding.comfortZoneView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.MENU_COMFORT_ZONE));
        binding.sensorCalibrationView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.MENU_SENSOR_CALIBRATION));
        binding.versionView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.MENU_VERSION));
        binding.functionSetupView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.MENU_FUNCTION_SETUP));
//        binding.parameterSetupView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.MENU_PARAMETER_SETUP));
//        binding.printSetupView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.MENU_PRINT_SETUP));
//        binding.demoView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.KEYBOARD_LOGIN_DEMO));
//        binding.userSetupView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.KEYBOARD_LOGIN_USER));
//        binding.systemSetupView.setOnClickListener(view -> systemModel.showLayout(LayoutPageEnum.KEYBOARD_LOGIN_SYSTEM));
//        binding.addPatientView.setOnClickListener(view -> {
//            systemModel.tagId = Constant.NA_VALUE;
//            lastUser.setEditUserEntity(lastUser.getUserEntity());
//            systemModel.showLayout(LayoutPageEnum.PATIENT_INFO);
//        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_menu_home;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        systemModel.tagId = Constant.NA_VALUE;
        binding.titleView.set(R.string.menu, null, true);

        binding.comfortZoneView.value.set(ContextUtil.getString(R.string.comfort_zone));
        binding.sensorCalibrationView.value.set(ContextUtil.getString(R.string.sensor_calibration));
        binding.functionSetupView.value.set(ContextUtil.getString(R.string.function_setup));
        binding.versionView.value.set(ContextUtil.getString(R.string.version));
        binding.parameterSetupView.value.set(ContextUtil.getString(R.string.parameter_setup));
        binding.printSetupView.value.set(ContextUtil.getString(R.string.print_setup));
        if (systemModel.demo.getValue()) {
            binding.demoView.value.set(ContextUtil.getString(R.string.quit_demo));
        } else {
            binding.demoView.value.set(ContextUtil.getString(R.string.demo_setup));
        }

        binding.userSetupView.value.set(ContextUtil.getString(R.string.user_setup));
        binding.systemSetupView.value.set(ContextUtil.getString(R.string.system_setup));
        binding.addPatientView.value.set(ContextUtil.getString(R.string.add_patient));

        binding.comfortZoneView.attach(lifecycleOwner);
        binding.sensorCalibrationView.attach(lifecycleOwner);
        binding.versionView.attach(lifecycleOwner);
        binding.functionSetupView.attach(lifecycleOwner);
        binding.parameterSetupView.attach(lifecycleOwner);
        binding.printSetupView.attach(lifecycleOwner);
        binding.demoView.attach(lifecycleOwner);
        binding.userSetupView.attach(lifecycleOwner);
        binding.systemSetupView.attach(lifecycleOwner);
        binding.addPatientView.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();

        binding.comfortZoneView.detach();
        binding.sensorCalibrationView.detach();
        binding.versionView.detach();
        binding.functionSetupView.detach();
        binding.parameterSetupView.detach();
        binding.printSetupView.detach();
        binding.demoView.detach();
        binding.userSetupView.detach();
        binding.systemSetupView.detach();
        binding.addPatientView.detach();
    }
}