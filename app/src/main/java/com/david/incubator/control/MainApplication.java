package com.david.incubator.control;

import android.app.Application;

import androidx.lifecycle.Observer;

import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.database.DaoControl;
import com.david.core.enumeration.LanguageEnum;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandControl;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.IntervalUtil;
import com.david.core.util.rely.BeepUtil;
import com.david.core.util.rely.SoundUtil;

import javax.inject.Inject;

import android_serialport_api.SerialPort;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainApplication extends Application {


    @Inject
    IntervalUtil intervalUtil;
    @Inject
    DaoControl daoControl;
    @Inject
    IncubatorCommandControl incubatorCommandControl;
    @Inject
    SystemModel systemModel;
    @Inject
    ConfigRepository configRepository;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    AutomationControl automationControl;
    @Inject
    DemoControl demoControl;
    @Inject
    SoundUtil soundUtil;
    @Inject
    BeepUtil beepUtil;

    private Observer<Boolean> demoCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        SerialPort.loadLibrary();
        ContextUtil.initialize(this);
        ContextUtil.getComponent().inject(this);

        LanguageEnum languageEnum = LanguageEnum.getLanguage();
        LanguageEnum.setLanguage(languageEnum.ordinal());
        sensorModelRepository.setText();

        intervalUtil.attach();

        soundUtil.attach();
        beepUtil.attach();

        demoCallback = aBoolean -> {
            if (systemModel.demo.getValue()) {
                demoControl.attach();
            } else {
                demoControl.detach();
            }
        };

        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            daoControl.start(MainApplication.this);
            configRepository.attach();
            emitter.onNext(0);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> start());
    }

    private void start() throws Exception {
        incubatorCommandControl.init();
        incubatorCommandControl.open(Constant.INCUBATOR_COM_ID, Constant.INCUBATOR_BAUDRATE);
        automationControl.attach();

        systemModel.demo.observeForever(demoCallback);
    }

    public void stop() {
        systemModel.demo.removeObserver(demoCallback);

        automationControl.detach();
        incubatorCommandControl.close();

        configRepository.detach();
        daoControl.stop();

        beepUtil.detach();
        soundUtil.detach();
        intervalUtil.detach();
    }
}