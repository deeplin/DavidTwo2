package com.david.core.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewTitleBinding;

import javax.inject.Inject;

public class TitleView extends BindingBasicLayout<ViewTitleBinding> {

    @Inject
    SystemModel systemModel;

    private LayoutPageEnum reversePageEnum = LayoutPageEnum.LAYOUT_NONE;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        binding.close.setOnClickListener(view -> {
            systemModel.showLayout(LayoutPageEnum.LAYOUT_NONE);
        });
        binding.reverse.setOnClickListener(view -> systemModel.showLayout(reversePageEnum));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_title;
    }

    public void set(int titleTextId, LayoutPageEnum reversePageEnum, boolean showReverse, boolean showClose) {
        set(titleTextId, reversePageEnum);
        if (!showReverse) {
            binding.reverse.setVisibility(View.GONE);
        }
        if (!showClose) {
            binding.close.setVisibility(View.GONE);
        }
    }

    public void set(int titleTextId, LayoutPageEnum reversePageEnum) {
        binding.title.setText(ContextUtil.getString(titleTextId));
        this.reversePageEnum = reversePageEnum;
    }
}