package com.david.core.util;

import android.view.animation.AlphaAnimation;

/**
 * author: Ling Lin
 * created on: 2018/1/25 12:21
 * email: 10525677@qq.com
 * description:
 */

public class AnimationUtil {

    public static AlphaAnimation getAlphaAnimation(int duration, int start, int end) {
        AlphaAnimation animation = new AlphaAnimation(start, end);
        animation.setDuration(duration);
        animation.setRepeatCount(0);
        return animation;
    }
}