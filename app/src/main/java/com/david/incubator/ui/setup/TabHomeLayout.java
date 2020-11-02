package com.david.incubator.ui.setup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycleOwner;

public class TabHomeLayout extends LinearLayout implements ILifeCycleOwner {

    private int tabId;
    private Consumer<Integer> tabConsumer;
    private TextView[] textViews;

    public TabHomeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void init(int tabSum) {
        tabId = Constant.NA_VALUE;
        textViews = new TextView[tabSum];
        for (int index = 0; index < tabSum; index++) {
            TextView textView = new TextView(getContext());
            textView.setBackgroundResource(R.drawable.button_tab_background);
            textView.setVisibility(View.INVISIBLE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextUtil.getColor(R.color.text_blue));
            textView.setTextSize(20);
            textView.setSelected(false);
            addView(textView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 68));
            textViews[index] = textView;
        }
    }

    public void setTabConsumer(Consumer<Integer> tabConsumer) {
        this.tabConsumer = tabConsumer;
    }

    public void setTab(int tabId) {
        this.tabId = tabId;
    }

    public void setText(final int id, String text, final Integer pageId) {
        TextView textView = textViews[id];
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
        if (id == tabId) {
            tabConsumer.accept(pageId);
        }
        textView.setOnClickListener(v -> {
            tabConsumer.accept(pageId);
            textViews[tabId].setSelected(false);
            tabId = id;
            textViews[tabId].setSelected(true);
        });
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        textViews[tabId].setSelected(true);
    }

    @Override
    public void detach() {
        this.tabId = Constant.NA_VALUE;
        for (int index = 0; index < textViews.length; index++) {
            textViews[index].setVisibility(View.INVISIBLE);
            textViews[index].setSelected(false);
        }
    }
}