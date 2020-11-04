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
import com.david.core.enumeration.EcgChannelEnum;
import com.david.core.model.EcgModel;
import com.david.core.model.SystemModel;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.rely.RangeUtil;

import javax.inject.Inject;

public class EcgSurfaceView extends WaveSurfaceView {

    @Inject
    BufferRepository bufferRepository;
    @Inject
    SystemModel systemModel;
    @Inject
    EcgModel ecgModel;

    protected Path newPath2;
    private int count = 0;
    private final Path iPacePath;
    private int ecgChannelEnumId;

    public EcgSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        iPacePath = new Path();
        newPath2 = new Path();
    }

    @Override
    public void attach() {
        super.attach();
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        iPacePath.reset();
        newPath2.reset();
        bufferRepository.getEcgBuffer(ecgChannelEnumId).start(this::draw);
        setVisibility(View.VISIBLE);
    }

    @Override
    public void detach() {
        bufferRepository.getEcgBuffer(ecgChannelEnumId).stop();
        iPacePath.reset();
        newPath2.reset();
        super.detach();
        setVisibility(View.GONE);
    }

    @Override
    protected ConfigEnum getConfigGain() {
        return ConfigEnum.EcgGain;
    }

    @Override
    protected ConfigEnum getConfigSpeed() {
        return ConfigEnum.EcgSpeed;
    }

    @Override
    protected float getPacketSpeed() {
        return 250f;
    }

    public void setEcgChannel(EcgChannelEnum ecgChannelEnum) {
        this.ecgChannelEnumId = ecgChannelEnum.ordinal();
    }

    private void draw(Integer start, Integer length, int[] dataArray) {
        if (!running || systemModel.freezeWave.getValue()) {
            return;
        }
        Canvas canvas = null;
        try {
            Rect rect = new Rect((int) xIndex, 0, (int) xIndex + 10, height);
            canvas = surfaceHolder.lockCanvas(rect);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            if (ecgModel.leadOff.getValue()) {
                for (int index = start; index < start + length; index++) {
                    if (xIndex <= 0) {
                        newPath.moveTo(0, height / 2f);
                        xIndex = stepForDot;
                    } else if (xIndex > width) {
                        canvas.drawPath(newPath, paint);
                        newPath.reset();
                        iPacePath.reset();
                        newPath.moveTo(0, height / 2f);
                        xIndex = stepForDot;
                    } else {
                        if (xIndex % 10 < 5) {
                            newPath.lineTo(xIndex, height / 2f);
                        } else {
                            newPath.moveTo(xIndex, height / 2f);
                        }
                        xIndex += stepForDot;
                    }
                }
                canvas.drawPath(newPath, paint);
            } else {
                for (int index = start; index < start + length; index++) {
                    float y = getData(dataArray[index]);
                    if (xIndex <= 0) {
                        newPath.moveTo(0, y);
                        newPath2.moveTo(0, y);
                        xIndex = stepForDot;
                    } else if (xIndex > width) {
                        canvas.drawPath(newPath, paint);
                        newPath.reset();
                        newPath2.reset();
                        iPacePath.reset();
                        newPath.moveTo(0, y);
                        newPath2.moveTo(0, y);
                        xIndex = stepForDot;
                    } else {
                        newPath.lineTo(xIndex, y);
                        newPath2.lineTo(xIndex, y);
                        xIndex += stepForDot;
                    }
                }
                if (count % 2 == 0) {
                    canvas.drawPath(newPath, paint);
                    newPath.reset();
                } else {
                    canvas.drawPath(newPath2, paint);
                    newPath2.reset();
                }
                canvas.drawPath(iPacePath, paint);
                count++;
            }

        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void buildPace() {
        for (int y = 0; y < height; y += 8) {
            iPacePath.moveTo(xIndex, y);
            iPacePath.rLineTo(0, 4);
        }
    }

    private float getData(Integer data) {
        if (data > Constant.IS_SIQ) {
            buildPace();
            data -= Constant.SIQ_STEP;
        }
        return RangeUtil.getYInRange(data * gain * -0.0689f, height);
    }
}