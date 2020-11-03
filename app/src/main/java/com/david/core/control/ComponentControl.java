package com.david.core.control;

import android.content.Context;
import android.view.View;

import com.david.core.ui.component.NumberPopupView;
import com.david.core.ui.component.OptionPopupView;
import com.david.core.util.ContextUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ComponentControl {

    public NumberPopupView getNumberPopupView() {
        return numberPopupView;
    }

    public OptionPopupView getOptionPopupView() {
        return optionPopupView;
    }

    private final NumberPopupView numberPopupView;
    private final OptionPopupView optionPopupView;

    @Inject
    ComponentControl() {
        Context applicationContext = ContextUtil.getApplicationContext();
        numberPopupView = new NumberPopupView(applicationContext);
        optionPopupView = new OptionPopupView(applicationContext);
        numberPopupView.setVisibility(View.GONE);
        optionPopupView.setVisibility(View.GONE);
    }
}
