package com.david.incubator.ui.setup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.SetupPageEnum;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ILifeCycle;
import com.david.core.util.ILifeCycleOwner;
import com.david.databinding.LayoutTabFrameBinding;

public class TabFrameLayout extends BindingBasicLayout<LayoutTabFrameBinding> {

    private LifecycleOwner lifeCycleOwner;
    private View currentLayout;

    private final View[] views = new View[SetupPageEnum.Wake.ordinal() + 1];

    public TabFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        views[SetupPageEnum.Temp.ordinal()] = new SetupTempLayout(getContext());
        views[SetupPageEnum.Humidity.ordinal()] = new SetupHumidityLayout(getContext());
        views[SetupPageEnum.Oxygen.ordinal()] = new SetupOxygenLayout(getContext());

        views[SetupPageEnum.Spo2.ordinal()] = new SetupSpo2Layout(getContext());
        views[SetupPageEnum.Ecg.ordinal()] = new SetupEcgLayout(getContext());
        views[SetupPageEnum.Resp.ordinal()] = new SetupRespLayout(getContext());
        views[SetupPageEnum.Co2.ordinal()] = new SetupCo2Layout(getContext());
        views[SetupPageEnum.Nibp.ordinal()] = new SetupNibpLayout(getContext());
        views[SetupPageEnum.Wake.ordinal()] = new SetupWakeLayout(getContext());

        for (int index = 0; index < views.length; index++) {
            View view = views[index];
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
                binding.rootView.addView(view);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tab_frame;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        this.lifeCycleOwner = lifeCycleOwner;
    }

    @Override
    public void detach() {
        super.detach();
        if (currentLayout instanceof ILifeCycleOwner) {
            ((ILifeCycleOwner) currentLayout).detach();
        } else if (currentLayout instanceof ILifeCycle) {
            ((ILifeCycle) currentLayout).detach();
        }
        currentLayout.setVisibility(View.INVISIBLE);
        lifeCycleOwner = null;
    }

    public void show(int layoutId) {
        if (currentLayout != null) {
            if (currentLayout instanceof ILifeCycleOwner) {
                ((ILifeCycleOwner) currentLayout).detach();
            } else if (currentLayout instanceof ILifeCycle) {
                ((ILifeCycle) currentLayout).detach();
            }
            currentLayout.setVisibility(View.INVISIBLE);
            currentLayout = null;
        }

        currentLayout = views[layoutId];

        if (currentLayout instanceof ILifeCycleOwner) {
            ((ILifeCycleOwner) currentLayout).attach(lifeCycleOwner);
        } else if (currentLayout instanceof ILifeCycle) {
            ((ILifeCycle) currentLayout).attach();
        }
        currentLayout.setVisibility(View.VISIBLE);
    }
}