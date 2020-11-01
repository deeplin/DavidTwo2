package com.david.incubator.ui.menu;

import android.content.Context;
import android.widget.TextView;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.KeyButtonEnum;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.FileUtil;
import com.david.core.util.LoggerUtil;
import com.david.core.util.ViewUtil;

import java.util.Locale;

import javax.inject.Inject;

public class MenuComfortZoneLayout extends BaseLayout {

    @Inject
    ConfigRepository configRepository;

    private String[] gestationDataArray;
    private String[] weightDataArray;

    private final TextView suggestedTextView;

    public MenuComfortZoneLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.MENU_COMFORT_ZONE);

        loadAssets();

        loadLiveDataItems(KeyButtonEnum.MENU_COMFORT_ZONE_AGE, 3, null, null);

        setOriginValue(0, ConfigEnum.ComfortZoneAge);
        setOriginValue(1, ConfigEnum.ComfortZoneGestation);
        setOriginValue(2, ConfigEnum.ComfortZoneWeight);

        setCallback(0, this::display);
        setCallback(1, this::display);
        setCallback(2, this::display);

        suggestedTextView = ViewUtil.buildTextView(getContext());
        addInnerView(3, suggestedTextView);
    }

    @Override
    public void attach() {
        super.attach();
        display(0);
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void loadAssets() {
        try {
            String GESTATION_DATA_FILE = "ComfortZoneGestation.txt";
            String gestationSource = FileUtil.readTextFileFromAssets(GESTATION_DATA_FILE);
            gestationDataArray = gestationSource.split("\t");

            String WEIGHT_DATA_FILE = "ComfortZoneWeight.txt";
            String weightSource = FileUtil.readTextFileFromAssets(WEIGHT_DATA_FILE);
            weightDataArray = weightSource.split("\t");
        } catch (Exception e) {
            LoggerUtil.e(e);
        }
    }

    private void display(Integer value) {
        int ageValue = configRepository.getConfig(ConfigEnum.ComfortZoneAge).getValue();
        int gestationValue = configRepository.getConfig(ConfigEnum.ComfortZoneGestation).getValue();
        int weightValue = configRepository.getConfig(ConfigEnum.ComfortZoneWeight).getValue();
        if (ageValue <= 7) {
            int index = (ageValue - 1) * 10 + gestationValue - 28;
            suggestedTextView.setText(String.format("%s %s ℃", ContextUtil.getString(R.string.proposed_setting_range),
                    gestationDataArray[index]));
        } else {
            int index = (ageValue - 8) * 18 + weightValue / 100 - 9;
            suggestedTextView.setText(String.format("%s %s ℃", ContextUtil.getString(R.string.proposed_setting_range),
                    weightDataArray[index]));
        }
    }

    public static String getGestationString(Integer key) {
        String text = "";
        if (key == 28) {
            text = "<29";
        } else if (key == 37) {
            text = ">36";
        } else if ((key < 37) && (key > 28)) {
            text = String.valueOf(key);
        }
        return text;
    }

    public static String getWeightString(Integer weight) {
        String text = "";
        if (weight == 900) {
            text = "500-1000";
        } else if (weight == 2600) {
            text = "2600-4000";
        } else if (weight < 2600 && weight > 900) {
            text = String.format(Locale.US, "%d-%d", weight, weight + 100);
        }
        return text;
    }
}