package com.david.core.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class PrintUtil {

    public static Bitmap measureAndGetBitmapByView(View view, int width, int height) {
        view.measure(width, height);
        view.layout(0, 0, width, height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public static Bitmap getBitmapByView(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}