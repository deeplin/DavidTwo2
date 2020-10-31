package com.david.core.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.core.util.Consumer;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewTextTwoButtonBinding;

public class TextTwoButtonView extends BindingBasicLayout<ViewTextTwoButtonBinding> {

    private Consumer<Integer> callback;
    private int optionId;

    public TextTwoButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        binding.leftButton.setOnClickListener(this::click);
        binding.rightButton.setOnClickListener(this::click);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_text_two_button;
    }

    public TextView getTextView() {
        return binding.key;
    }

    public void setKeyId(int textId) {
        setKey(ContextUtil.getString(textId));
    }

    public void setKey(String text) {
        binding.key.setText(text);
    }

    public void setValue(String leftText, String rightText) {
        binding.leftButton.setText(leftText);
        binding.rightButton.setText(rightText);
    }

    public int getSelect() {
        return optionId;
    }

    public void select(int optionId) {
        this.optionId = optionId;
        if (optionId == 0) {
            binding.leftButton.setBackgroundResource(R.drawable.background_panel);
            binding.leftButton.setTextColor(ContextUtil.getColor(R.color.text_blue));
            binding.rightButton.setBackgroundResource(R.drawable.background_panel_white);
            binding.rightButton.setTextColor(ContextUtil.getColor(R.color.button_background_disable));
        } else {
            binding.leftButton.setBackgroundResource(R.drawable.background_panel_white);
            binding.leftButton.setTextColor(ContextUtil.getColor(R.color.button_background_disable));
            binding.rightButton.setBackgroundResource(R.drawable.background_panel);
            binding.rightButton.setTextColor(ContextUtil.getColor(R.color.text_blue));
        }
    }

    private void click(View v) {
        optionId = (optionId + 1) % 2;
        callback.accept(optionId);
    }

    public void setCallback(Consumer<Integer> callback) {
        this.callback = callback;
    }

    public void disable() {
        binding.leftButton.setOnClickListener(null);
        binding.rightButton.setOnClickListener(null);
    }
}