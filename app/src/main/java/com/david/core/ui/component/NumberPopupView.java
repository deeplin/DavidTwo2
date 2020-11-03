package com.david.core.ui.component;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.david.R;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.ui.model.IntegerButtonModel;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewNumberPopupBinding;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class NumberPopupView extends BindingBasicLayout<ViewNumberPopupBinding> {

    @Inject
    SystemModel systemModel;

    private final ConstraintSet constraintSet;
    private volatile Disposable increaseDisposable;
    private volatile Disposable decreaseDisposable;

    private IntegerButtonModel integerButtonModel;

    public NumberPopupView(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        setId(View.generateViewId());
        setZ(100.f);
        constraintSet = new ConstraintSet();

        binding.topDecreaseButton.setOnTouchListener(this::decreaseValue);
        binding.topIncreaseButton.setOnTouchListener(this::increaseValue);

        binding.bottomDecreaseButton.setOnTouchListener(this::decreaseValue);
        binding.bottomIncreaseButton.setOnTouchListener(this::increaseValue);

        binding.button.setOnClickListener(v -> confirmAndReturn());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_number_popup;
    }

    public synchronized void set(IntegerButtonModel integerPopupModel) {
        integerPopupModel.reNew();
        this.integerButtonModel = integerPopupModel;
        binding.button.setText(integerPopupModel.getNewValue());
        binding.button.setSelected(true);
    }

    public synchronized void show(ConstraintLayout rootView, int viewId, boolean downward) {
        stopDisposable();

        constraintSet.clone(rootView);
        constraintSet.connect(getId(), ConstraintSet.END, viewId, ConstraintSet.END);
        if (downward) {
            constraintSet.clear(getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(getId(), ConstraintSet.TOP, viewId, ConstraintSet.TOP);
            binding.topDecreaseButton.setVisibility(View.GONE);
            binding.topIncreaseButton.setVisibility(View.GONE);
            binding.bottomDecreaseButton.setVisibility(View.VISIBLE);
            binding.bottomIncreaseButton.setVisibility(View.VISIBLE);
        } else {
            constraintSet.clear(getId(), ConstraintSet.TOP);
            constraintSet.connect(getId(), ConstraintSet.BOTTOM, viewId, ConstraintSet.BOTTOM);
            binding.topDecreaseButton.setVisibility(View.VISIBLE);
            binding.topIncreaseButton.setVisibility(View.VISIBLE);
            binding.bottomDecreaseButton.setVisibility(View.GONE);
            binding.bottomIncreaseButton.setVisibility(View.GONE);
        }
        constraintSet.applyTo(rootView);
        setVisibility(View.VISIBLE);
    }

    public synchronized void close() {
        setVisibility(View.GONE);
        integerButtonModel = null;
    }

    private void confirmAndReturn() {
        integerButtonModel.confirm();
        close();
    }

    public synchronized boolean increaseValue(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            stopDisposable();
//            increaseButton.setPressed(true);
            integerButtonModel.increase();
            setValue();
            increaseDisposable = Observable
                    .interval(1000, Constant.LONG_CLICK_DELAY, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Long aLong) -> {
                        systemModel.initializeTimeOut();
                        integerButtonModel.increase();
                        setValue();
                    });
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL ||
                event.getAction() == MotionEvent.ACTION_UP) {
            stopDisposable();
//            increaseButton.setPressed(false);
        }
        return true;
    }

    public synchronized boolean decreaseValue(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            stopDisposable();
//            decreaseButton.setPressed(true);
            integerButtonModel.decrease();
            setValue();
            decreaseDisposable = Observable
                    .interval(1000, Constant.LONG_CLICK_DELAY, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Long aLong) -> {
                        systemModel.initializeTimeOut();
                        integerButtonModel.decrease();
                        setValue();
                    });
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL ||
                event.getAction() == MotionEvent.ACTION_UP) {
            stopDisposable();
//            decreaseButton.setPressed(false);
        }
        return true;
    }

    private synchronized void stopDisposable() {
        if (decreaseDisposable != null) {
            decreaseDisposable.dispose();
            decreaseDisposable = null;
        }
        if (increaseDisposable != null) {
            increaseDisposable.dispose();
            increaseDisposable = null;
        }
    }

    private void setValue() {
        binding.button.setText(integerButtonModel.getNewValue());
        binding.button.setSelected(integerButtonModel.equal());
    }

    public void setBigFont(boolean bigFont) {
        if (bigFont) {
            binding.button.setHeight(88);
            binding.button.setTextSize(30);
        } else {
            binding.button.setHeight(56);
            binding.button.setTextSize(20);
        }
    }
}