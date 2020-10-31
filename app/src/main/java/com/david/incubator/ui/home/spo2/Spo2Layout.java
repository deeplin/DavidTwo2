package com.david.incubator.ui.home.spo2;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.control.SensorModelRepository;
import com.david.core.database.DaoControl;
import com.david.core.database.entity.Spo2Entity;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.IntervalUtil;
import com.david.core.util.ListUtil;
import com.david.core.util.TimeUtil;
import com.david.core.util.rely.RangeUtil;
import com.david.databinding.LayoutSpo2Binding;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.Consumer;

public class Spo2Layout extends BindingBasicLayout<LayoutSpo2Binding> implements Consumer<Long> {

    private static final int RAINBOW_POINT_SUM = 750;

    @Inject
    ConfigRepository configRepository;
    @Inject
    SensorModelRepository sensorModelRepository;
    @Inject
    IntervalUtil intervalUtil;
    @Inject
    DaoControl daoControl;
    @Inject
    SystemModel systemModel;

    private final int[][] destArray;
    private final List<Integer> activeSpo2Module;

    public Spo2Layout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);

        activeSpo2Module = configRepository.getActiveSpo2Module();
        switch (activeSpo2Module.size()) {
            case (0):
                binding.spo2DataWaveView0.setSensor0(SensorModelEnum.Spo2);
                binding.spo2DataWaveView1.setSensor0(SensorModelEnum.Pr);
                binding.spo2DataWaveView2.setVisibility(View.GONE);
                destArray = new int[2][RAINBOW_POINT_SUM];
                break;
            case (1):
                binding.spo2DataWaveView0.setSensor0(SensorModelEnum.Spo2);
                binding.spo2DataWaveView1.setSensor0(SensorModelEnum.Pr);
                SensorModelEnum sensorModelEnum = SensorModelEnum.values()
                        [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(0)];
                binding.spo2DataWaveView2.setSensor0(sensorModelEnum);
                destArray = new int[3][RAINBOW_POINT_SUM];
                break;
            default:
                int count = 4;
                binding.spo2DataWaveView0.setSensor0(SensorModelEnum.Spo2);
                binding.spo2DataWaveView0.setSensor1(SensorModelEnum.Pr);
                SensorModelEnum sensorModelEnum0 = SensorModelEnum.values()
                        [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(0)];
                binding.spo2DataWaveView1.setSensor0(sensorModelEnum0);
                SensorModelEnum sensorModelEnum1 = SensorModelEnum.values()
                        [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(1)];
                binding.spo2DataWaveView2.setSensor0(sensorModelEnum1);
                if (activeSpo2Module.size() > 2) {
                    count++;
                    SensorModelEnum sensorModelEnum2 = SensorModelEnum.values()
                            [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(2)];
                    binding.spo2DataWaveView1.setSensor1(sensorModelEnum2);
                }
                if (activeSpo2Module.size() > 3) {
                    count++;
                    SensorModelEnum sensorModelEnum3 = SensorModelEnum.values()
                            [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(3)];
                    binding.spo2DataWaveView2.setSensor1(sensorModelEnum3);
                }
                destArray = new int[count][RAINBOW_POINT_SUM];
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_spo2;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        binding.spo2SurfaceView.attach();

        int gain = configRepository.getConfig(ConfigEnum.Spo2Gain).getValue();
        binding.gain.setText(ListUtil.ecgGainList.get(gain));
        int speed = configRepository.getConfig(ConfigEnum.Spo2Speed).getValue();
        binding.speed.setText(ListUtil.spo2SpeedList.get(speed));

        binding.incubatorLayout.attach(lifeCycleOwner);
        binding.spo2ListLayout.attach(lifeCycleOwner);

        binding.spo2DataWaveView0.attach(lifeCycleOwner);
        binding.spo2DataWaveView1.attach(lifeCycleOwner);
        binding.spo2DataWaveView2.attach(lifeCycleOwner);

        intervalUtil.addSecondConsumer(Spo2Layout.class, this);

        if (systemModel.darkMode.getValue()) {
            binding.spo2SurfaceView.setBackgroundResource(R.drawable.background_panel_dark);
            binding.backgroundView.setBackgroundResource(R.drawable.background_panel_dark);
        } else {
            binding.spo2SurfaceView.setBackgroundResource(R.drawable.background_panel_white);
            binding.backgroundView.setBackgroundResource(R.drawable.background_panel_white);
        }
    }

    @Override
    public void detach() {
        super.detach();
        intervalUtil.removeSecondConsumer(Spo2Layout.class);

        binding.spo2DataWaveView2.detach();
        binding.spo2DataWaveView1.detach();
        binding.spo2DataWaveView0.detach();

        binding.spo2ListLayout.detach();
        binding.incubatorLayout.detach();
        binding.spo2SurfaceView.detach();
    }

    private int count = 5;

    @Override
    public void accept(Long aLong) {
        if (count < 5) {
            count++;
            return;
        }
        count = 0;
        long endTimeStamp = TimeUtil.getCurrentTimeInSecond();
        long startTimeStamp = endTimeStamp - RAINBOW_POINT_SUM * 5;

        fillData(startTimeStamp, endTimeStamp, SensorModelEnum.Spo2, destArray[0]);
        fillData(startTimeStamp, endTimeStamp, SensorModelEnum.Pr, destArray[1]);
        switch (activeSpo2Module.size()) {
            case (0):
                binding.spo2DataWaveView0.drawAll(0, destArray[0]);
                binding.spo2DataWaveView0.repaint();
                binding.spo2DataWaveView1.drawAll(0, destArray[1]);
                binding.spo2DataWaveView1.repaint();
                break;
            case (1):
                binding.spo2DataWaveView0.drawAll(0, destArray[0]);
                binding.spo2DataWaveView0.repaint();
                binding.spo2DataWaveView1.drawAll(0, destArray[1]);
                binding.spo2DataWaveView1.repaint();
                SensorModelEnum sensorModelEnum = SensorModelEnum.values()
                        [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(0)];
                fillData(startTimeStamp, endTimeStamp, sensorModelEnum, destArray[2]);
                binding.spo2DataWaveView2.drawAll(0, destArray[2]);
                binding.spo2DataWaveView2.repaint();
                break;
            default:
                binding.spo2DataWaveView0.drawAll(0, destArray[0]);
                binding.spo2DataWaveView0.drawAll(1, destArray[1]);
                binding.spo2DataWaveView0.repaint();

                SensorModelEnum sensorModelEnum0 = SensorModelEnum.values()
                        [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(0)];
                fillData(startTimeStamp, endTimeStamp, sensorModelEnum0, destArray[2]);
                binding.spo2DataWaveView1.drawAll(0, destArray[2]);
                SensorModelEnum sensorModelEnum1 = SensorModelEnum.values()
                        [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(1)];
                fillData(startTimeStamp, endTimeStamp, sensorModelEnum1, destArray[3]);
                binding.spo2DataWaveView2.drawAll(0, destArray[3]);
                if (activeSpo2Module.size() > 2) {
                    SensorModelEnum sensorModelEnum2 = SensorModelEnum.values()
                            [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(2)];
                    fillData(startTimeStamp, endTimeStamp, sensorModelEnum2, destArray[4]);
                    binding.spo2DataWaveView1.drawAll(1, destArray[4]);
                }
                if (activeSpo2Module.size() > 3) {
                    SensorModelEnum sensorModelEnum3 = SensorModelEnum.values()
                            [SensorModelEnum.Spo2.ordinal() + activeSpo2Module.get(3)];
                    fillData(startTimeStamp, endTimeStamp, sensorModelEnum3, destArray[5]);
                    binding.spo2DataWaveView2.drawAll(1, destArray[5]);
                }
                binding.spo2DataWaveView1.repaint();
                binding.spo2DataWaveView2.repaint();
                break;
        }
    }

    private void fillData(long startTimeStamp, long endTimeStamp, SensorModelEnum sensorModelEnum, int[] destList) {
        Spo2Entity[] srcList = daoControl.getSpo2DaoOperation().get(startTimeStamp, endTimeStamp);
        RangeUtil.fillData(RAINBOW_POINT_SUM, endTimeStamp, srcList, destList, 5, sensorModelEnum);
    }
}