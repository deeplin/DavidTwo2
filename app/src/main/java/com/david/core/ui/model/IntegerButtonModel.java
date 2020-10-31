package com.david.core.ui.model;

import android.widget.Button;

import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;

public class IntegerButtonModel {

    private final Button button;
    protected Function<Integer, String> converter;
    protected int min;
    protected int max;
    protected int originValue;
    protected int newValue;
    protected int step;
    private Consumer<Integer> confirmCallback;

    public IntegerButtonModel(Button button) {
        this.button = button;
        step = 1;
        converter = String::valueOf;
    }

    public void setConverter(Function<Integer, String> converter) {
        this.converter = converter;
    }

    public void setCallback(Consumer<Integer> confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public void reNew() {
        newValue = originValue;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void increase() {
        if (newValue < max) {
            newValue += step;
        }
    }

    public void decrease() {
        if (newValue > min) {
            newValue -= step;
        }
    }

    public void setOriginValue(int originValue) {
        this.originValue = originValue;
        newValue = originValue;
        button.post(() -> button.setText(converter.apply(originValue)));
    }

    public void confirm() {
        if (newValue != originValue) {
            updateValue();
        }
    }

    protected void updateValue() {
        originValue = newValue;
        button.setText(converter.apply(originValue));
        button.setSelected(true);
        if (confirmCallback != null) {
            confirmCallback.accept(originValue);
        }
    }

    public void setDefaultColor() {
        button.setSelected(false);
    }

    public int getOriginValue() {
        return originValue;
    }

    public String getNewValue() {
        return converter.apply(newValue);
    }

    public boolean equal() {
        return newValue == originValue;
    }
}