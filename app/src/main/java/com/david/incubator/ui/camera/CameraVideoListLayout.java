package com.david.incubator.ui.camera;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;

import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingLayout;
import com.david.core.util.CameraUtil;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.FileUtil;
import com.david.core.util.ViewUtil;
import com.david.databinding.LayoutImageListBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CameraVideoListLayout extends BindingLayout<LayoutImageListBinding> {

    private static int PAGE_ID;
    private static long START_TIME;
    private static long END_TIME;

    public static void init(long startTime, long endTime) {
        PAGE_ID = 0;
        START_TIME = startTime;
        END_TIME = endTime;
    }

    @Inject
    SystemModel systemModel;

    private final List<Button> buttonList = new ArrayList<>();
    private int buttonId;

    public CameraVideoListLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.CAMERA_VIDEO_LIST);

        boolean error = false;

        FileUtil.makeDirectory(CameraUtil.buildDirectory(CameraUtil.VIDEO_DIRECTORY));
        if (!FileUtil.makeDirectory(CameraUtil.buildDirectory(CameraUtil.VIDEO_DIRECTORY))) {
            error = true;
        }

        if (!error) {
            for (int index = 0; index < Constant.USER_PER_PAGE; index++) {
                Button button = ViewUtil.buildButton(getContext());

                button.setOnClickListener(v -> {
                    File file = (File) button.getTag();
                    CameraVideoLayout.filePath = file.getAbsolutePath();
                    systemModel.showLayout(LayoutPageEnum.CAMERA_VIDEO);
                });

                addInnerButton(index, button);
                buttonList.add(button);
            }
        }

        binding.previousPage.setOnClickListener(v -> {
            PAGE_ID--;
            show();
        });

        binding.nextPage.setOnClickListener(v -> {
            PAGE_ID++;
            show();
        });
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        show();
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void show() {
        binding.previousPage.setEnabled(PAGE_ID > 0);
        buttonId = 0;

        Observable.create((ObservableOnSubscribe<File>) emitter -> {
            File[] files = FileUtil.listSortedFile(CameraUtil.buildDirectory(CameraUtil.VIDEO_DIRECTORY), START_TIME, END_TIME);
            for (int index = Constant.USER_PER_PAGE * PAGE_ID;
                 index < Constant.USER_PER_PAGE * (PAGE_ID + 1) + 1 && index < files.length; index++) {
                emitter.onNext(files[index]);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull File file) {
                        if (buttonId < Constant.USER_PER_PAGE) {
                            Button button = buttonList.get(buttonId);
                            button.setVisibility(View.VISIBLE);
                            button.setTag(file);
                            button.setText(String.format(Locale.US, "%s", file.getName()));
                        }
                        buttonId++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        if (buttonId < Constant.USER_PER_PAGE) {
                            for (int index = buttonId; index < Constant.USER_PER_PAGE; index++) {
                                Button button = buttonList.get(index);
                                button.setTag(null);
                                button.setVisibility(View.INVISIBLE);
                            }
                            binding.nextPage.setEnabled(false);
                        } else if (buttonId > Constant.USER_PER_PAGE) {
                            binding.nextPage.setEnabled(true);
                        } else {
                            binding.nextPage.setEnabled(false);
                        }
                    }
                });
    }
}