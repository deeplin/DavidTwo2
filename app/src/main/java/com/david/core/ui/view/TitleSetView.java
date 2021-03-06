package com.david.core.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.IDisableView;
import com.david.databinding.ViewTitleSetBinding;

public class TitleSetView extends BindingBasicLayout<ViewTitleSetBinding> implements IDisableView {

    public TitleSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleIconView);
        int titleTop = typedArray.getInteger(R.styleable.TitleIconView_title_top, -1);
        int titleWidth = typedArray.getInteger(R.styleable.TitleIconView_title_width, -1);
        int integerTop = typedArray.getInteger(R.styleable.TitleIconView_integer_top, -1);
        int integerSize = typedArray.getInteger(R.styleable.TitleIconView_integer_size, -1);
        typedArray.recycle();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        if (titleTop >= 0) {
            constraintSet.setMargin(R.id.title, ConstraintSet.TOP, titleTop);
        }
        if (titleWidth >= 0) {
            constraintSet.constrainWidth(R.id.title, titleWidth);
        }
        if (integerTop >= 0) {
            constraintSet.setMargin(R.id.integerPart, ConstraintSet.TOP, integerTop);
        }
        constraintSet.applyTo(binding.rootView);
        if (integerSize >= 0) {
            binding.integerPart.setTextSize(integerSize);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_title_set;
    }

    public void set(SensorModel sensorModel) {
        binding.setViewModel(sensorModel);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }

    public void setDisable(boolean status, int backgroundColorId) {
        if (status) {
            binding.title.setBackgroundResource(R.drawable.background_panel_dark);
            binding.set.setVisibility(View.INVISIBLE);
            binding.objective.setVisibility(View.INVISIBLE);
            binding.integerPart.setVisibility(View.INVISIBLE);
            binding.unit.setVisibility(View.INVISIBLE);
        } else {
            binding.title.setBackgroundResource(backgroundColorId);
            binding.set.setVisibility(View.VISIBLE);
            binding.objective.setVisibility(View.VISIBLE);
            binding.integerPart.setVisibility(View.VISIBLE);
            binding.unit.setVisibility(View.VISIBLE);
        }
    }
}