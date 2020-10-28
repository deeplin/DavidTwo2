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
import com.david.databinding.ViewSensorRangeBinding;

public class SensorRangeView extends BindingBasicLayout<ViewSensorRangeBinding> implements IDisableView {

    private boolean uniqueColor;

    public SensorRangeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleIconView);
        int integerTop = typedArray.getInteger(R.styleable.TitleIconView_integer_top, -1);
        int integerSize = typedArray.getInteger(R.styleable.TitleIconView_integer_size, -1);
        int decimalSize = typedArray.getInteger(R.styleable.TitleIconView_decimal_size, -1);
        typedArray.recycle();

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        if (integerTop >= 0) {
            constraintSet.setMargin(binding.integerPart.getId(), ConstraintSet.TOP, integerTop);
        }
        constraintSet.applyTo(binding.rootView);

        if (integerSize >= 0) {
            binding.integerPart.setTextSize(integerSize);
        }

        if (decimalSize >= 0) {
            binding.decimalPart.setTextSize(decimalSize);
        }
    }

    private int titleBackgroundResource = R.drawable.background_panel_pink;

    @Override
    protected int getLayoutId() {
        return R.layout.view_sensor_range;
    }

    public void set(SensorModel sensorModel) {
        binding.setViewModel(sensorModel);
        if (uniqueColor) {
            binding.integerPart.setTextColor(sensorModel.getSensorModelEnum().getUniqueColor());
            binding.decimalPart.setTextColor(sensorModel.getSensorModelEnum().getUniqueColor());
        }
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
    }

    @Override
    public void detach() {
        super.detach();
    }

    @Override
    public void setDisable(boolean status, int backgroundColor) {
        if (status) {
            binding.title.setBackgroundResource(R.drawable.background_panel_dark);
            binding.unit.setVisibility(View.INVISIBLE);
            binding.integerPart.setVisibility(View.INVISIBLE);
            binding.decimalPart.setVisibility(View.INVISIBLE);
            binding.upperLimit.setVisibility(View.INVISIBLE);
            binding.lowerLimit.setVisibility(View.INVISIBLE);
        } else {
            binding.title.setBackgroundResource(titleBackgroundResource);
            binding.unit.setVisibility(View.VISIBLE);
            binding.integerPart.setVisibility(View.VISIBLE);
            binding.decimalPart.setVisibility(View.VISIBLE);
            binding.upperLimit.setVisibility(View.VISIBLE);
            binding.lowerLimit.setVisibility(View.VISIBLE);
        }
    }

    public void setTitleBackground(int backgroundResource) {
        titleBackgroundResource = backgroundResource;
        binding.title.setBackgroundResource(backgroundResource);
    }

    public void setSpo2SmallLayout() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.TOP, 8);
        constraintSet.setMargin(binding.title.getId(), ConstraintSet.START, 4);
        constraintSet.applyTo(binding.rootView);

        binding.title.setTextSize(16);
        binding.unit.setTextSize(16);
        binding.integerPart.setTextSize(46);
        binding.decimalPart.setTextSize(34);
        binding.upperLimit.setTextSize(16);
        binding.lowerLimit.setTextSize(16);
    }

    public void setFontSize(int integerPartSize, int decimalPartSize) {
        binding.integerPart.setTextSize(integerPartSize);
        binding.decimalPart.setTextSize(decimalPartSize);
    }

    public void setIntegerTop(int integerTop) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.rootView);
        if (integerTop >= 0) {
            constraintSet.setMargin(binding.integerPart.getId(), ConstraintSet.TOP, integerTop);
        }
        constraintSet.applyTo(binding.rootView);
    }

    public void setUniqueColor() {
        uniqueColor = true;
    }
}