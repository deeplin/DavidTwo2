package com.david.core.ui.layout;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;

import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.util.ILifeCycleOwner;

public abstract class BindingLayout<U extends ViewDataBinding> extends BaseLayout implements ILifeCycleOwner {

    protected U binding;

    public BindingLayout(Context context) {
        super(context);
    }

    @Override
    protected void init(LayoutPageEnum layoutPageEnum) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, layoutPageEnum.getLayoutId(), this, true);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach();
        binding.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
        binding.setLifecycleOwner(null);
    }

//    @Override
//    protected ConstraintLayout getRoot() {
//        return (ConstraintLayout) binding.getRoot();
//    }
}