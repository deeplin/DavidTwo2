package com.david.incubator.ui.bottom;

import com.david.R;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:28
 * email: 10525677@qq.com
 * description:
 */
public enum MenuEnum {

    NIBP(R.mipmap.nibp, R.string.NIBP),
    SWITCH_SCREEN(R.mipmap.switch_screen, R.string.screen_choice),
    FREEZE(R.mipmap.freeze, R.string.freeze),
    TREND(R.mipmap.chart, R.string.trend_chart),
    CAMERA(R.mipmap.camera, R.string.camera),
    PRINT(R.mipmap.print, R.string.print_setup),
    ALARM_SETUP(R.mipmap.alarm_setup, R.string.alarm_setup);

    private final int imageId;
    private final int textId;

    MenuEnum(int imageId, int textId) {
        this.imageId = imageId;
        this.textId = textId;
    }

    public int getImageId() {
        return imageId;
    }


    public int getTextId() {
        return textId;
    }
}