package com.david.incubator.ui.system.factory;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;
import com.david.incubator.ui.menu.sensorcalibration.ConfirmLayout;

import javax.inject.Inject;

public class ConfirmFactoryLayout extends ConfirmLayout {

    @Inject
    SystemModel systemModel;

    public ConfirmFactoryLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.SYSTEM_CONFIRM_FACTORY);

        binding.cancel.setOnClickListener(v -> systemModel.showLayout(LayoutPageEnum.SYSTEM_FACTORY));

        binding.confirm.setOnClickListener(v -> {
            systemModel.tagId += 100;
            systemModel.showLayout(LayoutPageEnum.SYSTEM_FACTORY);
        });
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        switch (systemModel.tagId) {
            case (0):
                binding.confirmText.setText(ContextUtil.getString(R.string.reset_lower_confirm));
                break;
            case (1):
                binding.confirmText.setText(ContextUtil.getString(R.string.backup_setup_confirm));
                break;
            case (2):
                binding.confirmText.setText(ContextUtil.getString(R.string.restore_setup_confirm));
                break;
        }
    }

    @Override
    public void detach() {
        super.detach();
    }
}
