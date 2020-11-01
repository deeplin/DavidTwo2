package com.david.incubator.print.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import com.david.core.enumeration.ConfigEnum;
import com.david.core.util.Constant;
import com.david.core.util.rely.RangeUtil;

public class PrintEcgCurveView extends BasePrintCurveView {

    private final Path iPacePath;
    private boolean moveTo;
    private boolean leadOff;

    public PrintEcgCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        moveTo = true;
        iPacePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(newPath, paint);
        canvas.drawPath(iPacePath, paint);
    }

    @Override
    public void attach() {
        super.attach();
        iPacePath.reset();
        moveTo = true;
    }

    public void detach() {
        iPacePath.reset();
        super.detach();
    }

    public void setLeadOff(boolean leadOff) {
        this.leadOff = leadOff;
    }

    @Override
    protected ConfigEnum getConfigGain() {
        return ConfigEnum.EcgGain;
    }

    @Override
    protected ConfigEnum getConfigSpeed() {
        return ConfigEnum.PrintSpeed;
    }

    @Override
    public float getPacketLength() {
        return 250f;
    }

    public float drawInitialPrintPoints(Integer start, int length, int[] dataArray) {
        float lastY = 0;
        boolean initialized = false;
        for (int pointId = 0; pointId < length; pointId++) {
            float xIndex = stepForDot * (pointId + 1);
            if (leadOff) {
                if (xIndex % 10 < 5) {
                    if (moveTo) {
                        moveTo = false;
                        newPath.moveTo(xIndex, height / 2f);
                    } else {
                        newPath.lineTo(xIndex, height / 2f);
                    }
                } else if (!moveTo) {
                    moveTo = true;
                }
                if (pointId == length - 1) {
                    lastY = height / 2f;
                }
            } else {
                float y = getData(dataArray[pointId + start], xIndex);
                if (initialized) {
                    if (pointId == length - 1) {
                        lastY = y;
                        newPath.lineTo(width, y);
                    } else {
                        newPath.lineTo(xIndex, y);
                    }
                } else {
                    newPath.moveTo(xIndex, y);
                    initialized = true;
                }
            }
        }
        postInvalidate();
        return lastY;
    }

    public float drawPrintPoints(Integer start, Integer length, int[] dataArray, float previousY) {
        float lastY = 0;
        newPath.reset();
        iPacePath.reset();

        newPath.moveTo(0, previousY);
        for (int pointId = 0; pointId < length; pointId++) {
            float xIndex = stepForDot * (pointId + 1);
            if (leadOff) {
                if (xIndex % 10 < 5) {
                    if (moveTo) {
                        moveTo = false;
                        newPath.moveTo(xIndex, height / 2f);
                    } else {
                        newPath.lineTo(xIndex, height / 2f);
                    }
                } else if (!moveTo) {
                    moveTo = true;
                }
                if (pointId == length - 1) {
                    lastY = height / 2f;
                }
            } else {
                float y = getData(dataArray[pointId + start], xIndex);
                if (pointId != length - 1) {
                    newPath.lineTo(xIndex, y);
                } else {
                    lastY = y;
                    newPath.lineTo(width, y);
                }
            }
        }
        postInvalidate();
        return lastY;
    }

    private void buildPace(float xIndex) {
        for (int y = 0; y < height; y += 8) {
            iPacePath.moveTo(xIndex, y);
            iPacePath.rLineTo(0, 4);
        }
    }

    private float getData(Integer data, float xIndex) {
        if (data > Constant.IS_SIQ) {
            buildPace(xIndex);
            data -= Constant.SIQ_STEP;
        }
        return RangeUtil.getYInRange(data * gain * -0.0689f, height);
    }
}