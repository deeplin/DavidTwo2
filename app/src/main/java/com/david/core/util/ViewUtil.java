package com.david.core.util;

import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ModuleEnum;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class ViewUtil {

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
