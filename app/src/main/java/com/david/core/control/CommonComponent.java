package com.david.core.control;

import com.david.core.model.BaseSensorModel;
import com.david.core.serial.incubator.IncubatorCommandControl;
import com.david.core.serial.incubator.command.module.ModuleHardwareCommand;
import com.david.core.serial.spo2.strategy.ParameterStrategy;
import com.david.core.ui.curve.EcgSurfaceView;
import com.david.core.ui.curve.Spo2SurfaceView;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface CommonComponent {
    void inject(IncubatorCommandControl incubatorCommandControl);

    void inject(ConfigRepository configRepository);

    void inject(ModuleHardwareCommand moduleHardwareCommand);

    void inject(ParameterStrategy parameterStrategy);

    void inject(BaseSensorModel baseSensorModel);

    void inject(EcgSurfaceView ecgSurfaceView);

    void inject(Spo2SurfaceView spo2SurfaceView);
}