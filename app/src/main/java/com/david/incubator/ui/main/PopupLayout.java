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
import com.david.core.util.ILifeCycleOwner;
import com.david.databinding.LayoutPopupBinding;
import com.david.incubator.ui.menu.MenuHomeLayout;
import com.david.incubator.ui.menu.SwitchScreenLayout;

import javax.inject.Inject;

public class PopupLayout extends BindingBasicLayout<LayoutPopupBinding> {

    private final int POPUP_START_ID = LayoutPageEnum.MENU_HOME.ordinal();

    @Inject
    SystemModel systemModel;

    private View currentLayout;
    private final View[] views =
            new View[LayoutPageEnum.LAYOUT_NONE.ordinal() - POPUP_START_ID];

    public PopupLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        views[LayoutPageEnum.SWITCH_SCREEN.ordinal() - POPUP_START_ID] = new SwitchScreenLayout(getContext());
        views[LayoutPageEnum.MENU_HOME.ordinal() - POPUP_START_ID] = new MenuHomeLayout(getContext());

        for (int index = 0; index < views.length; index++) {
            View view = views[index];
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
                binding.mainFrameLayout.addView(view);
            }
        }
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
        if (currentLayout instanceof ILifeCycleOwner) {
            ((ILifeCycleOwner) currentLayout).attach(lifeCycleOwner);
        } else if (currentLayout instanceof ILifeCycle) {
            ((ILifeCycle) currentLayout).attach();
        }

        currentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void detach() {
        super.detach();
        currentLayout.setVisibility(View.INVISIBLE);
        if (currentLayout instanceof ILifeCycleOwner) {
            ((ILifeCycleOwner) currentLayout).detach();
        } else if (currentLayout instanceof ILifeCycle) {
            ((ILifeCycle) currentLayout).detach();
        }
        currentLayout = null;
        //        binding.incubatorVerticalListLayout.attach(lifeCycleOwner);
    }
}