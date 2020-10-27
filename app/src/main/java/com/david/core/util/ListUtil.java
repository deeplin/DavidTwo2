package com.david.core.util;

import com.david.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListUtil {
    public static final List<Integer> statusList = new ArrayList<>();
    public static final List<Integer> volumeList = new ArrayList<>();

    public static final List<String> ecgSpeedList = new ArrayList<>();
    public static final List<String> ecgGainList = new ArrayList<>();

    public static final List<String> spo2SpeedList = new ArrayList<>();
    public static final List<String> printSpeedList = new ArrayList<>();
    public static final List<String> incubatorIntervalList = new ArrayList<>();
    public static final List<String> monitorIntervalList = new ArrayList<>();

    public static final int[] nibpInitialPressure = new int[]{60, 70, 80, 90, 100, 110, 120, 130, 140};
    public static final int[] nibpAutoInterval = new int[]{150, 300, 600, 900, 1200, 1800, 2700, 3600, 5400, 7200};
    public static final List<String> nibpAutoIntervalString = new ArrayList<>();

    public static List<String> co2mmHgRangeList = new ArrayList<>();
    public static List<String> co2kPaRangeList = new ArrayList<>();
    public static List<String> co2PercentageRangeList = new ArrayList<>();

    static {
        statusList.add(R.string.off);
        statusList.add(R.string.on);

        volumeList.add(R.string.low);
        volumeList.add(R.string.high);

        ecgSpeedList.add("6.25mm/s");
        ecgSpeedList.add("12.5mm/s");
        ecgSpeedList.add("25mm/s");
        ecgSpeedList.add("50mm/s");

        spo2SpeedList.add("6.25mm/s");
        spo2SpeedList.add("12.5mm/s");
        spo2SpeedList.add("25mm/s");

        printSpeedList.add("12.5mm/s");
        printSpeedList.add("25mm/s");
        printSpeedList.add("50mm/s");

        ecgGainList.add("×0.125");
        ecgGainList.add("×0.25");
        ecgGainList.add("×0.5");
        ecgGainList.add("×1");
        ecgGainList.add("×2");
        ecgGainList.add("×4");

        incubatorIntervalList.add("2h");
        incubatorIntervalList.add("4h");
        incubatorIntervalList.add("8h");
        incubatorIntervalList.add("12h");
        incubatorIntervalList.add("24h");
        incubatorIntervalList.add("48h");

        monitorIntervalList.add("10m");
        monitorIntervalList.add("2h");
        monitorIntervalList.add("4h");
        monitorIntervalList.add("12h");
        monitorIntervalList.add("24h");
        monitorIntervalList.add("48h");

        for (int index = 0; index < nibpAutoInterval.length; index++) {
            if (index > 0) {
                nibpAutoIntervalString.add(String.format(Locale.US, "%d", nibpAutoInterval[index] / 60));
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                nibpAutoIntervalString.add(String.format(Locale.US, "%s", decimalFormat.format(nibpAutoInterval[index] / 60.0)));
            }
        }

        co2mmHgRangeList.add("0-30mmHg");
        co2mmHgRangeList.add("0-60mmHg");
        co2mmHgRangeList.add("0-90mmHg");
        co2mmHgRangeList.add("0-150mmHg");
        co2kPaRangeList.add("0.0-4.0kPa");
        co2kPaRangeList.add("0.0-8.0kPa");
        co2kPaRangeList.add("0.0-12.0kPa");
        co2kPaRangeList.add("0.0-20.0kPa");
        co2PercentageRangeList.add("0.0-4.0%");
        co2PercentageRangeList.add("0.0-8.0%");
        co2PercentageRangeList.add("0.0-12.0%");
        co2PercentageRangeList.add("0.0-20.0%");
    }
}