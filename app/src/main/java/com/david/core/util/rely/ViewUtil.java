package com.david.core.util.rely;

import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.david.core.util.ContextUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

/**
 * author: Ling Lin
 * created on: 2017/7/16 15:19
 * email: 10525677@qq.com
 * description:
 */

public class ViewUtil {

    private static ConstraintSet constraintSet = new ConstraintSet();

    public static void addInnerView(ConstraintLayout rootView, View view, int width, int height, int start, int top, int end, int bottom) {
        rootView.addView(view, width, height);
        constraintSet.clone(rootView);
        if (start > 0) {
            constraintSet.connect(view.getId(), ConstraintSet.START, rootView.getId(), ConstraintSet.START, start);
        }
        if (top > 0) {
            constraintSet.connect(view.getId(), ConstraintSet.TOP, rootView.getId(), ConstraintSet.TOP, top);
        }
        if (end > 0) {
            constraintSet.connect(view.getId(), ConstraintSet.END, rootView.getId(), ConstraintSet.END, end);
        }
        if (bottom > 0) {
            constraintSet.connect(view.getId(), ConstraintSet.BOTTOM, rootView.getId(), ConstraintSet.BOTTOM, bottom);
        }
        constraintSet.applyTo(rootView);
    }

    public static void addInnerButton(ConstraintLayout rootView, int rowId, View view) {
        if (rowId < 8) {
            addInnerView(rootView, view, 220, 50, 80, 48 + rowId * 66, -1, -1);
        } else {
            addInnerView(rootView, view, 220, 50, -1, 48 + (rowId - 8) * 66, 80, -1);
        }
    }

    public static void addInnerTabButton(ConstraintLayout rootView, int rowId, View view) {
        if (rowId < 8) {
            addInnerView(rootView, view, 200, 50, 40, 8 + rowId * 66, -1, -1);
        } else {
            addInnerView(rootView, view, 200, 50, -1, 8 + (rowId - 8) * 66, 40, -1);
        }
    }

    public static Paint buildPaint(int strokeWidth) {
        Paint paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        return paint;
    }

    public static Paint buildPaint(int color, int strokeWidth) {
        Paint paint = buildPaint(strokeWidth);
        paint.setColor(color);
        return paint;
    }

//    public static TextView buildTextView(Context context) {
//        TextView textView = new TextView(context);
//        textView.setId(View.generateViewId());
//        textView.setGravity(Gravity.CENTER_VERTICAL);
//        textView.setTextColor(ContextUtil.getColor(R.color.st_machine_heavy));
//        textView.setTextSize(20);
//        return textView;
//    }
//
//    public static Button buildButton(Context context) {
//        Button button = new Button(context);
//        button.setId(View.generateViewId());
//        button.setBackgroundResource(R.drawable.button_background);
//        button.setTextColor(ContextUtil.getColor(R.color.st_machine_heavy));
//        button.setTextSize(20);
//        button.setAllCaps(false);
//        return button;
//    }
//
//    public static KeyButtonView buildKeyButtonView(Context context) {
//        KeyButtonView keyButtonView = new KeyButtonView(context);
//        keyButtonView.setId(View.generateViewId());
//        return keyButtonView;
//    }

    public static void showToast(String message) {
        Observable.just(message).observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {
                    Toast toast = Toast.makeText(ContextUtil.getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
    }
}