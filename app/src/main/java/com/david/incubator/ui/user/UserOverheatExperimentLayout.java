package com.david.incubator.ui.user;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.david.R;
import com.david.core.alarm.AlarmControl;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class UserOverheatExperimentLayout extends BaseLayout {

    @Inject
    IncubatorModel incubatorModel;
    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorCommandSender incubatorCommandSender;
    @Inject
    AlarmControl alarmControl;

    private final Button firstButton;
    private final Button secondButton;

    public UserOverheatExperimentLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.USER_OVERHEAT_EXPERIMENT);

        firstButton = ViewUtil.buildButton(getContext());
        secondButton = ViewUtil.buildButton(getContext());

        addInnerButton(0, firstButton);
        addInnerButton(1, secondButton);

        firstButton.setOnClickListener(v -> {
            int functionId = (int) v.getTag();
            click(functionId);
        });

        secondButton.setOnClickListener(v -> {
            int functionId = (int) v.getTag();
            click(functionId);
        });
    }

    @Override
    public void attach() {
        super.attach();
        if (incubatorModel.isAir()) {
            firstButton.setText(ContextUtil.getString(R.string.overheat_air_above_37));
            firstButton.setTag(0);
            secondButton.setText(ContextUtil.getString(R.string.overheat_air_below_37));
            secondButton.setTag(1);
            secondButton.setVisibility(View.VISIBLE);
        } else if (incubatorModel.isCabin() && incubatorModel.isSkin()) {
            firstButton.setText(ContextUtil.getString(R.string.overheat_skin_above_37));
            firstButton.setTag(2);
            secondButton.setVisibility(View.INVISIBLE);
        } else if (incubatorModel.isWarmer() && incubatorModel.isSkin()) {
            firstButton.setText(ContextUtil.getString(R.string.stable_state));
            firstButton.setTag(3);
            secondButton.setVisibility(View.INVISIBLE);
        }

        firstButton.setEnabled(!alarmControl.isAlarm());
        secondButton.setEnabled(!alarmControl.isAlarm());
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void click(int functionId) {
        switch (functionId) {
            case (0):
                incubatorCommandSender.setCtrlOverheat("120",
                        (aBoolean1, baseSerialMessage1) -> systemModel.lockScreen.post(true));
                break;
            case (1):
            case (2):
            case (3):
                incubatorCommandSender.setCtrlOverheat("100",
                        (aBoolean1, baseSerialMessage1) -> systemModel.lockScreen.post(true));
                break;
        }
    }
}