package com.david.core.util;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

/**
 * author: Ling Lin
 * created on: 2017/7/29 14:50
 * email: 10525677@qq.com
 * description:
 */

public class DataBindingAdapter {

    @BindingAdapter("android:backgroundResource")
    public static void setBackgroundResource(View view, int color) {
        view.setBackgroundResource(color);
    }

    @BindingAdapter("android:imageSrc")
    public static void setImageSrc(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}