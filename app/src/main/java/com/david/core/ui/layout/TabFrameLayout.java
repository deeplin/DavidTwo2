package com.david.core.ui.layout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.util.ILifeCycleOwner;
import com.david.databinding.LayoutTabFrameBinding;

public class TabFrameLayout extends BindingBasicLayout<LayoutTabFrameBinding> {

    private LifecycleOwner parentOwner;
    private ILifeCycleOwner currentLayout;

    public TabFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tab_frame;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        parentOwner = lifecycleOwner;
    }

    @Override
    public void detach() {
        super.detach();
        if (currentLayout != null) {
            currentLayout.detach();
            currentLayout = null;
        }
        parentOwner = null;
    }

    public void show(int layoutId) {
        if (currentLayout != null) {
            currentLayout.detach();
            currentLayout = null;
        }
        binding.rootView.removeAllViews();

        //todo deeplin
//        currentLayout = tabControl.getLayout(layoutId);
//        binding.rootView.addView((View) currentLayout);
//        currentLayout.attach(parentOwner);
    }
}