package com.david.incubator.ui.trend;

import com.david.R;
import com.david.core.util.ContextUtil;

public enum TrendCategoryEnum {
    Incubator(R.string.incubator), Ecg(R.string.ecg_id), Spo2(R.string.spo2), Co2(R.string.co2);

    public String getDisplay() {
        return display;
    }

    private final String display;

    TrendCategoryEnum(int textId) {
        display = ContextUtil.getString(textId);
    }
}
