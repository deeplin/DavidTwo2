package com.david.incubator.ui.home.standard.top;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.david.R;
import com.david.core.util.ContextUtil;

/**
 * author: Ling Lin
 * created on: 2018/1/4 22:27
 * email: 10525677@qq.com
 * description:
 */

public class TimingView extends View {

    private final Paint rectPaint;

    private int rectWidth;
    private int rectGap;
    private int rectHeight;

    private int itemNum;

    public TimingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rectPaint = new Paint();
        rectPaint.setColor(ContextUtil.getColor(R.color.text_blue));
        rectPaint.setStyle(Paint.Style.FILL);
        rectWidth = 28;
        rectHeight = 8;
        rectGap = 4;
    }

    public void setSmallLayout() {
        rectWidth = 16;
    }

    public void set(int itemNum) {
        this.itemNum = itemNum;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int index = 0; index < 7; index++) {
            int rx = index * (rectWidth + rectGap);
            canvas.drawRect(rx, 0, rx + rectWidth, rectHeight, rectPaint);
        }

        for (int index = 0; index < itemNum; index++) {
            int rx = index * (rectWidth + rectGap);
            canvas.drawRect(rx, 12, rx + rectWidth, 28, rectPaint);
        }
    }
}