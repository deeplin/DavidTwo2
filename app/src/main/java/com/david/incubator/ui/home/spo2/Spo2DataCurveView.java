package com.david.incubator.ui.home.spo2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.david.R;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.ui.curve.BaseCurveView;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;
import com.david.core.util.ViewUtil;
import com.david.core.util.rely.RangeUtil;

public class Spo2DataCurveView extends BaseCurveView implements ILifeCycle {

    private final Paint sidePaint;


    public Spo2DataCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sidePaint = ViewUtil.buildPaint(ContextUtil.getColor(R.color.black), 1);
    }

    @Override
    protected int getCurveCount() {
        return 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (running) {
            for (int index = 0; index < getCurveCount(); index++) {
                Paint paint = paintArray[index];
                if (paint != null) {
                    canvas.drawPath(path, paint);
                }
            }
        }
        path.reset();
        drawWhiteDotBarrier(canvas, MARGIN);
        path.reset();
        drawWhiteDotBarrier(canvas, height - MARGIN);
    }

    private void drawWhiteDotBarrier(Canvas canvas, int y) {
        for (int index = MARGIN; index < width - MARGIN; index += 8) {
            path.moveTo(index, y);
            path.rLineTo(4, 0);
            canvas.drawPath(path, sidePaint);
        }
    }

    public void set(int curveId, SensorModelEnum sensorModelEnum) {
        paintArray[curveId] = ViewUtil.buildPaint(sensorModelEnum.getUniqueColor(), 2);
        sensorModelEnumArray[curveId] = sensorModelEnum;
    }

    public void drawAll(int curveId, int[] dataSeries) {
        if (running) {
            path.reset();
            boolean startPoint = true;
            for (int index = 0; index < dataSeries.length; index++) {
                int data = dataSeries[index];
                if (data >= 0) {
                    float newY = RangeUtil.getYInRange(data, sensorModelEnumArray[curveId], height, MARGIN, MARGIN);
                    if (startPoint) {
                        path.moveTo(index, newY);
                        startPoint = false;
                    } else {
                        path.lineTo(index, newY);
                    }
                } else {
                    startPoint = true;
                }
            }
        }
    }
}