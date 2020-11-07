package com.david.core.enumeration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;

import java.util.Locale;
import java.util.Objects;

/**
 * author: Ling Lin
 * created on: 2017/7/20 11:23
 * email: 10525677@qq.com
 * description:
 */

public enum LanguageEnum {
    CHN("中文"), ENG("English");
//    TUR("TUR"), POL("POL"), RUS("RUS");

    private static final String LANGUAGE_ID = "languageId";
    private final String text;

    LanguageEnum(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    private static Locale getLanguageLocal(LanguageEnum languageEnum) {
        if (Objects.equals(languageEnum.ordinal(), LanguageEnum.CHN.ordinal())) {
            return Locale.CHINESE;
        }
        if (Objects.equals(languageEnum.ordinal(), LanguageEnum.ENG.ordinal())) {
            return Locale.ENGLISH;
        }
//        else if (languageIndex == LanguageEnum.TUR.ordinal()) {
//            Locale turkish = new Locale("tr", "TR");
//            setLocalLanguage(ContextUtil.getApplicationContext(), turkish);
//        } else if (languageIndex == LanguageEnum.POL.ordinal()) {
//            Locale spanish = new Locale("pl", "PL");
//            setLocalLanguage(ContextUtil.getApplicationContext(), spanish);
//        } else if (languageIndex == LanguageEnum.RUS.ordinal()) {
//            Locale russia = new Locale("ru", "RU");
//            setLocalLanguage(ContextUtil.getApplicationContext(), russia);
//        }
        return null;
    }

    public static LanguageEnum getLanguage() {
        final SharedPreferences sharedPreferences = ContextUtil.getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE);
        int languageId = sharedPreferences.getInt(LANGUAGE_ID, Constant.NA_VALUE);
        if (languageId == Constant.NA_VALUE) {
            languageId = LanguageEnum.ENG.ordinal();
            sharedPreferences.edit().putInt(LANGUAGE_ID, languageId).apply();
        }
        return LanguageEnum.values()[languageId];
    }

    public static void saveLanguage(LanguageEnum languageEnum) {
        final SharedPreferences sharedPreferences = ContextUtil.getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(LANGUAGE_ID, languageEnum.ordinal()).apply();
    }

    public static void setLanguage(Context context, LanguageEnum languageEnum) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(getLanguageLocal(languageEnum));
        resources.updateConfiguration(configuration, metrics);
    }
}