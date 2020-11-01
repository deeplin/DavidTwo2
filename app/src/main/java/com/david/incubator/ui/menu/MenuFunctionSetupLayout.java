package com.david.incubator.ui.menu;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.component.TextTwoButtonView;
import com.david.core.ui.layout.BindingLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.ViewUtil;
import com.david.databinding.LayoutCommonBinding;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class MenuFunctionSetupLayout extends BindingLayout<LayoutCommonBinding> {

    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    ConfigRepository configRepository;
    @Inject
    SystemModel systemModel;

    private final TextTwoButtonView alarmVolumeView;

    private final Button darkButton;

    public MenuFunctionSetupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.MENU_FUNCTION_SETUP);

        loadLiveDataItems(KeyButtonEnum.MENU_FUNCTION_SETUP_SCREEN_LUMINANCE, 4, null, null);

        setOriginValue(0, ConfigEnum.ScreenLuminance);
        setOriginValue(1, ConfigEnum.NotificationVolume);
        setOriginValue(2, ConfigEnum.PulseVolume);
        setOriginValue(3, ConfigEnum.ApgarVolume);

        alarmVolumeView = new TextTwoButtonView(getContext(), null);
        alarmVolumeView.setId(View.generateViewId());
        addInnerView(4, alarmVolumeView);

        darkButton = ViewUtil.buildButton(getContext());
        addInnerButton(5, darkButton);
        darkButton.setOnClickListener(v -> {
            if (systemModel.darkMode.getValue()) {
                systemModel.darkMode.set(false);
                darkButton.setText(ContextUtil.getString(R.string.day_mode));
            } else {
                systemModel.darkMode.set(true);
                darkButton.setText(ContextUtil.getString(R.string.night_mode));
            }
        });
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);

        alarmVolumeView.setKeyId(R.string.alarm_volume);
        alarmVolumeView.setValue(ContextUtil.getString(ListUtil.volumeList.get(0)), ContextUtil.getString(ListUtil.volumeList.get(1)));
        alarmVolumeView.setCallback(this::setAlarmVolume);
        alarmVolumeView.select(configRepository.getConfig(ConfigEnum.AlarmVolume).getValue());

        if (systemModel.darkMode.getValue()) {
            darkButton.setText(ContextUtil.getString(R.string.night_mode));
        } else {
            darkButton.setText(ContextUtil.getString(R.string.day_mode));
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void setAlarmVolume(Integer optionId) {
        incubatorCommandSender.setAlarmVolume(optionId, (aBoolean, baseCommand) -> {
            if (aBoolean) {
                configRepository.getConfig(ConfigEnum.AlarmVolume).post(optionId);
                Observable.just(optionId).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> alarmVolumeView.select(optionId));
            }
        });
    }
}