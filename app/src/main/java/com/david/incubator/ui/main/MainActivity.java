package com.david.incubator.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.SystemEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.ILifeCycleOwner;
import com.david.core.util.IntervalUtil;
import com.david.databinding.ActivityMainBinding;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.home.basic.BasicLayout;
import com.david.incubator.ui.home.standard.StandardLayout;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.Consumer;

public class MainActivity extends AppCompatActivity implements ILifeCycle, Consumer<Long> {

    @Inject
    ConfigRepository configRepository;
    @Inject
    SystemModel systemModel;
    @Inject
    IntervalUtil intervalUtil;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    ModuleHardware moduleHardware;

    private ActivityMainBinding binding;
    private ILifeCycleOwner currentLayout;

    private Observer<Integer> systemStartCallback;
    private Observer<LayoutPageEnum> layoutIdCallback;
    private Observer<Integer> screenLuminanceCallback;
    private Observer<SystemEnum> systemEnumObserver;
    private Observer<Boolean> demoObserver;

    private Observer<Boolean> darkObserver;

    private final ILifeCycleOwner[] views = new ILifeCycleOwner[LayoutPageEnum.NONE.ordinal()];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextUtil.getComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        systemStartCallback = integer -> {
            if (integer == 3) {
                attach();
                binding.startImage.setVisibility(View.GONE);
            }
        };

        layoutIdCallback = layoutPageEnum -> {
            if (currentLayout != null) {
                currentLayout.detach();
                ((View) currentLayout).setVisibility(View.INVISIBLE);
                currentLayout = null;
            }

            if (layoutPageEnum.ordinal() >= LayoutPageEnum.LAYOUT_ALL.ordinal()) {
                currentLayout = views[LayoutPageEnum.LAYOUT_ALL.ordinal()];
            } else {
                currentLayout = views[layoutPageEnum.ordinal()];
            }
            currentLayout.attach(this);
            ((View) currentLayout).setVisibility(View.VISIBLE);
        };

        screenLuminanceCallback = integer -> {
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = integer / 5.0f;
            window.setAttributes(layoutParams);
        };

        systemEnumObserver = systemEnum -> {
            if (Objects.equals(systemEnum, SystemEnum.Transit)) {
                binding.transitImage.setVisibility(View.VISIBLE);
            } else {
                binding.transitImage.setVisibility(View.INVISIBLE);
            }
        };

        demoObserver = aBoolean -> binding.demoView.setVisibility(aBoolean ? View.VISIBLE : View.GONE);

        darkObserver = aBoolean -> {
            binding.menuLayout.setDarkMode(aBoolean);
            if (aBoolean) {
                binding.getRoot().setBackgroundResource(R.color.background_dark);
            } else {
                binding.getRoot().setBackgroundResource(R.color.background);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        configRepository.getConfig(ConfigEnum.ScreenLuminance).observeForever(screenLuminanceCallback);
        systemModel.systemInitState.observeForever(systemStartCallback);
        incubatorModel.systemMode.observeForever(systemEnumObserver);
        systemModel.demo.observeForever(demoObserver);
        systemModel.darkMode.observeForever(darkObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        systemModel.darkMode.removeObserver(darkObserver);
        systemModel.demo.removeObserver(demoObserver);
        incubatorModel.systemMode.removeObserver(systemEnumObserver);
        systemModel.systemInitState.removeObserver(systemStartCallback);
        configRepository.getConfig(ConfigEnum.ScreenLuminance).removeObserver(screenLuminanceCallback);

        detach();
        if (currentLayout != null) {
            currentLayout.detach();
            currentLayout = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication) getApplication()).stop();
    }

    @Override
    public void attach() {
        buildViews();

        binding.topLayout.attach(this);
        binding.menuLayout.attach(this);
        intervalUtil.addSecondConsumer(MainActivity.class, this);

        systemModel.layoutPage.observeForever(layoutIdCallback);
    }

    @Override
    public void detach() {
        systemModel.layoutPage.removeObserver(layoutIdCallback);

        intervalUtil.removeSecondConsumer(MainActivity.class);
        binding.menuLayout.detach();
        binding.topLayout.detach();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                systemModel.initializeTimeOut();
                break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void accept(Long aLong) {
        systemModel.increaseLockTime();
    }

    private void buildViews() {
        Context applicationContext = ContextUtil.getApplicationContext();
        views[LayoutPageEnum.LAYOUT_STANDARD.ordinal()] = new StandardLayout(applicationContext);
        views[LayoutPageEnum.LAYOUT_BASIC.ordinal()] = new BasicLayout(applicationContext);
//        views[LayoutPageEnum.LAYOUT_STANDARD_BASIC.ordinal()] = new StandardBasicLayout(applicationContext);
//        views[LayoutPageEnum.LAYOUT_TEMP_CURVE.ordinal()] = new TempCurveLayout(applicationContext);
//        views[LayoutPageEnum.LAYOUT_WEIGHT_CURVE.ordinal()] = new WeightCurveLayout(applicationContext);
//        views[LayoutPageEnum.LAYOUT_BODY_WAVE.ordinal()] = new BodyWaveLayout(applicationContext);
//        views[LayoutPageEnum.LAYOUT_SAME_SCREEN.ordinal()] = new SameScreenLayout(applicationContext);
//        views[LayoutPageEnum.LAYOUT_SPO2.ordinal()] = new Spo2Layout(applicationContext);
//        views[LayoutPageEnum.LAYOUT_CAMERA.ordinal()] = new CameraLayout(applicationContext);

        for (int index = 0; index < views.length; index++) {
            View lifeCycleOwner = (View) views[index];
            if (lifeCycleOwner != null) {
                lifeCycleOwner.setVisibility(View.INVISIBLE);
                binding.mainFrameLayout.addView((View) lifeCycleOwner);
            }
        }
    }
}
