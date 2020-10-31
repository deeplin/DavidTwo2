package com.david.core.ui.model;

import com.david.core.ui.component.KeyButtonView;
import com.david.core.util.LazyLiveData;

public class LiveDataPopupModel extends IntegerPopupModel {

    private LazyLiveData<Integer> liveData;

    public LiveDataPopupModel(KeyButtonView keyButtonView) {
        super(keyButtonView);
    }

    @Override
    protected void updateValue() {
        super.updateValue();
        if (liveData != null)
            liveData.set(originValue);
    }

    public void setLiveData(LazyLiveData<Integer> liveData) {
        this.liveData = liveData;
    }

    public void setOriginValue() {
        if (liveData != null)
            setOriginValue(liveData.getValue());
    }
}
