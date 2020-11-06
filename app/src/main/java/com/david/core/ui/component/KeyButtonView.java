package com.david.core.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewKeyButtonBinding;

public class KeyButtonView extends BindingBasicLayout<ViewKeyButtonBinding> {

    private int textId;

    public KeyButtonView(Context context) {
        super(context);
    }

    public KeyButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_key_button;
    }

    public void setKey(String text) {
        binding.key.setText(text);
    }

    public void setKeyId(int textId) {
        this.textId = textId;
        setText();
    }

    public void setText() {
        setKey(ContextUtil.getString(textId));
    }

    public TextView getKey() {
        return binding.key;
    }

    public void setValue(String valueText) {
        binding.value.setText(valueText);
    }

    public Button getValue() {
        return binding.value;
    }

    public void setError(boolean error) {
        if (error) {
            binding.value.setBackgroundResource(R.drawable.alarm_high_background);
        } else {
            binding.value.setBackgroundResource(R.drawable.button_background);
        }
    }

    public void setSelected(boolean selected) {
        binding.value.setSelected(selected);
    }

    public void setBigFont() {
        binding.key.setTextSize(40);
        binding.value.setTextSize(40);
    }
}