package com.david.core.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;

public class MyGridLayout extends GridLayout {

    public MyGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public void set(int rowCount, int columnCount) {
//        super.setRowCount(rowCount);
//        super.setColumnCount(columnCount);
//    }
//
//    public void addFill(View view, int row, int column, int width, int height) {
//        Spec rowSpec = GridLayout.spec(row);
//        Spec columnSpec = GridLayout.spec(column);
//        LayoutParams layoutParams = new LayoutParams(rowSpec, columnSpec);
//        layoutParams.width = width;
//        if (height > 0)
//            layoutParams.height = height;
//        layoutParams.setGravity(Gravity.FILL);
//        addView(view, layoutParams);
//    }
//
//    public void add(View view, int row, int column) {
//        Spec rowSpec = GridLayout.spec(row, 1f);
//        Spec columnSpec = GridLayout.spec(column, 1f);
//        LayoutParams layoutParams = new LayoutParams(rowSpec, columnSpec);
//        layoutParams.setGravity(Gravity.CENTER);
//        addView(view, layoutParams);
//    }
//
//    public void add(View view, int row, int column, int rowSize, int columnSize, int width, int height) {
//        Spec rowSpec = GridLayout.spec(row, rowSize, 1f);
//        Spec columnSpec = GridLayout.spec(column, columnSize, 1f);
//        LayoutParams layoutParams = new LayoutParams(rowSpec, columnSpec);
//        layoutParams.width = width;
//        layoutParams.height = height;
//        layoutParams.setGravity(Gravity.CENTER);
//        addView(view, layoutParams);
//    }
}
