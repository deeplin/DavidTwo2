package com.david.core.ui.curve;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.david.core.enumeration.SensorModelEnum;
import com.david.core.util.ILifeCycle;

public abstract class BaseCurveView extends View implements ILifeCycle {

    protected static final int MARGIN = 4;
    protected static final int width = 750;
    protected int height;
    protected boolean running;
    protected final Paint[] paintArray;
    protected final SensorModelEnum[] sensorModelEnumArray;
    protected final Path path;

    public BaseCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        running = false;
        paintArray = new Paint[getCurveCount()];
        sensorModelEnumArray = new SensorModelEnum[getCurveCount()];
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        height = getHeight();
        running = true;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
        running = false;
    }

    protected abstract int getCurveCount();
}