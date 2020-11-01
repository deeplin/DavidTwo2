package com.david.core.util;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.ui.component.KeyButtonView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class ViewUtil {

    private static ConstraintSet constraintSet = new ConstraintSet();

    public static void addInnerView(ConstraintLayout rootView, View view, int width, int height, int start, int top, int end, int bottom) {
        rootView.addView(view, width, height);
        constraintSet.clone(rootView);
        if (start >= 0) {
            constraintSet.connect(view.getId(), ConstraintSet.START, rootView.getId(), ConstraintSet.START, start);
        }
        if (top >= 0) {
            constraintSet.connect(view.getId(), ConstraintSet.TOP, rootView.getId(), ConstraintSet.TOP, top);
        }
        if (end >= 0) {
            constraintSet.connect(view.getId(), ConstraintSet.END, rootView.getId(), ConstraintSet.END, end);
        }
        if (bottom >= 0) {
            constraintSet.connect(view.getId(), ConstraintSet.BOTTOM, rootView.getId(), ConstraintSet.BOTTOM, bottom);
        }
        constraintSet.applyTo(rootView);
    }

    public static KeyButtonView buildKeyButtonView(Context context) {
        KeyButtonView keyButtonView = new KeyButtonView(context);
        keyButtonView.setId(View.generateViewId());
        return keyButtonView;
    }

    public static Button buildButton(Context context) {
        Button button = new Button(context);
        button.setId(View.generateViewId());
        button.setBackgroundResource(R.drawable.button_background);
        button.setTextColor(ContextUtil.getColor(R.color.text_blue));
        button.setTextSize(20);
        button.setAllCaps(false);
        return button;
    }

    public static void setDisable(ModuleHardware moduleHardware, ModuleEnum moduleEnum, IDisableView titleSetView, boolean isPink) {
        int backgroundColor;
        if (isPink) {
            backgroundColor = R.drawable.background_panel_pink;
        } else {
            backgroundColor = R.drawable.background_panel_blue;
        }
        if (moduleHardware.isActive(moduleEnum)) {
            titleSetView.setDisable(false, backgroundColor);
            titleSetView.setVisibility(View.VISIBLE);
        } else if (moduleHardware.isInActive(moduleEnum)) {
            titleSetView.setDisable(true, backgroundColor);
            titleSetView.setVisibility(View.VISIBLE);
        } else {
            titleSetView.setVisibility(View.GONE);
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

    public static void showToast(String message) {
        Observable.just(message).observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {
                    Toast toast = Toast.makeText(ContextUtil.getApplicationContext(), msg, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
    }
}
