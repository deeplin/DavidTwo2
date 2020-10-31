package com.david.core.ui.model;

import android.view.View;

import com.david.core.ui.component.KeyButtonView;

public class IntegerPopupModel extends IntegerButtonModel {

    protected final KeyButtonView keyButtonView;
    private int min2;
    private int max2;

    public IntegerPopupModel(KeyButtonView keyButtonView) {
        super(keyButtonView.getValue());
        this.keyButtonView = keyButtonView;
    }

    public KeyButtonView getKeyButtonView() {
        return keyButtonView;
    }

    @Override
    public void setMin(int min) {
        super.setMin(min);
        this.min2 = Integer.MIN_VALUE;
    }

    @Override
    public void setMax(int max) {
        super.setMax(max);
        this.max2 = Integer.MAX_VALUE;
    }

    public void setMin2(int min2) {
        this.min2 = min2;
    }

    public void setMax2(int max2) {
        this.max2 = max2;
    }

    @Override
    public void increase() {
        if (newValue < max && newValue < max2 - step) {
            newValue += step;
        }
    }

    @Override
    public void decrease() {
        if (newValue > min && newValue > min2 + step) {
            newValue -= step;
        }
    }

    @Override
    public void setDefaultColor() {
        super.setDefaultColor();
        keyButtonView.setError(false);
    }
}