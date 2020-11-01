package com.david.core.ui.component;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.util.Consumer;

import com.david.R;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.LazyLiveData;
import com.david.databinding.ViewOptionPopupBinding;

import java.util.ArrayList;
import java.util.List;

public class OptionPopupView extends BindingBasicLayout<ViewOptionPopupBinding> {

    private static final int MAX_OPTION = 10;

    private final List<String> optionList = new ArrayList<>();
    private final List<Button> buttonList = new ArrayList<>();
    private final ConstraintSet constraintSet;
    private Consumer<Integer> callback;

    private int currentOptionId;
    private LazyLiveData<Integer> lazyLiveData;

    public OptionPopupView(Context context) {
        super(context);
        setZ(100.f);
        setId(View.generateViewId());

        currentOptionId = Constant.NA_VALUE;

        constraintSet = new ConstraintSet();

        for (int index = 0; index < MAX_OPTION; index++) {
            Button button = new Button(getContext());
            button.setWidth(176);
            button.setAllCaps(false);
            button.setHeight(52);
            button.setTextSize(16);
            button.setBackgroundResource(R.drawable.button_bottom_background);
            button.setVisibility(View.GONE);
            button.setTag(index);
            button.setTextColor(ContextUtil.getColor(R.color.text_blue));
            button.setOnClickListener(this::confirmAndReturn);
            buttonList.add(button);
            binding.rootView.addView(button);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_option_popup;
    }

    public synchronized void init() {
        for (int index = 0; index < MAX_OPTION; index++) {
            buttonList.get(index).setSelected(false);
            buttonList.get(index).setVisibility(View.GONE);
        }
    }

    public synchronized void setOption(int index, String optionText) {
        buttonList.get(index).setText(optionText);
        buttonList.get(index).setVisibility(View.VISIBLE);
    }

    public synchronized void setCallback(Consumer<Integer> callback) {
        this.callback = callback;
    }

    public void setSelectedId(int selectedId) {
        buttonList.get(selectedId).setSelected(true);
        currentOptionId = selectedId;
    }

    public void setSelectedId(LazyLiveData<Integer> lazyLiveData) {
        this.lazyLiveData = lazyLiveData;
        setSelectedId(lazyLiveData.getValue());
    }

    public int getActiveItem() {
        int sum = 0;
        for (int index = 0; index < MAX_OPTION; index++) {
            if (buttonList.get(index).getVisibility() != View.GONE) {
                sum++;
            }
        }
        return sum;
    }

    public synchronized void show(ConstraintLayout rootView, int viewId, boolean downward) {
        constraintSet.clone(rootView);
        constraintSet.clear(getId(), ConstraintSet.TOP);
        constraintSet.clear(getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(getId(), ConstraintSet.END, viewId, ConstraintSet.END);
        if (downward) {
            constraintSet.clear(getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(getId(), ConstraintSet.TOP, viewId, ConstraintSet.TOP);
        } else {
            constraintSet.clear(getId(), ConstraintSet.TOP);
            constraintSet.connect(getId(), ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM);
        }
        constraintSet.applyTo(rootView);
        setVisibility(View.VISIBLE);
    }

    public synchronized void close() {
        lazyLiveData = null;
        callback = null;
        optionList.clear();
        if (currentOptionId >= 0) {
            buttonList.get(currentOptionId).setSelected(false);
            currentOptionId = Constant.NA_VALUE;
        }
        setVisibility(View.GONE);
    }

    protected void confirmAndReturn(View view) {
        if (lazyLiveData != null) {
            lazyLiveData.set((int) view.getTag());
        }
        callback.accept((int) view.getTag());
        close();
    }
}