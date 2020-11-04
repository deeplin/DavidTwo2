package com.david.core.enumeration;

import com.david.R;
import com.david.core.util.ContextUtil;

public enum EcgChannelEnum {
    I("I"), II("II"), III("III"),
    aVR("aVR"), aVL("aVL"), aVF("aVF"), V("V"),
    Spo2("SpO₂"),
//    Resp("RESP"),
    Co2("CO₂"),
    Close(null);

    private String text;

    EcgChannelEnum(String text) {
        this.text = text;
    }

    public static void init() {
        EcgChannelEnum.Close.text = ContextUtil.getString(R.string.off);
    }

    @Override
    public String toString() {
        return text;
    }

    public static boolean isEcg(EcgChannelEnum ecgChannelEnum) {
        return ecgChannelEnum.ordinal() <= V.ordinal();
    }

    public static boolean isSpo2(EcgChannelEnum ecgChannelEnum) {
        return ecgChannelEnum.ordinal() == Spo2.ordinal();
    }

//    public static boolean isResp(EcgChannelEnum ecgChannelEnum) {
////        return ecgChannelEnum.ordinal() == Resp.ordinal();
//    }

    public static boolean isCo2(EcgChannelEnum ecgChannelEnum) {
        return ecgChannelEnum.ordinal() == Co2.ordinal();
    }
}