package com.david.incubator.ui.system.factory;

import android.content.Context;
import android.widget.Button;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class SystemFactoryLayout extends BaseLayout {

    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorCommandSender incubatorCommandSender;

    private final Button resetLowerButton;
    private final Button backupSetupButton;
    private final Button restoreSetupSetupButton;

    public SystemFactoryLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_FACTORY);

        resetLowerButton = ViewUtil.buildButton(getContext());
        backupSetupButton = ViewUtil.buildButton(getContext());
        restoreSetupSetupButton = ViewUtil.buildButton(getContext());

        addInnerButton(0, resetLowerButton);
        addInnerButton(1, backupSetupButton);
        addInnerButton(2, restoreSetupSetupButton);

        resetLowerButton.setOnClickListener(v -> {
            systemModel.tagId = 0;
            systemModel.showLayout(LayoutPageEnum.SYSTEM_CONFIRM_FACTORY);
        });

        backupSetupButton.setOnClickListener(v -> {
            systemModel.tagId = 1;
            systemModel.showLayout(LayoutPageEnum.SYSTEM_CONFIRM_FACTORY);
        });

        restoreSetupSetupButton.setOnClickListener(v -> {
            systemModel.tagId = 2;
            systemModel.showLayout(LayoutPageEnum.SYSTEM_CONFIRM_FACTORY);
        });
    }

    @Override
    public void attach() {
        super.attach();

        resetLowerButton.setText(ContextUtil.getString(R.string.reset_lower));
        backupSetupButton.setText(ContextUtil.getString(R.string.backup_setup));
        restoreSetupSetupButton.setText(ContextUtil.getString(R.string.restore_setup));
        if (systemModel.tagId >= 100) {
            systemModel.tagId %= 100;
            switch (systemModel.tagId) {
                case (0):
                    incubatorCommandSender.factory((aBoolean, baseCommand) -> resetLowerButton.setSelected(aBoolean));
                    break;
                case (1):
                    incubatorCommandSender.backupSetup((aBoolean, baseCommand) -> backupSetupButton.setSelected(aBoolean));
                    break;
                case (2):
                    incubatorCommandSender.restoreSetup((aBoolean, baseCommand) -> restoreSetupSetupButton.setSelected(aBoolean));
                    break;
            }
        } else if (systemModel.tagId < 0) {
            resetLowerButton.setSelected(false);
            backupSetupButton.setSelected(false);
            restoreSetupSetupButton.setSelected(false);
        }
    }

    @Override
    public void detach() {
        super.detach();
    }
}