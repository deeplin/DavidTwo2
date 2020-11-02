package com.david.core.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.ViewTitleIntegerBinding;

public class TitleIntegerView extends BindingBasicLayout<ViewTitleIntegerBinding> {

    public TitleIntegerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleIconView);
        int titleHeight = typedArray.getInteger(R.styleable.TitleIconView_title_height, -1);
        int integerSize = typedArray.getInteger(R.styleable.TitleIconView_integer_size, -1);
        int integerStart = typedArray.getInteger(R.styleable.TitleIconView_integer_start, -1);
        int decimalSize = typedArray.getInteger(R.styleable.TitleIconView_decimal_size, -1);
        int uniSize = typedArray.getInteger(R.styleable.TitleIconView_unit_size, -1);
        typedArray.recycle();

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        if (titleHeight >= 0) {
            constraintSet.constrainHeight(R.id.title, titleHeight);
        }

        if (integerStart >= 0) {
            constraintSet.setMargin(binding.integerPart.getId(), ConstraintSet.START, integerStart);
        }
        constraintSet.applyTo(binding.rootView);

        if (integerSize >= 0) {
            binding.integerPart.setTextSize(integerSize);
        }
        if (decimalSize >= 0) {
            binding.decimalPart.setTextSize(decimalSize);
        }
        if (uniSize >= 0) {
            binding.unit.setTextSize(uniSize);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_title_integer;
    }

    public void set(SensorModel sensorModel) {
        binding.setSensorModel(sensorModel);
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }

    public void setSmallLayout() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        constraintSet.setMargin(binding.integerPart.getId(), ConstraintSet.TOP, 84);
        constraintSet.applyTo(binding.rootView);

        binding.integerPart.setTextSize(60);
        binding.decimalPart.setTextSize(40);

        binding.unit.setTextSize(24);
    }
}