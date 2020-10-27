package com.david.core.enumeration;

import com.david.R;

/**
 * author: Ling Lin
 * created on: 2017/7/17 20:12
 * email: 10525677@qq.com
 * description:
 */

public enum CtrlEnum {
    Skin(R.string.skin_mode), Air(R.string.air_mode), Manual(R.string.manual_mode), Prewarm(R.string.prewarm_mode), Standby;

    private final int textId;

    CtrlEnum() {
        this(-1);
    }

    CtrlEnum(int textId) {
        this.textId = textId;
    }

    public int getTextId() {
        return textId;
    }
}