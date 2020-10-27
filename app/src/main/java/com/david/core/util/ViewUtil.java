package com.david.core.util;

import android.view.View;

import com.david.R;
import com.david.core.control.ModuleHardware;
import com.david.core.enumeration.ModuleEnum;
import com.david.core.ui.view.TitleIconView;
import com.david.core.ui.view.TitleSetView;

public class ViewUtil {

    public static void setDisable(ModuleHardware moduleHardware, ModuleEnum moduleEnum, TitleSetView titleSetView, boolean isPink) {
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

    public static void setDisable(ModuleHardware moduleHardware, ModuleEnum moduleEnum, TitleIconView titleIconView, boolean isPink) {
//        int backgroundColor;
//        if (isPink) {
//            backgroundColor = R.drawable.background_panel_pink;
//        } else {
//            backgroundColor = R.drawable.background_panel_blue;
//        }
//        if (moduleHardware.isActive(moduleEnum)) {
//            titleIconView.setDisable(false, backgroundColor);
//            titleIconView.setVisibility(View.VISIBLE);
//        } else if (moduleHardware.isInActive(moduleEnum)) {
//            titleIconView.setDisable(true, backgroundColor);
//            titleIconView.setVisibility(View.VISIBLE);
//        } else {
//            titleIconView.setVisibility(View.GONE);
//        }
    }
}
