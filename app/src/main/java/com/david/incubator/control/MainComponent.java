package com.david.incubator.control;

import com.david.core.control.CommonComponent;
import com.david.core.ui.alarm.PhysiologicalAlarmListLayout;
import com.david.core.ui.alarm.TechnicalAlarmListLayout;
import com.david.core.ui.curve.Co2SurfaceView;
import com.david.incubator.print.ui.BasePrintCurveView;
import com.david.incubator.print.ui.PrintEcgWaveView;
import com.david.incubator.print.ui.PrintSpo2WaveView;
import com.david.incubator.print.ui.PrintWaveView;
import com.david.incubator.serial.co2.Co2CommandControl;
import com.david.incubator.serial.wake.WakeCommandControl;
import com.david.incubator.ui.alarm.SetupAlarmCo2Layout;
import com.david.incubator.ui.alarm.SetupAlarmEcgLayout;
import com.david.incubator.ui.alarm.SetupAlarmLayout;
import com.david.incubator.ui.alarm.SetupAlarmNibpLayout;
import com.david.incubator.ui.alarm.SetupAlarmRespLayout;
import com.david.incubator.ui.alarm.SetupAlarmSpo2Layout;
import com.david.incubator.ui.bottom.MenuLayout;
import com.david.incubator.ui.camera.CameraImageLayout;
import com.david.incubator.ui.camera.CameraImageListLayout;
import com.david.incubator.ui.camera.CameraVideoLayout;
import com.david.incubator.ui.camera.CameraVideoListLayout;
import com.david.incubator.ui.home.basic.BasicLayout;
import com.david.incubator.ui.home.basic.MiddleRightBasicLayout;
import com.david.incubator.ui.home.basic.StandardSpo2BasicLayout;
import com.david.incubator.ui.home.bodywave.BodyWaveLayout;
import com.david.incubator.ui.home.camera.CameraView;
import com.david.incubator.ui.home.common.Co2RrTinyView;
import com.david.incubator.ui.home.common.Co2RrView;
import com.david.incubator.ui.home.common.HrPrView;
import com.david.incubator.ui.home.common.IncubatorLayout;
import com.david.incubator.ui.home.common.MonitorLayout;
import com.david.incubator.ui.home.common.NibpTinyView;
import com.david.incubator.ui.home.common.NibpView;
import com.david.incubator.ui.home.common.Spo2ListView;
import com.david.incubator.ui.home.common.TimingLayout;
import com.david.incubator.ui.home.common.TimingSmallLayout;
import com.david.incubator.ui.home.samescreen.SameScreenLayout;
import com.david.incubator.ui.home.spo2.Spo2Layout;
import com.david.incubator.ui.home.spo2.Spo2ListLayout;
import com.david.incubator.ui.home.standard.StandardLayout;
import com.david.incubator.ui.home.standard.bottom.StandardSpo2Layout;
import com.david.incubator.ui.home.standard.middle.MiddleLeftLayout;
import com.david.incubator.ui.home.standard.middle.MiddleRightLayout;
import com.david.incubator.ui.home.standard.top.HomeSetView;
import com.david.incubator.ui.home.tempcurve.TempCurveLayout;
import com.david.incubator.ui.home.weightcurve.WeightCurveLayout;
import com.david.incubator.ui.keyboard.KeyboardLoginLayout;
import com.david.incubator.ui.main.IncubatorListLayout;
import com.david.incubator.ui.main.MainActivity;
import com.david.incubator.ui.main.PopupLayout;
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
import com.david.incubator.ui.patient.PatientListLayout;
import com.david.incubator.ui.setup.SetupCo2Layout;
import com.david.incubator.ui.setup.SetupEcgLayout;
import com.david.incubator.ui.setup.SetupHomeIncubatorLayout;
import com.david.incubator.ui.setup.SetupHomeLayout;
import com.david.incubator.ui.setup.SetupHumidityLayout;
import com.david.incubator.ui.setup.SetupNibpLayout;
import com.david.incubator.ui.setup.SetupOxygenLayout;
import com.david.incubator.ui.setup.SetupRespLayout;
import com.david.incubator.ui.setup.SetupSpo2Layout;
import com.david.incubator.ui.setup.SetupTempLayout;
import com.david.incubator.ui.setup.SetupWakeLayout;
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
import com.david.incubator.ui.top.TopLayout;
import com.david.incubator.ui.trend.chart.TrendChartLayout;
import com.david.incubator.ui.trend.print.TrendPrintLayout;
import com.david.incubator.ui.trend.print.TrendPrintTableLayout;
import com.david.incubator.ui.trend.table.TrendTableLayout;
import com.david.incubator.ui.user.UserHomeLayout;
import com.david.incubator.ui.user.UserLanguageLayout;
import com.david.incubator.ui.user.UserModuleSetupLayout;
import com.david.incubator.ui.user.UserOverheatExperimentLayout;
import com.david.incubator.ui.user.UserTimeLayout;
import com.david.incubator.ui.user.UserUnitSetupLayout;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface MainComponent extends CommonComponent {
    void inject(MainApplication mainApplication);

    void inject(MainActivity mainActivity);

    void inject(TopLayout topLayout);

    void inject(MenuLayout menuLayout);

    void inject(StandardLayout standardLayout);

    void inject(HomeSetView homeSetView);

    void inject(TimingLayout timingLayout);

    void inject(MiddleLeftLayout middleLeftLayout);

    void inject(MiddleRightLayout middleRightLayout);

    void inject(HrPrView hrPrView);

    void inject(StandardSpo2Layout standardSpo2Layout);

    void inject(Co2RrView co2RrView);

    void inject(NibpView nibpView);

    void inject(BasicLayout basicLayout);

    void inject(StandardSpo2BasicLayout standardSpo2BasicLayout);

    void inject(MiddleRightBasicLayout middleRightBasicLayout);

    void inject(TempCurveLayout tempCurveLayout);

    void inject(IncubatorLayout incubatorLayout);

    void inject(TimingSmallLayout timingSmallLayout);

    void inject(MonitorLayout monitorLayout);

    void inject(Spo2ListView spo2ListView);

    void inject(Co2RrTinyView co2RrTinyView);

    void inject(NibpTinyView nibpTinyView);

    void inject(WeightCurveLayout weightCurveLayout);

    void inject(Co2SurfaceView co2SurfaceView);

    void inject(BodyWaveLayout bodyWaveLayout);

    void inject(SameScreenLayout sameScreenLayout);

    void inject(Spo2ListLayout spo2ListLayout);

    void inject(Spo2Layout spo2Layout);

    void inject(PopupLayout popupLayout);

    void inject(SwitchScreenLayout switchScreenLayout);

    void inject(MenuHomeLayout menuHomeLayout);

    void inject(MenuComfortZoneLayout menuComfortZoneLayout);

    void inject(MenuSensorCalibrationLayout menuSensorCalibrationLayout);

    void inject(ConfirmSensorCalibrationLayout confirmSensorCalibrationLayout);

    void inject(MenuVersionLayout menuVersionLayout);

    void inject(MenuFunctionSetupLayout menuFunctionSetupLayout);

    void inject(MenuParameterSetupLayout menuParameterSetupLayout);

    void inject(BasePrintCurveView basePrintCurveView);

    void inject(PrintEcgWaveView printEcgWaveView);

    void inject(PrintSpo2WaveView printSpo2WaveView);

    void inject(PrintWaveView printWaveView);

    void inject(KeyboardLoginLayout keyboardLoginLayout);

    void inject(UserHomeLayout userHomeLayout);

    void inject(UserTimeLayout userTimeLayout);

    void inject(UserLanguageLayout userLanguageLayout);

    void inject(UserOverheatExperimentLayout userOverheatExperimentLayout);

    void inject(UserModuleSetupLayout userModuleSetupLayout);

    void inject(UserUnitSetupLayout userUnitSetupLayout);

    void inject(SystemHomeLayout systemHomeLayout);

    void inject(SystemDeviationAlarmLayout systemDeviationAlarmLayout);

    void inject(SystemOverheatAlarmLayout systemOverheatAlarmLayout);

    void inject(SystemRangeSetupLayout systemRangeSetupLayout);

    void inject(SystemModuleCalibrationLayout systemModuleCalibrationLayout);

    void inject(SystemSensorCalibrationLayout systemSensorCalibrationLayout);

    void inject(SystemDebugInfoLayout systemDebugInfoLayout);

    void inject(SystemFactoryLayout systemFactoryLayout);

    void inject(ConfirmFactoryLayout confirmFactoryLayout);

    void inject(SystemNibpCalibrationLayout systemNibpCalibrationLayout);

    void inject(SystemAlarmListLayout systemAlarmListLayout);

    void inject(SetupHomeIncubatorLayout setupHomeIncubatorLayout);

    void inject(SetupTempLayout setupTempLayout);

    void inject(SetupHumidityLayout setupHumidityLayout);

    void inject(SetupOxygenLayout setupOxygenLayout);

    void inject(IncubatorListLayout incubatorListLayout);

    void inject(SetupHomeLayout setupHomeLayout);

    void inject(SetupSpo2Layout setupSpo2Layout);

    void inject(SetupEcgLayout setupEcgLayout);

    void inject(SetupRespLayout setupRespLayout);

    void inject(SetupNibpLayout setupNibpLayout);

    void inject(Co2CommandControl co2CommandControl);

    void inject(WakeCommandControl wakeCommandControl);

    void inject(SetupCo2Layout setupCo2Layout);

    void inject(SetupWakeLayout setupWakeLayout);

    void inject(CameraView cameraView);

    void inject(MenuPrintSetupLayout menuPrintSetupLayout);

    void inject(SystemPrintLayout systemPrintLayout);

    void inject(PatientAddLayout patientAddLayout);

    void inject(PatientConfirmRemoveLayout patientConfirmRemoveLayout);

    void inject(PatientInfoLayout patientInfoLayout);

    void inject(PatientListLayout patientListLayout);

    void inject(PhysiologicalAlarmListLayout physiologicalAlarmListLayout);

    void inject(TechnicalAlarmListLayout technicalAlarmListLayout);

    void inject(TrendChartLayout trendChartLayout);

    void inject(TrendTableLayout trendTableLayout);

    void inject(TrendPrintTableLayout trendPrintTableLayout);

    void inject(TrendPrintLayout trendPrintLayout);

    void inject(SetupAlarmLayout setupAlarmLayout);

    void inject(SetupAlarmSpo2Layout setupAlarmSpo2Layout);

    void inject(SetupAlarmEcgLayout setupAlarmEcgLayout);

    void inject(SetupAlarmRespLayout setupAlarmRespLayout);

    void inject(SetupAlarmCo2Layout setupAlarmCo2Layout);

    void inject(SetupAlarmNibpLayout setupAlarmNibpLayout);

    void inject(CameraImageListLayout cameraImageListLayout);

    void inject(CameraVideoListLayout cameraVideoListLayout);

    void inject(CameraImageLayout cameraImageLayout);

    void inject(CameraVideoLayout cameraVideoLayout);

    void inject(SystemModuleSetupLayout systemModuleSetupLayout);
}