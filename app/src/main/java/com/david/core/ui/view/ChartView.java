package com.david.core.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import com.david.R;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.TimeUtil;
import com.david.core.util.ViewUtil;
import com.david.core.util.rely.RangeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartView extends View {

    private static final int AXIS_PADDING_TOP = 5;
    private static final int AXIS_PADDING_RIGHT = 20;

    private final List<String> xAxisTextList = new ArrayList<>();

    private Paint[] paintArray;
    private SensorModelEnum[] sensorModelEnumArray;
    private int[][] dataSeriesArray;
    private final Paint axisPaint;
    private final Paint textPaint;
    private final Paint whitePaint;
    private final Path axisPath;
    private final Path whitePath;

    private int width;
    private int height;

    private int paddingTop;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;

    private Function<Double, String> yFormat;
    private int yMax;
    private int yMin;
    private float yStepCount;
    private String unit;

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    private boolean ready;

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        axisPath = new Path();
        whitePath = new Path();
        axisPaint = ViewUtil.buildPaint(ContextUtil.getColor(R.color.text_blue), 2);
        axisPaint.setAntiAlias(true);
        whitePaint = ViewUtil.buildPaint(ContextUtil.getColor(R.color.white), 2);
        textPaint = ViewUtil.buildPaint(ContextUtil.getColor(R.color.text_blue), 0);
        textPaint.setTextSize(16);
        textPaint.setAntiAlias(true);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (top >= 0)
            paddingTop = top;
        if (bottom >= 0)
            paddingBottom = bottom;
        if (left >= 0)
            paddingLeft = left;
        if (right >= 0)
            paddingRight = right;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setXAxis(long currentEndTime, int interval) {
        xAxisTextList.clear();
        for (int index = 0; index < Constant.DOT_PER_CHART / 10; index++) {
            if (index % 2 == 1) {
                xAxisTextList.add(TimeUtil.getTimeFromSecond(
                        currentEndTime - (5 - (index / 2)) * 20 * interval,
                        TimeUtil.HourMinute));
            } else {
                xAxisTextList.add("");
            }
        }
    }

    public void setWeightXAxis(long currentEndTime, int interval) {
        xAxisTextList.clear();
        for (int index = 0; index < Constant.DOT_PER_WEIGHT_CHART; index++) {
            if (index % 2 == 1) {
                xAxisTextList.add(TimeUtil.getTimeFromSecond(
                        currentEndTime - (5 - (index / 2)) * 2 * interval,
                        TimeUtil.MonthDay));
            } else {
                xAxisTextList.add("");
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (ready) {
            super.onDraw(canvas);
            axisPath.reset();
            whitePath.reset();
            drawAxis();
            drawXAxisStep(canvas);
            drawYAxisStep(canvas);

            canvas.drawPath(axisPath, axisPaint);
            canvas.drawPath(whitePath, whitePaint);
            drawDataSet(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.width = width;
        this.height = height;
    }

    private void drawAxis() {
        axisPath.moveTo(paddingLeft, height - paddingBottom);
        axisPath.lineTo(width - paddingRight, height - paddingBottom);

        axisPath.moveTo(paddingLeft, height - paddingBottom);
        axisPath.lineTo(paddingLeft, paddingTop);
    }

    private void drawXAxisStep(Canvas canvas) {
        float xStep = (float) (width - paddingLeft - paddingRight - AXIS_PADDING_RIGHT) / xAxisTextList.size();
        float xStart = paddingLeft;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int baseLineY = (int) (height - paddingBottom - fontMetrics.top - fontMetrics.bottom + 10);
        for (int index = 0; index < xAxisTextList.size(); index++) {
            xStart += xStep;
            axisPath.moveTo(xStart, height - paddingBottom);
            axisPath.rLineTo(0, 8);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(xAxisTextList.get(index), xStart, baseLineY, textPaint);
        }
    }

    private void drawYAxisStep(Canvas canvas) {
        double y = yMin;
        int yStart = height - paddingBottom;
        float yStep = (float) (height - paddingTop - paddingBottom - AXIS_PADDING_TOP) / yStepCount;
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        for (int index = 0; index <= yStepCount; index++) {
            axisPath.moveTo(paddingLeft, yStart);
            axisPath.rLineTo(-8, 0);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            int baseLineY = (int) (yStart - fontMetrics.top / 2 - fontMetrics.bottom / 2);
            canvas.drawText(yFormat.apply(y), paddingLeft - 15, baseLineY, textPaint);
            y += (yMax - yMin) / yStepCount;
            yStart -= yStep;

            if (index < yStepCount) {
                int xIndex = 10 + paddingLeft;
                do {
                    whitePath.moveTo(xIndex, yStart);
                    whitePath.rLineTo(10, 0);
                    xIndex += 20;
                } while (xIndex < width - paddingRight - AXIS_PADDING_RIGHT);
            } else {
                canvas.drawText(unit, paddingLeft + 10 + unit.length() * 10, baseLineY + 10, textPaint);
            }
        }
    }

    private void drawDataSet(Canvas canvas) {
        float xStep = (float) (width - paddingLeft - paddingRight - AXIS_PADDING_RIGHT) / xAxisTextList.size() / 10;
        for (int index = 0; index < dataSeriesArray.length; index++) {
            int[] dataSeries = dataSeriesArray[index];
            if (dataSeries == null) {
                continue;
            }

            SensorModelEnum sensorModelEnum = sensorModelEnumArray[index];
            Paint paint = paintArray[index];
            axisPath.reset();
            float xStart = (float) (width - paddingRight - AXIS_PADDING_RIGHT);

            boolean startPoint = true;
            for (int dataId = 0; dataId < dataSeries.length; dataId++) {
                int data = dataSeries[dataId];

                if (data >= 0) {
                    float newY = RangeUtil.getYInRange(data, sensorModelEnum, height,
                            paddingTop + AXIS_PADDING_TOP, paddingBottom);
                    if (startPoint) {
                        axisPath.moveTo(xStart, newY);
                        startPoint = false;
                    } else {
                        axisPath.lineTo(xStart, newY);
                    }
                } else {
                    startPoint = true;
                }
                xStart -= xStep;
            }
            canvas.drawPath(axisPath, paint);
        }
    }

    public void initDataSet(int size, SensorModelEnum sensorModelEnum, Function<Double, String> yFormat) {
        paintArray = new Paint[size];
        sensorModelEnumArray = new SensorModelEnum[size];
        dataSeriesArray = new int[size][];
        this.yMin = sensorModelEnum.getCoordinatorLower();
        this.yMax = sensorModelEnum.getCoordinatorUpper();
        this.yStepCount = sensorModelEnum.getStepCount();
        this.yFormat = yFormat;
    }

    public void setSensor(int curveId, SensorModelEnum sensorModelEnum) {
        paintArray[curveId] = ViewUtil.buildPaint(sensorModelEnum.getUniqueColor(), 2);
        paintArray[curveId].setAntiAlias(true);
        sensorModelEnumArray[curveId] = sensorModelEnum;
    }

    public void setDataSet(int curveId, int[] dataSeries) {
        dataSeriesArray[curveId] = dataSeries;
    }

    public void removeDataSet(int curveId) {
        dataSeriesArray[curveId] = null;
    }

    public void clearDataSet() {
        Arrays.fill(dataSeriesArray, null);
    }
}