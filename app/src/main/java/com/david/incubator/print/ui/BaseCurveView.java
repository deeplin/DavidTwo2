package com.david.incubator.print.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.david.R;
import com.david.core.config.ConfigEnum;
import com.david.core.config.ConfigRepository;
import com.david.core.ui.view.WaveSurfaceView;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public abstract class BaseCurveView extends View implements ILifeCycle {

    @Inject
    ConfigRepository configRepository;

    protected final Paint paint;
    protected Path newPath;
    protected int width;
    protected int height;

    protected float gain;
    protected float stepForDot;

    public BaseCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        newPath = new Path();
        paint = ViewUtil.buildPaint(ContextUtil.getColor(R.color.black), 2);
        paint.setAntiAlias(true);
    }

    @Override
    public void attach() {
        gain = (float) Math.pow(2, configRepository.getConfig(getConfigGain()).getValue() - 3);
        float speed = (float) Math.pow(2, configRepository.getConfig(getConfigSpeed()).getValue() + 1);
        stepForDot = (WaveSurfaceView.BASE_SPEED * speed / Constant.PRINT_DOT_PITCH / getPacketLength());
        newPath.reset();
    }

    @Override
    public void detach() {
        newPath.reset();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    protected abstract ConfigEnum getConfigGain();

    protected abstract ConfigEnum getConfigSpeed();

    public abstract float getPacketLength();
}