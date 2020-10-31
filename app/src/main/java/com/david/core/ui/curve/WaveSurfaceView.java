package com.david.core.ui.curve;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public abstract class WaveSurfaceView extends SurfaceView implements SurfaceHolder.Callback, ILifeCycle {

    @Inject
    ConfigRepository configRepository;

    public final static float BASE_SPEED = 6.25f;
    protected final SurfaceHolder surfaceHolder;
    protected final Paint paint;
    protected Path newPath;
    protected int width;
    protected int height;

    protected float xIndex;

    protected float gain;
    protected float stepForDot;
    protected boolean running;

    public WaveSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        newPath = new Path();

        surfaceHolder = getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        surfaceHolder.addCallback(this);

        paint = ViewUtil.buildPaint(ContextUtil.getColor(R.color.text_blue), 0);
        paint.setAntiAlias(true);
        running = false;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        this.width = width;
        this.height = height;
        running = true;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        running = false;
    }

    @Override
    public void attach() {
        xIndex = 0;
        gain = (float) Math.pow(2, configRepository.getConfig(getConfigGain()).getValue() - 3);
        float speed = (float) Math.pow(2, configRepository.getConfig(getConfigSpeed()).getValue());
        stepForDot = (BASE_SPEED * speed / Constant.getCurrentDotPitch() / getPacketSpeed());
        newPath.reset();
    }

    @Override
    public void detach() {
        newPath.reset();
    }

    protected abstract ConfigEnum getConfigGain();

    protected abstract ConfigEnum getConfigSpeed();

    protected abstract float getPacketSpeed();
}