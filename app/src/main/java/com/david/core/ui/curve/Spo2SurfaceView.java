package com.david.core.ui.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.david.core.buffer.BufferRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.model.SystemModel;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.rely.RangeUtil;

import javax.inject.Inject;

public class Spo2SurfaceView extends WaveSurfaceView {

    @Inject
    BufferRepository bufferRepository;
    @Inject
    SystemModel systemModel;

    private final Path newSiqPath;

    private boolean siq;

    public Spo2SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        newSiqPath = new Path();
    }

    @Override
    public void attach() {
        super.attach();
        newSiqPath.reset();
        bufferRepository.getSpo2Buffer().start2(this::draw);
        setVisibility(View.VISIBLE);
    }

    @Override
    public void detach() {
        bufferRepository.getSpo2Buffer().stop();
        newSiqPath.reset();
        super.detach();
        setVisibility(View.GONE);
    }

    @Override
    protected ConfigEnum getConfigGain() {
        return ConfigEnum.Spo2Gain;
    }

    @Override
    protected ConfigEnum getConfigSpeed() {
        return ConfigEnum.Spo2Speed;
    }

    @Override
    protected float getPacketSpeed() {
        if (systemModel.demo.getValue()) {
            return 15.625f;
        }
        return 62.5f;
    }

    public void draw(Integer data) {
        if (!running || systemModel.freezeWave.getValue()) {
            return;
        }
        Canvas canvas = null;
        if (xIndex <= 0) {
            newPath.moveTo(0, getData(data));
            newSiqPath.moveTo(0, height - 2);
            xIndex = stepForDot;
        } else if (xIndex > width) {
            newPath.reset();
            newSiqPath.reset();
            newPath.moveTo(0, getData(data));
            newSiqPath.moveTo(0, height - 2);
            xIndex = stepForDot;
        } else {
            try {
                Rect rect = new Rect(0, 0, (int) xIndex + 10, height);
                canvas = surfaceHolder.lockCanvas(rect);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                newPath.lineTo(xIndex, getData(data));
                canvas.drawPath(newPath, paint);

                if (siq) {
                    newSiqPath.lineTo(xIndex, height - 4);
                    newSiqPath.lineTo(xIndex, height - 12);
                    siq = false;
                }
                newSiqPath.lineTo(xIndex, height - 4);
                canvas.drawPath(newSiqPath, paint);

                xIndex += stepForDot;
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private float getData(Integer data) {
        if (data > Constant.IS_SIQ) {
            data -= Constant.SIQ_STEP;
            siq = true;
        }
        return RangeUtil.getYInRange(data * gain * -1, -10240, 10240, 1, height, 0, 0);
    }
}