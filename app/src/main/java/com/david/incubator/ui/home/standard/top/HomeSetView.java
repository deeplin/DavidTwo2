package com.david.incubator.ui.home.standard.top;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.SensorModelRepository;
import com.david.core.enumeration.CtrlEnum;
import com.david.core.enumeration.SensorModelEnum;
import com.david.core.model.IncubatorModel;
import com.david.core.model.SensorModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.FormatUtil;
import com.david.core.util.LazyLiveData;
import com.david.databinding.ViewHomeSetBinding;

import java.util.Locale;

import javax.inject.Inject;

public class HomeSetView extends BindingBasicLayout<ViewHomeSetBinding> {

    @Inject
    IncubatorModel incubatorModel;
    @Inject
    SensorModelRepository sensorModelRepository;

    public final LazyLiveData<String> ctrlMode = new LazyLiveData<>();
    public final LazyLiveData<String> incString = new LazyLiveData<>();

    private final Observer<CtrlEnum> ctrlSetObserver;
    private final Observer<Boolean> ohtestObserver;
    private final Observer<Integer> setIncObserver;
    private final Observer<Integer> setWarmerObserver;
    private final Observer<Integer> integerObserver;
    private final Observer<Integer> manObserver;
    private final Observer<Integer> cTimeObserver;

    public HomeSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);
        binding.setHomeSetView(this);

        ctrlSetObserver = obj -> setHomeSet();
        ohtestObserver = obj -> setHomeSet();

        setIncObserver = integer -> {
            if (integer >= 0) {
                if (incubatorModel.isCabin()) {
                    ViewGroup.LayoutParams layoutParams = binding.inc.getLayoutParams();
                    layoutParams.width = (int) (integer * 0.82f);
                    incString.set(integer + " %");
                }
            }
        };

        setWarmerObserver = integer -> {
            if (integer >= 0) {
                if (incubatorModel.isWarmer()) {
                    ViewGroup.LayoutParams layoutParams = binding.inc.getLayoutParams();
                    layoutParams.width = (int) (integer * 0.82f);
                    incString.set(integer + " %");
                }
            }
        };

        integerObserver = integer -> binding.objective.setText(FormatUtil.formatValueUnit(SensorModelEnum.Skin1, integer));

        manObserver = integer -> binding.objective.setText(FormatUtil.formatValue(integer, 1, "0 '%'"));

        cTimeObserver = integer -> {
            String heatString = String.format(Locale.US, "%02d:%02d", (integer / 60) % 60, integer % 60);
            binding.objective.setText(heatString);
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_home_set;
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        incubatorModel.ctrlMode.observeForever(ctrlSetObserver);
        incubatorModel.ohTest.observeForever(ohtestObserver);
        SensorModel incModel = sensorModelRepository.getSensorModel(SensorModelEnum.Inc);
        incModel.textNumber.observeForever(setIncObserver);
        SensorModel warmerModel = sensorModelRepository.getSensorModel(SensorModelEnum.Warmer);
        warmerModel.textNumber.observeForever(setWarmerObserver);
    }

    @Override
    public void detach() {
        super.detach();
        clearObserver();
        SensorModel warmerModel = sensorModelRepository.getSensorModel(SensorModelEnum.Warmer);
        warmerModel.textNumber.removeObserver(setWarmerObserver);
        SensorModel incModel = sensorModelRepository.getSensorModel(SensorModelEnum.Inc);
        incModel.textNumber.removeObserver(setIncObserver);
        incubatorModel.ohTest.removeObserver(ohtestObserver);
        incubatorModel.ctrlMode.removeObserver(ctrlSetObserver);
    }

    private void setHomeSet() {
        clearObserver();

        if (incubatorModel.ohTest.getValue()) {
            binding.objective.setText("-- ℃");
            ctrlMode.set(ContextUtil.getString(R.string.overheat_mode));
            setTextColor(ContextUtil.getColor(R.color.text_pink));
            binding.setView.setBackgroundResource(R.drawable.set_background_pink);
            binding.animationView.setBackgroundResource(R.drawable.inc_background_pink);
            binding.incOutline.setBackgroundResource(R.drawable.inc_outline_pink);
            return;
        }

        CtrlEnum ctrlEnum = incubatorModel.ctrlMode.getValue();
        switch (ctrlEnum) {
            case Skin:
                SensorModel skin1SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
                skin1SensorModel.objective.observeForever(integerObserver);
                ctrlMode.set(ContextUtil.getString(R.string.skin_mode));
                setTextColor(ContextUtil.getColor(R.color.text_pink));
                binding.setView.setBackgroundResource(R.drawable.set_background_pink);
                binding.animationView.setBackgroundResource(R.drawable.inc_background_pink);
                binding.incOutline.setBackgroundResource(R.drawable.inc_outline_pink);
                break;
            case Air:
                SensorModel airSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
                airSensorModel.objective.observeForever(integerObserver);
                ctrlMode.set(ContextUtil.getString(R.string.air_mode));
                setTextColor(ContextUtil.getColor(R.color.text_blue));
                binding.set.setTextColor(ContextUtil.getColor(R.color.text_blue));
                binding.setView.setBackgroundResource(R.drawable.set_background_blue);
                binding.animationView.setBackgroundResource(R.drawable.inc_background_blue);
                binding.incOutline.setBackgroundResource(R.drawable.inc_outline_blue);
                break;
            case Manual:
                incubatorModel.manualObjective.observeForever(manObserver);
                ctrlMode.set(ContextUtil.getString(R.string.manual_mode));
                setTextColor(ContextUtil.getColor(R.color.text_blue));
                binding.set.setTextColor(ContextUtil.getColor(R.color.text_blue));
                binding.setView.setBackgroundResource(R.drawable.set_background);
                binding.animationView.setBackgroundResource(R.drawable.inc_background_blue);
                binding.incOutline.setBackgroundResource(R.drawable.inc_outline_blue);
                break;
            case Prewarm:
                incubatorModel.cTime.observeForever(cTimeObserver);
                ctrlMode.set(ContextUtil.getString(R.string.prewarm_mode));
                setTextColor(ContextUtil.getColor(R.color.text_blue));
                binding.set.setTextColor(ContextUtil.getColor(R.color.text_blue));
                binding.setView.setBackgroundResource(R.drawable.set_background);
                binding.animationView.setBackgroundResource(R.drawable.inc_background_blue);
                binding.incOutline.setBackgroundResource(R.drawable.inc_outline_blue);
                break;
        }
    }

    private void clearObserver() {
        SensorModel skin1SensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Skin1);
        skin1SensorModel.objective.removeObserver(integerObserver);
        SensorModel airSensorModel = sensorModelRepository.getSensorModel(SensorModelEnum.Air);
        airSensorModel.objective.removeObserver(integerObserver);

        incubatorModel.manualObjective.removeObserver(manObserver);
        incubatorModel.cTime.removeObserver(cTimeObserver);
    }

    private void setTextColor(int color) {
        binding.set.setTextColor(color);
        binding.objective.setTextColor(color);
        binding.ctrlMode.setTextColor(color);
        binding.incString.setTextColor(color);
        binding.inc.setBackgroundColor(color);
    }
}