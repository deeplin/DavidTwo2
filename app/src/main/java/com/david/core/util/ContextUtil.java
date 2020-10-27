package com.david.core.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import androidx.core.content.ContextCompat;

import com.david.incubator.control.DaggerMainComponent;
import com.david.incubator.control.MainComponent;

public class ContextUtil {

    private static Context applicationContext;
    private static MainComponent mainComponent;

    static {
        mainComponent = DaggerMainComponent.builder().build();
    }

    public static MainComponent getComponent() {
        return mainComponent;
    }

    public static void initialize(Application application) {
        applicationContext = application.getApplicationContext();
    }

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static String getString(int resourceId) {
        return applicationContext.getResources().getString(resourceId);
    }

    public static int getColor(int resourceId) {
        return ContextCompat.getColor(applicationContext, resourceId);
    }

    public static int getStringsId(String name) {
        Resources res = applicationContext.getResources();
        return res.getIdentifier(name, "string", applicationContext.getPackageName());
    }
}