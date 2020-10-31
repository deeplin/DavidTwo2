package com.david.core.ui.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.lifecycle.Observer;

import com.david.core.buffer.BufferRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.model.SystemModel;
import com.david.core.util.ContextUtil;
import com.david.core.util.rely.RangeUtil;

import javax.inject.Inject;

public class Co2SurfaceView extends WaveSurfaceView {

    @Inject
    BufferRepository bufferRepository;
    @Inject
    SystemModel systemModel;

    private final Observer<Boolean> selectCo2RrObserver;

    private final Path dotPath;

    private int range;
    private int denominator;

    private boolean isResp;

    public Co2SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        dotPath = new Path();

        setDotBarrier(dotPath, 4);
        setDotBarrier(dotPath, 95 / 2f);
        setDotBarrier(dotPath, 95 - 4);

        selectCo2RrObserver = aBoolean -> {
            bufferRepository.getCo2Buffer().stop();
            bufferRepository.getEcgRrBuffer().stop();
            if (aBoolean) {
                bufferRepository.getCo2Buffer().start(this::draw);
            } else {
                bufferRepository.getEcgRrBuffer().start(this::draw);
            }
            super.attach();
        };
    }

    @Override
    public void attach() {
        super.attach();
        systemModel.selectCo2.observeForever(selectCo2RrObserver);
    }

    @Override
    public void detach() {
        systemModel.selectCo2.removeObserver(selectCo2RrObserver);
        bufferRepository.getCo2Buffer().stop();
        bufferRepository.getEcgRrBuffer().stop();
        super.detach();
    }

    @Override
    protected ConfigEnum getConfigGain() {
        return ConfigEnum.RespGain;
    }

    @Override
    protected ConfigEnum getConfigSpeed() {
        return ConfigEnum.RespSpeed;
    }

    @Override
    protected float getPacketSpeed() {
        if (systemModel.selectCo2.getValue()) {
            if (systemModel.demo.getValue()) {
                return 100;
            }
            return 20;
        } else {
            if (systemModel.demo.getValue()) {
                return 250;
            }
            return 125;
        }
    }

    public void setResp(boolean isResp) {
        this.isResp = isResp;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public void draw(Integer start, Integer length, int[] dataArray) {
        if (!running || systemModel.freezeWave.getValue()) {
            return;
        }

        if (length == 1) {
            draw(dataArray[0]);
            return;
        }

        Canvas canvas = null;
        try {
            Rect rect = new Rect(0, 0, (int) xIndex + 10, height);
            canvas = surfaceHolder.lockCanvas(rect);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            for (int index = start; index < start + length; index++) {
                if (xIndex <= 0) {
                    newPath.moveTo(0, getData(dataArray[index]));
                    xIndex = stepForDot;
                } else if (xIndex > width) {
                    canvas.drawPath(newPath, paint);
                    newPath.reset();
                    newPath.moveTo(0, getData(dataArray[index]));
                    xIndex = stepForDot;
                } else {
                    newPath.lineTo(xIndex, getData(dataArray[index]));
                    xIndex += stepForDot;
                }
            }

            canvas.drawPath(newPath, paint);
            canvas.drawPath(dotPath, paint);
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void draw(Integer data) {
        Canvas canvas = null;
        if (xIndex <= 0) {
            newPath.moveTo(0, getData(data));
            xIndex = stepForDot;
        } else if (xIndex > width) {
            newPath.reset();
            newPath.moveTo(0, getData(data));
            xIndex = stepForDot;
        } else {
            try {
                Rect rect = new Rect(0, 0, (int) xIndex + 10, height);
                canvas = surfaceHolder.lockCanvas(rect);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                newPath.lineTo(xIndex, getData(data));
                canvas.drawPath(newPath, paint);

                xIndex += stepForDot;
                canvas.drawPath(dotPath, paint);
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private float getData(Integer data) {
        if (isResp) {
            return RangeUtil.getYInRange(data * gain, -range, range, denominator, height, 4, 4);
        } else {
            return RangeUtil.getYInRange(data * gain, 0, range, denominator, height, 4, 4);
        }
    }

    private void setDotBarrier(Path path, float y) {
        for (int index = 0; index < 750; index += 8) {
            path.moveTo(index, y);
            path.rLineTo(4, 0);
        }
    }
}