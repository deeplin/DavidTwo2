package com.david.core.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.databinding.ViewButtonValueBinding;

public class ButtonValueView extends BindingBasicLayout<ViewButtonValueBinding> {

    public ButtonValueView(Context context) {
        super(context);
    }

    public ButtonValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_button_value;
    }

    public void setValue(String text) {
        binding.value.setText(text);
    }

    public Button getButton() {
        return binding.keyButton;
    }
}