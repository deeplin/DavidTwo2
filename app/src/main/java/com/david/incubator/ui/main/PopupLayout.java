package com.david.incubator.ui.main;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.alarm.PhysiologicalAlarmListLayout;
import com.david.core.ui.alarm.TechnicalAlarmListLayout;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.ILifeCycleOwner;
import com.david.databinding.LayoutPopupBinding;
import com.david.incubator.ui.alarm.SetupAlarmLayout;
import com.david.incubator.ui.camera.CameraImageLayout;
import com.david.incubator.ui.camera.CameraImageListLayout;
import com.david.incubator.ui.camera.CameraVideoLayout;
import com.david.incubator.ui.camera.CameraVideoListLayout;
import com.david.incubator.ui.keyboard.KeyboardLoginLayout;
import com.david.incubator.ui.menu.MenuComfortZoneLayout;
import com.david.incubator.ui.menu.MenuFunctionSetupLayout;
import com.david.incubator.ui.menu.MenuHomeLayout;
import com.david.incubator.ui.menu.MenuParameterSetupLayout;
import com.david.incubator.ui.menu.MenuPrintSetupLayout;
import com.david.incubator.ui.menu.MenuVersionLayout;
import com.david.incubator.ui.menu.SwitchScreenLayout;
import com.david.incubator.ui.menu.sensorcalibration.ConfirmSensorCalibrationLayout;
import com.david.incubator.ui.menu.sensorcalibration.MenuSensorCalibrationLayout;
import com.david.incubator.ui.patient.PatientAddLayout;
import com.david.incubator.ui.patient.PatientConfirmRemoveLayout;
import com.david.incubator.ui.patient.PatientInfoLayout;
import com.david.incubator.ui.patient.PatientListInfoLayout;
import com.david.incubator.ui.patient.PatientListLayout;
import com.david.incubator.ui.setup.SetupHomeIncubatorLayout;
import com.david.incubator.ui.setup.SetupHomeLayout;
import com.david.incubator.ui.system.SystemAlarmListLayout;
import com.david.incubator.ui.system.SystemDebugInfoLayout;
import com.david.incubator.ui.system.SystemDeviationAlarmLayout;
import com.david.incubator.ui.system.SystemHomeLayout;
import com.david.incubator.ui.system.SystemModuleCalibrationLayout;
import com.david.incubator.ui.system.SystemModuleSetupLayout;
import com.david.incubator.ui.system.SystemNibpCalibrationLayout;
import com.david.incubator.ui.system.SystemOverheatAlarmLayout;
import com.david.incubator.ui.system.SystemRangeSetupLayout;
import com.david.incubator.ui.system.SystemSensorCalibrationLayout;
import com.david.incubator.ui.system.factory.ConfirmFactoryLayout;
import com.david.incubator.ui.system.factory.SystemFactoryLayout;
import com.david.incubator.ui.system.print.SystemPrintLayout;
import com.david.incubator.ui.trend.chart.TrendChartLayout;
import com.david.incubator.ui.trend.print.TrendPrintLayout;
import com.david.incubator.ui.trend.table.TrendTableLayout;
import com.david.incubator.ui.user.UserHomeLayout;
import com.david.incubator.ui.user.UserLanguageLayout;
import com.david.incubator.ui.user.UserModuleSetupLayout;
import com.david.incubator.ui.user.UserOverheatExperimentLayout;
import com.david.incubator.ui.user.UserTimeLayout;
import com.david.incubator.ui.user.UserUnitSetupLayout;

import javax.inject.Inject;

public class PopupLayout extends BindingBasicLayout<LayoutPopupBinding> {

    private final int POPUP_START_ID = LayoutPageEnum.MENU_HOME.ordinal();

    @Inject
    SystemModel systemModel;

    private View currentLayout;
    private final View[] views =
            new View[LayoutPageEnum.LAYOUT_NONE.ordinal() - POPUP_START_ID];

    public PopupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        views[LayoutPageEnum.SWITCH_SCREEN.ordinal() - POPUP_START_ID] = new SwitchScreenLayout(getContext());
        views[LayoutPageEnum.MENU_HOME.ordinal() - POPUP_START_ID] = new MenuHomeLayout(getContext());
        views[LayoutPageEnum.MENU_COMFORT_ZONE.ordinal() - POPUP_START_ID] = new MenuComfortZoneLayout(getContext());
        views[LayoutPageEnum.MENU_SENSOR_CALIBRATION.ordinal() - POPUP_START_ID] = new MenuSensorCalibrationLayout(getContext());
        views[LayoutPageEnum.MENU_CONFIRM_SENSOR_CALIBRATION.ordinal() - POPUP_START_ID] = new ConfirmSensorCalibrationLayout(getContext());
        views[LayoutPageEnum.MENU_VERSION.ordinal() - POPUP_START_ID] = new MenuVersionLayout(getContext());
        views[LayoutPageEnum.MENU_FUNCTION_SETUP.ordinal() - POPUP_START_ID] = new MenuFunctionSetupLayout(getContext());
        views[LayoutPageEnum.MENU_PARAMETER_SETUP.ordinal() - POPUP_START_ID] = new MenuParameterSetupLayout(getContext());
        views[LayoutPageEnum.MENU_PRINT_SETUP.ordinal() - POPUP_START_ID] = new MenuPrintSetupLayout(getContext());
        views[LayoutPageEnum.KEYBOARD_LOGIN_DEMO.ordinal() - POPUP_START_ID] = new KeyboardLoginLayout(getContext());

        views[LayoutPageEnum.USER_HOME.ordinal() - POPUP_START_ID] = new UserHomeLayout(getContext());
        views[LayoutPageEnum.USER_LANGUAGE.ordinal() - POPUP_START_ID] = new UserLanguageLayout(getContext());
        views[LayoutPageEnum.USER_TIME.ordinal() - POPUP_START_ID] = new UserTimeLayout(getContext());
        views[LayoutPageEnum.USER_OVERHEAT_EXPERIMENT.ordinal() - POPUP_START_ID] = new UserOverheatExperimentLayout(getContext());
        views[LayoutPageEnum.USER_MODULE_SETUP.ordinal() - POPUP_START_ID] = new UserModuleSetupLayout(getContext());
        views[LayoutPageEnum.USER_UNIT_SETUP.ordinal() - POPUP_START_ID] = new UserUnitSetupLayout(getContext());

        views[LayoutPageEnum.SYSTEM_HOME.ordinal() - POPUP_START_ID] = new SystemHomeLayout(getContext());
        views[LayoutPageEnum.SYSTEM_DEVIATION_ALARM.ordinal() - POPUP_START_ID] = new SystemDeviationAlarmLayout(getContext());
        views[LayoutPageEnum.SYSTEM_OVERHEAT_ALARM.ordinal() - POPUP_START_ID] = new SystemOverheatAlarmLayout(getContext());
        views[LayoutPageEnum.SYSTEM_RANGE_SETUP.ordinal() - POPUP_START_ID] = new SystemRangeSetupLayout(getContext());
        views[LayoutPageEnum.SYSTEM_SENSOR_CALIBRATION.ordinal() - POPUP_START_ID] = new SystemSensorCalibrationLayout(getContext());
        views[LayoutPageEnum.SYSTEM_DEBUG_INFO.ordinal() - POPUP_START_ID] = new SystemDebugInfoLayout(getContext());
        views[LayoutPageEnum.SYSTEM_FACTORY.ordinal() - POPUP_START_ID] = new SystemFactoryLayout(getContext());
        views[LayoutPageEnum.SYSTEM_CONFIRM_FACTORY.ordinal() - POPUP_START_ID] = new ConfirmFactoryLayout(getContext());
        views[LayoutPageEnum.SYSTEM_MODULE_CALIBRATION.ordinal() - POPUP_START_ID] = new SystemModuleCalibrationLayout(getContext());
        views[LayoutPageEnum.SYSTEM_NIBP_CALIBRATION.ordinal() - POPUP_START_ID] = new SystemNibpCalibrationLayout(getContext());
        views[LayoutPageEnum.SYSTEM_ALARM_LIST.ordinal() - POPUP_START_ID] = new SystemAlarmListLayout(getContext());
        views[LayoutPageEnum.SYSTEM_MODULE_SETUP.ordinal() - POPUP_START_ID] = new SystemModuleSetupLayout(getContext());

        views[LayoutPageEnum.SETUP_HOME_INCUBATOR.ordinal() - POPUP_START_ID] = new SetupHomeIncubatorLayout(getContext());
        views[LayoutPageEnum.SETUP_HOME.ordinal() - POPUP_START_ID] = new SetupHomeLayout(getContext());
        views[LayoutPageEnum.SYSTEM_PRINT.ordinal() - POPUP_START_ID] = new SystemPrintLayout(getContext());

        views[LayoutPageEnum.PATIENT_INFO.ordinal() - POPUP_START_ID] = new PatientInfoLayout(getContext());
        views[LayoutPageEnum.PATIENT_ADD.ordinal() - POPUP_START_ID] = new PatientAddLayout(getContext());
        views[LayoutPageEnum.PATIENT_LIST.ordinal() - POPUP_START_ID] = new PatientListLayout(getContext());
        views[LayoutPageEnum.PATIENT_LIST_INFO.ordinal() - POPUP_START_ID] = new PatientListInfoLayout(getContext());
        views[LayoutPageEnum.PATIENT_CONFIRM_REMOVE.ordinal() - POPUP_START_ID] = new PatientConfirmRemoveLayout(getContext());

        views[LayoutPageEnum.ALARM_LIST_PHYSIOLOGICAL.ordinal() - POPUP_START_ID] = new PhysiologicalAlarmListLayout(getContext());
        views[LayoutPageEnum.ALARM_LIST_TECHNICAL.ordinal() - POPUP_START_ID] = new TechnicalAlarmListLayout(getContext());

        views[LayoutPageEnum.TREND_CHART.ordinal() - POPUP_START_ID] = new TrendChartLayout(getContext());
        views[LayoutPageEnum.TREND_TABLE.ordinal() - POPUP_START_ID] = new TrendTableLayout(getContext());
        views[LayoutPageEnum.TREND_PRINT.ordinal() - POPUP_START_ID] = new TrendPrintLayout(getContext());

        views[LayoutPageEnum.SETUP_ALARM.ordinal() - POPUP_START_ID] = new SetupAlarmLayout(getContext());

        views[LayoutPageEnum.CAMERA_IMAGE.ordinal() - POPUP_START_ID] = new CameraImageLayout(getContext());
        views[LayoutPageEnum.CAMERA_VIDEO.ordinal() - POPUP_START_ID] = new CameraVideoLayout(getContext());
        views[LayoutPageEnum.CAMERA_IMAGE_LIST.ordinal() - POPUP_START_ID] = new CameraImageListLayout(getContext());
        views[LayoutPageEnum.CAMERA_VIDEO_LIST.ordinal() - POPUP_START_ID] = new CameraVideoListLayout(getContext());

        for (int index = 0; index < views.length; index++) {
            View view = views[index];
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
                binding.mainFrameLayout.addView(view);
            }
        }
        views[LayoutPageEnum.KEYBOARD_LOGIN_USER.ordinal() - POPUP_START_ID] = views[LayoutPageEnum.KEYBOARD_LOGIN_DEMO.ordinal() - POPUP_START_ID];
        views[LayoutPageEnum.KEYBOARD_LOGIN_SYSTEM.ordinal() - POPUP_START_ID] = views[LayoutPageEnum.KEYBOARD_LOGIN_DEMO.ordinal() - POPUP_START_ID];
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_popup;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.incubatorListLayout.attach(lifeCycleOwner);
        currentLayout = views[systemModel.layoutPage.getValue().ordinal() - LayoutPageEnum.MENU_HOME.ordinal()];
        if (currentLayout instanceof ILifeCycleOwner) {
            ((ILifeCycleOwner) currentLayout).attach(lifeCycleOwner);
        } else if (currentLayout instanceof ILifeCycle) {
            ((ILifeCycle) currentLayout).attach();
        }

        currentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void detach() {
        super.detach();
        currentLayout.setVisibility(View.INVISIBLE);
        if (currentLayout instanceof ILifeCycleOwner) {
            ((ILifeCycleOwner) currentLayout).detach();
        } else if (currentLayout instanceof ILifeCycle) {
            ((ILifeCycle) currentLayout).detach();
        }
        currentLayout = null;
        binding.incubatorListLayout.detach();
    }
}