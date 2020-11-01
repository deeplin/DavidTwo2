package com.david.incubator.print.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import com.david.core.config.ConfigEnum;
import com.david.core.util.Constant;
import com.david.core.util.LoggerUtil;
import com.david.core.util.RangeUtil;

public class PrintSpo2CurveView extends BaseCurveView {

    private final Path siqPath;
    private boolean siq;

    public PrintSpo2CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        siqPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(newPath, paint);
//        canvas.drawPath(siqPath, paint);
    }

    @Override
    public void attach() {
        super.attach();
        siqPath.reset();
    }

    public void detach() {
        siqPath.reset();
        super.detach();
    }

    @Override
    protected ConfigEnum getConfigGain() {
        return ConfigEnum.Spo2Gain;
    }

    @Override
    protected ConfigEnum getConfigSpeed() {
        return ConfigEnum.PrintSpeed;
    }

    @Override
    public float getPacketLength() {
        return 62.5f;
    }

    public float drawInitialPrintPoints(Integer start, int length, int[] dataArray) {
        float lastY = 0;

        for (int pointId = 0; pointId < length; pointId++) {
            float xIndex = stepForDot * (pointId + 1);
            float y = getData(dataArray[pointId + start]);
            if (pointId == 0) {
                newPath.moveTo(xIndex, y);
            } else if (pointId == length - 1) {
                lastY = y;
                newPath.lineTo(width, y);
            } else {
                newPath.lineTo(xIndex, y);
            }

            if (pointId != 0) {
                if (siq) {
                    siqPath.lineTo(xIndex, height - 4);
                    siqPath.lineTo(xIndex, height - 12);
                    siq = false;
                }
                siqPath.lineTo(xIndex, height - 4);
            } else {
                siqPath.moveTo(xIndex, height - 4);
            }
        }
        postInvalidate();
        return lastY;
    }

    public float drawPrintPoints(Integer start, Integer length, int[] dataArray, float previousY) {
        float lastY = 0;

        newPath.reset();
        siqPath.reset();

        newPath.moveTo(0, previousY);
        siqPath.moveTo(0, height - 4);

        for (int pointId = 0; pointId < length; pointId++) {
            float xIndex = stepForDot * (pointId + 1);
            float y = getData(dataArray[pointId + start]);
            if (pointId != length - 1) {
                newPath.lineTo(xIndex, y);
            } else {
                lastY = y;
                newPath.lineTo(width, y);
            }

            if (siq) {
                siqPath.lineTo(xIndex, height - 4);
                siqPath.lineTo(xIndex, height - 12);
                siq = false;
            }
            siqPath.lineTo(xIndex, height - 4);
        }
        postInvalidate();
        return lastY;
    }

    private float getData(Integer data) {
        if (data > Constant.IS_SIQ) {
            data -= Constant.SIQ_STEP;
            siq = true;
        }
        return RangeUtil.getYInRange(data * gain * -1, -10240, 10240, 1, height, 0, 0);
    }
}