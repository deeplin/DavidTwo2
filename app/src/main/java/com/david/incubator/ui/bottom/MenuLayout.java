package com.david.incubator.ui.bottom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.alarm.AlarmControl;
import com.david.core.alarm.AlarmModel;
import com.david.core.control.ModuleHardware;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycleOwner;
import com.david.databinding.LayoutMenuBinding;
import com.david.incubator.serial.nibp.NibpCommandControl;
import com.david.incubator.ui.top.TopViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class MenuLayout extends LinearLayout implements ILifeCycleOwner {

    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorModel incubatorModel;
    @Inject
    TopViewModel topViewModel;
    @Inject
    AlarmControl alarmControl;
    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    ModuleHardware moduleHardware;
    //    @Inject
//    TrendControlModel trendControlModel;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    NibpCommandControl nibpCommandControl;

    private int startId = 0;
    private int initialStartId = 0;
    private final List<Pair<ImageButton, TextView>> pairList = new ArrayList<>();

    private final Observer<Boolean> lockScreenCallback;
    private final Observer nibpCallback;
    private final LayoutMenuBinding binding;

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = LayoutMenuBinding.inflate(layoutInflater, this, true);

        Pair<ImageButton, TextView> pair0 = new Pair(binding.menuButton0, binding.menuText0);
        Pair<ImageButton, TextView> pair1 = new Pair(binding.menuButton1, binding.menuText1);
        Pair<ImageButton, TextView> pair2 = new Pair(binding.menuButton2, binding.menuText2);
        pairList.add(pair0);
        pairList.add(pair1);
        pairList.add(pair2);

        binding.menuButton0.setOnClickListener(v -> click(0));
        binding.menuButton1.setOnClickListener(v -> click(1));
        binding.menuButton2.setOnClickListener(v -> click(2));

        binding.lockScreen.setOnClickListener(v -> {
            if (incubatorModel.ohTest.getValue()) {
                return;
            }
            if (incubatorModel.isTransit()) {
                return;
            }
            synchronized (systemModel.lockScreen) {
                systemModel.lockScreen.set(!systemModel.lockScreen.getValue());
            }
        });

        binding.home.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.LAYOUT_STANDARD));

        binding.setting.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.MENU_HOME));

        binding.muteAlarm.setOnClickListener(v -> {
            if (!incubatorModel.isTransit()) {
                topViewModel.muteAlarm();
            }
        });

        binding.clearAlarm.setOnClickListener(v -> {
            if (incubatorModel.ohTest.getValue()) {
                systemModel.lockScreen.set(false);
            }
            AlarmModel topAlarm = alarmControl.getTopTechnicalAlarm();
            if (topAlarm != null && Objects.equals(topAlarm.getAlarmWord(), "AIR.OVH")) {
                SensorModel airModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
                if (airModel.textNumber.getValue() < 370) {
                    incubatorCommandSender.clearAlarm("AIR.OVH", (aBoolean, baseSerialMessage) -> topViewModel.clearAlarm());
                }
            }
            if (topAlarm != null && Objects.equals(topAlarm.getAlarmWord(), "SKIN.OVH")) {
                SensorModel skinModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
                if (skinModel.textNumber.getValue() < 380) {
                    incubatorCommandSender.clearAlarm("SKIN.OVH", (aBoolean, baseSerialMessage) -> topViewModel.clearAlarm());
                }
            }
            if (topAlarm != null && Objects.equals(topAlarm.getAlarmWord(), "CTRL.MAN")) {
                incubatorCommandSender.clearAlarm("CTRL.MAN", (aBoolean, baseSerialMessage) -> topViewModel.clearAlarm());
            }

            systemModel.closePopup();
        });

        lockScreenCallback = aBoolean -> {
            if (aBoolean) {
                systemModel.closePopup();
            }
            setVisible(!aBoolean);
        };

        binding.leftWards.setOnClickListener(v -> {
            startId -= 3;
            if (startId < 0) {
                if (initialStartId == 0) {
                    startId = 6;
                } else {
                    startId = 4;
                }
            }
            displayButton();
        });

        binding.rightwards.setOnClickListener(v -> {
            startId += 3;
            if (startId >= MenuEnum.values().length) {
                startId = initialStartId;
            }
            displayButton();
        });

        nibpCallback = obj -> {
            if (systemModel.layoutPage.getValue() == LayoutPageEnum.LAYOUT_SPO2) {
                initialStartId = 1;
                startId = 1;
            } else if (!moduleHardware.isActive(ModuleEnum.Nibp)) {
                initialStartId = 1;
                startId = 1;
            } else {
                initialStartId = 0;
                startId = 0;
            }
            displayButton();
        };
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        systemModel.lockScreen.observeForever(lockScreenCallback);
        moduleHardware.getModule(ModuleEnum.Nibp).observeForever(nibpCallback);
        systemModel.layoutPage.observeForever(nibpCallback);
    }

    @Override
    public void detach() {
        systemModel.layoutPage.removeObserver(nibpCallback);
        moduleHardware.getModule(ModuleEnum.Nibp).removeObserver(nibpCallback);
        systemModel.lockScreen.removeObserver(lockScreenCallback);
    }

    private void click(int iconId) {
        int functionId = iconId + startId;
        switch (functionId) {
            case (0):
                nibpCommandControl.press();
                break;
            case (1):
                systemModel.showLayout(LayoutPageEnum.SWITCH_SCREEN);
                break;
            case (2):
                systemModel.freezeWave.set(!systemModel.freezeWave.getValue());
                break;
            case (3):
//                trendControlModel.init();
//                systemModel.showLayout(LayoutPageEnum.TREND_CHART);
                break;
            case (4):
                if (systemModel.layoutPage.getValue() == LayoutPageEnum.LAYOUT_CAMERA) {
                    systemModel.showLayout(LayoutPageEnum.LAYOUT_STANDARD);
                } else {
                    systemModel.showLayout(LayoutPageEnum.LAYOUT_CAMERA);
                }
                break;
            case (5):
                systemModel.showLayout(LayoutPageEnum.MENU_PRINT_SETUP);
                break;
            case (6):
                systemModel.showSetupPage(SetupPageEnum.Spo2);
                break;
        }
    }

    private void setVisible(boolean status) {
        if (status) {
            binding.backgroundView.setVisibility(VISIBLE);
            binding.lockScreen.setImageResource(R.mipmap.unlock_screen);
            binding.leftWards.setVisibility(VISIBLE);
            binding.rightwards.setVisibility(VISIBLE);
            for (Pair<ImageButton, TextView> pair : pairList) {
                pair.first.setVisibility(VISIBLE);
                pair.second.setVisibility(VISIBLE);
            }
            incubatorModel.KANG.post(false);
            incubatorCommandSender.resumeAlarm("ALL");
        } else {
            binding.backgroundView.setVisibility(INVISIBLE);
            binding.lockScreen.setImageResource(R.mipmap.lock_screen);
            binding.leftWards.setVisibility(INVISIBLE);
            binding.rightwards.setVisibility(INVISIBLE);
            for (Pair<ImageButton, TextView> pair : pairList) {
                pair.first.setVisibility(INVISIBLE);
                pair.second.setVisibility(INVISIBLE);
            }
        }
    }

    public void setDarkMode(boolean darkMode) {
        if (darkMode) {
            binding.backgroundView.setBackgroundResource(R.drawable.background_panel_dark);
        } else {
            binding.backgroundView.setBackgroundResource(R.drawable.background_panel_pink);
        }
    }

    private void displayButton() {
        MenuEnum[] menuEnums = MenuEnum.values();
        for (int index = 0; index < pairList.size(); index++) {
            Pair<ImageButton, TextView> pair = pairList.get(index);
            int pairId = startId + index;
            if (pairId < menuEnums.length) {
                pair.first.setImageResource(menuEnums[pairId].getImageId());
                pair.second.setText(ContextUtil.getString(menuEnums[startId + index].getTextId()));
                pair.first.setVisibility(VISIBLE);
                pair.second.setVisibility(VISIBLE);
            } else {
                pair.first.setVisibility(INVISIBLE);
                pair.second.setVisibility(INVISIBLE);
            }
        }
    }
}