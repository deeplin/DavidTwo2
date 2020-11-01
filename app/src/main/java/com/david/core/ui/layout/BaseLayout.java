package com.david.core.ui.layout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.arch.core.util.Function;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Consumer;

import com.david.R;
import com.david.core.control.ComponentControl;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.BindingLayoutEnum;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.component.KeyButtonView;
import com.david.core.ui.component.NumberPopupView;
import com.david.core.ui.component.OptionPopupView;
import com.david.core.ui.component.TitleView;
import com.david.core.ui.model.IntegerPopupModel;
import com.david.core.ui.model.LiveDataPopupModel;
import com.david.core.util.ILifeCycle;
import com.david.core.util.LazyLiveData;
import com.david.core.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public abstract class BaseLayout extends ConstraintLayout implements ILifeCycle {

    @Inject
    ComponentControl componentControl;
    @Inject
    ConfigRepository configRepository;

    private BindingLayoutEnum bindingLayoutEnum;

    private final List<IntegerPopupModel> integerPopupModelList = new ArrayList<>();

    protected NumberPopupView numberPopupView;
    private OptionPopupView optionPopupView;
    protected KeyButtonView[] keyButtonViews;
    private ConfigEnum[] configEnums;

    private int topMargin = 0;
    protected int titleId;

    public BaseLayout(Context context) {
        super(context);
        setId(View.generateViewId());
    }

    protected void init(LayoutPageEnum layoutPageEnum) {
        numberPopupView = componentControl.getNumberPopupView();
        optionPopupView = componentControl.getOptionPopupView();
        this.bindingLayoutEnum = layoutPageEnum.getBindingLayoutEnum();
        if (bindingLayoutEnum.isTitle()) {
            TitleView titleView = new TitleView(getContext(), null);
            titleId = View.generateViewId();
            titleView.setId(titleId);
            titleView.set(layoutPageEnum.getTitleId(), layoutPageEnum.getParentPageEnum(), layoutPageEnum.isShowReverse(), layoutPageEnum.isShowClose());
            addInnerView(titleView, LayoutParams.MATCH_PARENT, 56, 0, 0, -1, -1);
            topMargin = 56;
        }

        ImageView backgroundImage = new ImageView(getContext());
        backgroundImage.setId(View.generateViewId());
        backgroundImage.setBackgroundResource(R.drawable.background_panel);
        backgroundImage.setZ(-1);
        addInnerView(backgroundImage, LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT, 4, topMargin, 4, 4);

        setBackgroundResource(R.drawable.background_panel_blue_dark);
        backgroundImage.setOnClickListener(view -> closePopup());
    }

    @Override
    public void attach() {
        addView(numberPopupView, bindingLayoutEnum.getPopupWidth(), bindingLayoutEnum.getPopupHeight());
        closePopup();

        addView(optionPopupView, bindingLayoutEnum.getPopupWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        optionPopupView.close();

        for (IntegerPopupModel integerPopupModel : integerPopupModelList) {
            if (integerPopupModel instanceof LiveDataPopupModel) {
                ((LiveDataPopupModel) integerPopupModel).setOriginValue();
                integerPopupModel.getKeyButtonView().setText();
            }
            integerPopupModel.setDefaultColor();
        }
    }

    @Override
    public void detach() {
        closePopup();
        removeView(optionPopupView);
        removeView(numberPopupView);
    }

    private void addInnerView(View view, int width, int height, int start, int top, int end, int bottom) {
        ViewUtil.addInnerView(this, view, width, height, start, top, end, bottom);
    }

    protected void addInnerView(int rowId, View view) {
        if (rowId < 8) {
            addInnerView(view, bindingLayoutEnum.getComponentWidth(), bindingLayoutEnum.getComponentHeight(),
                    16, topMargin + rowId * bindingLayoutEnum.getComponentHeight(), -1, -1);
        } else {
            addInnerView(view, bindingLayoutEnum.getComponentWidth(), bindingLayoutEnum.getComponentHeight(),
                    -1, topMargin + (rowId - 8) * bindingLayoutEnum.getComponentHeight(), 16, -1);
        }
    }

    protected void addFullInnerView(int rowId, View view) {
        addInnerView(view, bindingLayoutEnum.getComponentWidth() * 2 + 16, bindingLayoutEnum.getComponentHeight(),
                16, topMargin + rowId * bindingLayoutEnum.getComponentHeight(), -1, -1);
    }

    protected void addInnerButton(int rowId, View view) {
        if (rowId < 8) {
            addInnerView(view, bindingLayoutEnum.getButtonWidth(), bindingLayoutEnum.getButtonHeight(),
                    bindingLayoutEnum.getButtonStart(), topMargin + 8 + rowId * bindingLayoutEnum.getComponentHeight(),
                    -1, -1);
        } else {
            addInnerView(view, bindingLayoutEnum.getButtonWidth(), bindingLayoutEnum.getButtonHeight(),
                    -1, topMargin + 8 + (rowId - 8) * bindingLayoutEnum.getComponentHeight(),
                    bindingLayoutEnum.getButtonStart(), -1);
        }
    }

    /*NumberPopupView*/
    protected void loadLiveDataItems(KeyButtonEnum startEnum, int itemNum, Function<KeyButtonEnum, Integer> lowerCondition,
                                     Function<KeyButtonEnum, Integer> upperCondition) {
        for (int index = 0; index < itemNum; index++) {
            addKeyButtonWithLiveData(index, index);
            KeyButtonEnum keyButtonEnum = KeyButtonEnum.values()[startEnum.ordinal() + index];
            setKeyButtonEnum(index, keyButtonEnum, lowerCondition, upperCondition);
        }
    }

    protected void addKeyButtonWithLiveData(int index, int rowId) {
        KeyButtonView keyButtonView = new KeyButtonView(getContext());
        keyButtonView.setId(View.generateViewId());

        final IntegerPopupModel integerPopupModel = new LiveDataPopupModel(keyButtonView);
        integerPopupModelList.add(index, integerPopupModel);

        keyButtonView.getKey().setOnClickListener(v -> closePopup());
        addInnerView(rowId, keyButtonView);
    }

    protected void setKeyButtonEnum(int index, KeyButtonEnum keyButtonEnum, Function<KeyButtonEnum, Integer> minCondition,
                                    Function<KeyButtonEnum, Integer> maxCondition) {
        IntegerPopupModel integerPopupModel = integerPopupModelList.get(index);
        integerPopupModel.setConverter(keyButtonEnum.getConverter());
        integerPopupModel.setMin(keyButtonEnum.getLowerLimit());
        integerPopupModel.setMax(keyButtonEnum.getUpperLimit());
        integerPopupModel.setStep(keyButtonEnum.getStep());

        integerPopupModel.getKeyButtonView().setKeyId(keyButtonEnum.getTitleId());
        integerPopupModel.getKeyButtonView().getValue().setOnClickListener(v -> {
            closePopup();
            if (minCondition != null)
                integerPopupModel.setMin2(minCondition.apply(keyButtonEnum));
            if (maxCondition != null)
                integerPopupModel.setMax2(maxCondition.apply(keyButtonEnum));
            numberPopupView.set(integerPopupModel);
            numberPopupView.show(this, integerPopupModel.getKeyButtonView().getId(), index != 7 && index != 15);
        });
    }

    protected void setOriginValue(int index, ConfigEnum configEnum) {
        setOriginValue(index, configRepository.getConfig(configEnum));
    }

    protected void setOriginValue(int index, LazyLiveData<Integer> lazyLiveData) {
        LiveDataPopupModel liveDataPopupModel = (LiveDataPopupModel) integerPopupModelList.get(index);
        liveDataPopupModel.setLiveData(lazyLiveData);
        setOriginValue(index, lazyLiveData.getValue());
    }

    protected void setOriginValue(int index, int originValue) {
        IntegerPopupModel integerPopupModel = integerPopupModelList.get(index);
        integerPopupModel.setOriginValue(originValue);
    }

    protected void setCallback(int index, Consumer<Integer> callback) {
        LiveDataPopupModel liveDataPopupModel = (LiveDataPopupModel) integerPopupModelList.get(index);
        liveDataPopupModel.setCallback(callback);
    }

    protected IntegerPopupModel getIntegerPopupModel(int index) {
        return integerPopupModelList.get(index);
    }

    protected void closePopup() {
        optionPopupView.close();
        numberPopupView.close();
    }

    /*KeyButton*/
    protected void initPopup(int rowSum) {
        configEnums = new ConfigEnum[rowSum];
        keyButtonViews = new KeyButtonView[rowSum];
        for (int index = 0; index < rowSum; index++) {
            keyButtonViews[index] = ViewUtil.buildKeyButtonView(getContext());
        }
    }

    protected void setRowId(int index, int rowId) {
        KeyButtonView keyButtonView = keyButtonViews[index];
        keyButtonView.setVisibility(View.VISIBLE);
        addInnerView(rowId, keyButtonView);
    }

    protected void setConfig(int index, ConfigEnum configEnum) {
        configEnums[index] = configEnum;
    }

    protected void setText(int index, int textId, Object[] valueArray) {
        KeyButtonView keyButtonView = keyButtonViews[index];
        keyButtonView.setSelected(false);
        keyButtonView.setKeyId(textId);
        keyButtonView.setValue(valueArray[configRepository.getConfig(configEnums[index]).getValue()].toString());
    }

    /*OptionPopupView*/

    protected void setPopup(final int index, Object[] valueArray, ConfigEnum[] existingValue,
                            boolean downward, Consumer<Integer> callback) {
        setPopup(index, index, valueArray, existingValue, downward, callback);
    }

    protected void setPopup(final int index, final int dropDownViewId, final Object[] valueArray,
                            final ConfigEnum[] existingValue, final boolean downward, final Consumer<Integer> callback) {
        final KeyButtonView keyButtonView = keyButtonViews[index];
        keyButtonView.getValue().setOnClickListener(v -> {
            optionPopupView.init();

            for (int i = 0; i < valueArray.length; i++) {
                boolean match = false;
                if (existingValue != null) {
                    for (int j = 0; j < existingValue.length; j++) {
                        if (i != acceptId() && configRepository.getConfig(existingValue[j]).getValue().intValue() == i) {
                            match = true;
                        }
                    }
                }
                if (!match && valueArray[i] != null) {
                    optionPopupView.setOption(i, valueArray[i].toString());
                }
            }

            optionPopupView.setSelectedId(configRepository.getConfig(configEnums[index]).getValue());
            optionPopupView.setCallback(integer -> {
                if (integer.intValue() != configRepository.getConfig(configEnums[index]).getValue().intValue()) {
                    configRepository.getConfig(configEnums[index]).set(integer);
                    keyButtonView.setValue(valueArray[integer].toString());
                    keyButtonView.setSelected(true);
                    if (callback != null)
                        callback.accept(integer);
                }
            });
            optionPopupView.show(this, keyButtonViews[dropDownViewId].getId(), downward);
        });
    }

    protected int acceptId() {
        return Integer.MIN_VALUE;
    }
}