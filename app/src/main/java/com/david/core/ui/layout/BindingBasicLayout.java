package com.david.core.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.util.ILifeCycleOwner;

public abstract class BindingBasicLayout<U extends ViewDataBinding> extends ConstraintLayout implements ILifeCycleOwner {

    protected final U binding;

    public BindingBasicLayout(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, true);
        setClickable(true);
    }

    public BindingBasicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), this, true);
        setClickable(true);
    }

    protected abstract int getLayoutId();

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        binding.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    public void detach() {
        binding.setLifecycleOwner(null);
    }

    public void setSensorDarkMode(boolean darkMode) {
        if (darkMode) {
            binding.getRoot().setBackgroundResource(R.drawable.background_panel_dark);
        } else {
            binding.getRoot().setBackgroundResource(R.drawable.background_panel);
        }
    }
}