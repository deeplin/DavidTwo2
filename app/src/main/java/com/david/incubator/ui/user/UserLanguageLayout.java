package com.david.incubator.ui.user;

import android.content.Context;

import com.david.R;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.LanguageEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class UserLanguageLayout extends BaseLayout {

    @Inject
    SensorModelRepository sensorModelRepository;

    private final KeyButtonView languageView;

    private int originLanguageOption;

    public UserLanguageLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.USER_LANGUAGE);

        languageView = ViewUtil.buildKeyButtonView(getContext());
        languageView.getValue().setOnClickListener(v -> {
            optionPopupView.init();
            for (LanguageEnum languageEnum : LanguageEnum.values()) {
                optionPopupView.setOption(languageEnum.ordinal(), languageEnum.toString());
            }
            optionPopupView.setSelectedId(originLanguageOption);
            optionPopupView.setCallback(this::languageCallback);
            optionPopupView.show(this, languageView.getId(), true);
        });

        addInnerView(0, languageView);
    }

    @Override
    public void attach() {
        super.attach();

        languageView.setKey(ContextUtil.getString(R.string.language));
        LanguageEnum languageEnum = LanguageEnum.getLanguage();
        originLanguageOption = languageEnum.ordinal();
        languageView.setValue(languageEnum.toString());
        languageView.setSelected(false);
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void languageCallback(int optionId) {
        if (optionId != originLanguageOption) {
            originLanguageOption = optionId;
            LanguageEnum.setLanguage(LanguageEnum.values()[optionId]);
            LanguageEnum.setLanguage(optionId);
            sensorModelRepository.setText();
            languageView.setValue(LanguageEnum.values()[optionId].toString());
            languageView.setSelected(true);
        }
    }
}