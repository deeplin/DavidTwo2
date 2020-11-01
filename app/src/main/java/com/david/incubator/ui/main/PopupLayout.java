package com.david.incubator.ui.main;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.databinding.LayoutPopupBinding;
import com.david.incubator.ui.menu.SwitchScreenLayout;

import javax.inject.Inject;

public class PopupLayout extends BindingBasicLayout<LayoutPopupBinding> {

    @Inject
    SystemModel systemModel;

    private View currentLayout;
    private final View[] views =
            new View[LayoutPageEnum.LAYOUT_NONE.ordinal() - LayoutPageEnum.MENU_HOME.ordinal()];

    public PopupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        views[LayoutPageEnum.SWITCH_SCREEN.ordinal()] = new SwitchScreenLayout(getContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_popup;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
//        binding.incubatorVerticalListLayout.attach(lifeCycleOwner);
        currentLayout = views[systemModel.layoutPage.getValue().ordinal() - LayoutPageEnum.MENU_HOME.ordinal()];
        ((ILifeCycle) currentLayout).attach();
        binding.mainFrameLayout.addView((View) currentLayout);
    }

    @Override
    public void detach() {
        super.detach();
        binding.mainFrameLayout.removeAllViews();
        ((ILifeCycle) currentLayout).detach();
        currentLayout = null;
//        binding.incubatorVerticalListLayout.detach();
    }
}