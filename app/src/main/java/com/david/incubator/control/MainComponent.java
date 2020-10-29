package com.david.incubator.control;

import com.david.core.control.CommonComponent;
import com.david.incubator.ui.bottom.MenuLayout;
import com.david.incubator.ui.home.basic.BasicLayout;
import com.david.incubator.ui.home.basic.MiddleRightBasicLayout;
import com.david.incubator.ui.home.basic.StandardSpo2BasicLayout;
import com.david.incubator.ui.home.common.Co2RrTinyView;
import com.david.incubator.ui.home.common.Co2RrView;
import com.david.incubator.ui.home.common.HrPrView;
import com.david.incubator.ui.home.common.IncubatorLayout;
import com.david.incubator.ui.home.common.MonitorLayout;
import com.david.incubator.ui.home.common.NibpTinyView;
import com.david.incubator.ui.home.common.NibpView;
import com.david.incubator.ui.home.common.Spo2ListView;
import com.david.incubator.ui.home.common.TimingSmallLayout;
import com.david.incubator.ui.home.standard.StandardLayout;
import com.david.incubator.ui.home.standard.bottom.StandardSpo2Layout;
import com.david.incubator.ui.home.standard.middle.MiddleLeftLayout;
import com.david.incubator.ui.home.standard.middle.MiddleRightLayout;
import com.david.incubator.ui.home.standard.top.HomeSetView;
import com.david.incubator.ui.home.common.TimingLayout;
import com.david.incubator.ui.home.tempcurve.TempCurveLayout;
import com.david.incubator.ui.home.weightcurve.WeightCurveLayout;
import com.david.incubator.ui.main.MainActivity;
import com.david.incubator.ui.top.TopLayout;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface MainComponent extends CommonComponent {
    void inject(MainApplication mainApplication);

    void inject(MainActivity mainActivity);

    void inject(TopLayout topLayout);

    void inject(MenuLayout menuLayout);

    void inject(StandardLayout standardLayout);

    void inject(HomeSetView homeSetView);

    void inject(TimingLayout timingLayout);

    void inject(MiddleLeftLayout middleLeftLayout);

    void inject(MiddleRightLayout middleRightLayout);

    void inject(HrPrView hrPrView);

    void inject(StandardSpo2Layout standardSpo2Layout);

    void inject(Co2RrView co2RrView);

    void inject(NibpView nibpView);

    void inject(BasicLayout basicLayout);

    void inject(StandardSpo2BasicLayout standardSpo2BasicLayout);

    void inject(MiddleRightBasicLayout middleRightBasicLayout);

    void inject(TempCurveLayout tempCurveLayout);

    void inject(IncubatorLayout incubatorLayout);

    void inject(TimingSmallLayout timingSmallLayout);

    void inject(MonitorLayout monitorLayout);

    void inject(Spo2ListView spo2ListView);

    void inject(Co2RrTinyView co2RrTinyView);

    void inject(NibpTinyView nibpTinyView);

    void inject(WeightCurveLayout weightCurveLayout);
}