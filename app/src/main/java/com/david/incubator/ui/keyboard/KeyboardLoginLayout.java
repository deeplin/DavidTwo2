package com.david.incubator.ui.keyboard;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.databinding.LayoutKeyboardLoginBinding;

import javax.inject.Inject;

public class KeyboardLoginLayout extends BindingBasicLayout<LayoutKeyboardLoginBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    IncubatorCommandSender incubatorCommandSender;

    private final StringBuilder password;
    private final StringBuilder passwordMask;

    public KeyboardLoginLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        password = new StringBuilder();
        passwordMask = new StringBuilder();

        binding.keyboardLayout.set(4, 3);

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 3; column++) {
                int id = row * 3 + column + 1;

                View view;
                switch (id) {
                    case (10):
                        ImageButton backspaceButton = new ImageButton(getContext());
                        backspaceButton.setImageResource(R.mipmap.backspace);
                        backspaceButton.setOnClickListener(this::clickBackspace);
                        view = backspaceButton;
                        break;
                    case (12):
                        ImageButton enterView = new ImageButton(getContext());
                        enterView.setImageResource(R.mipmap.enter);
                        enterView.setOnClickListener(this::clickEnter);
                        view = enterView;
                        break;
                    case (11):
                        id = 0;
                    default:
                        Button button = new Button(getContext());
                        button.setText(String.valueOf(id));
                        button.setTextSize(22);
                        button.setTextColor(ContextUtil.getColor(R.color.text_blue));
                        button.setOnClickListener(this::onClick);
                        view = button;
                        break;
                }
                view.setMinimumWidth(140);
                view.setMinimumHeight(64);
                view.setBackgroundResource(R.drawable.background_panel);
                binding.keyboardLayout.add(view, row, column);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_keyboard_login;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        switch (systemModel.layoutPage.getValue()) {
            case KEYBOARD_LOGIN_USER:
                binding.titleView.set(R.string.user_setup, LayoutPageEnum.MENU_HOME, true);
                break;
            case KEYBOARD_LOGIN_SYSTEM:
                binding.titleView.set(R.string.system_setup, LayoutPageEnum.MENU_HOME, true);
                break;
            case KEYBOARD_LOGIN_DEMO:
                binding.titleView.set(R.string.demo_setup, LayoutPageEnum.MENU_HOME, true);
                break;
        }

        password.delete(0, password.length());
        passwordMask.delete(0, passwordMask.length());
        displayMask();
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void displayMask() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < password.length(); index++) {
            stringBuilder.append("* ");
        }
        binding.passwordMask.setText(stringBuilder.toString());
    }

    private void clickBackspace(View imageView) {
        if (password.length() > 0) {
            password.deleteCharAt(password.length() - 1);
            displayMask();
        }
    }

    private void clickEnter(View imageView) {
        if (password.toString().equals(Constant.SYSTEM_PASSWORD) && (systemModel.layoutPage.getValue() == LayoutPageEnum.KEYBOARD_LOGIN_SYSTEM)) {
            systemModel.showLayout(LayoutPageEnum.SYSTEM_HOME);
        } else if (password.toString().equals(Constant.USER_PASSWORD) && (systemModel.layoutPage.getValue() == LayoutPageEnum.KEYBOARD_LOGIN_USER)) {
            systemModel.showLayout(LayoutPageEnum.USER_HOME);
        } else if (password.toString().equals(Constant.SYSTEM_PASSWORD) && (systemModel.layoutPage.getValue() == LayoutPageEnum.KEYBOARD_LOGIN_USER)) {
            systemModel.showLayout(LayoutPageEnum.USER_HOME);
        } else if (password.toString().equals(Constant.USER_PASSWORD) && (systemModel.layoutPage.getValue() == LayoutPageEnum.KEYBOARD_LOGIN_USER)) {
            systemModel.showLayout(LayoutPageEnum.USER_HOME);
        } else if (password.toString().equals(Constant.DEMO_PASSWORD) && (systemModel.layoutPage.getValue() == LayoutPageEnum.KEYBOARD_LOGIN_DEMO)) {
            systemModel.demo.set(!systemModel.demo.getValue());
            incubatorCommandSender.setDemo(systemModel.demo.getValue());
            systemModel.closePopup();
        } else {
            password.delete(0, password.length());
            displayMask();
        }
    }

    private void onClick(View button) {
        if (password.length() < Constant.SYSTEM_PASSWORD.length()) {
            password.append(((Button) button).getText());
            displayMask();
        }
    }
}