package com.david.incubator.ui.patient;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LifecycleOwner;

import com.david.core.database.DaoControl;
import com.david.core.database.LastUser;
import com.david.core.database.entity.UserEntity;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingLayout;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;
import com.david.databinding.LayoutPatientListBinding;

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

public class PatientListLayout extends BindingLayout<LayoutPatientListBinding> {

    @Inject
    SystemModel systemModel;
    @Inject
    DaoControl daoControl;
    @Inject
    LastUser lastUser;

    private final List<Button> buttonList = new ArrayList<>();
    private int buttonId;

    public PatientListLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.PATIENT_LIST);

        for (int index = 0; index < Constant.USER_PER_PAGE; index++) {
            Button button = ViewUtil.buildButton(getContext());

            button.setOnClickListener(v -> {
                UserEntity userEntity = (UserEntity) button.getTag();
                lastUser.setEditUserEntity(userEntity);
                systemModel.showLayout(LayoutPageEnum.PATIENT_LIST_INFO);
            });

            addInnerButton(index, button);

            buttonList.add(button);
        }

        binding.previousPage.setOnClickListener(v -> {
            lastUser.userOffset -= Constant.USER_PER_PAGE;
            systemModel.layoutPage.notifyChange();
        });

        binding.previousPage.setOnClickListener(v -> {
            lastUser.userOffset += Constant.USER_PER_PAGE;
            systemModel.layoutPage.notifyChange();
        });
    }

    @Override
    public void attach(LifecycleOwner lifecycleOwner) {
        super.attach(lifecycleOwner);
        buttonId = 0;

        int offset = lastUser.userOffset;

        if (offset <= 0) {
            binding.previousPage.setEnabled(false);
        }

        Observable.create((ObservableOnSubscribe<UserEntity>) emitter -> {
            UserEntity[] userModelArray = daoControl.getUserDaoOperation()
                    .query(0, Integer.MAX_VALUE,
                            offset, Constant.USER_PER_PAGE + 1);
            for (UserEntity userEntity : userModelArray) {
                emitter.onNext(userEntity);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserEntity>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull UserEntity userEntity) {
                        if (buttonId < Constant.USER_PER_PAGE) {
                            Button button = buttonList.get(buttonId);
                            button.setVisibility(View.VISIBLE);
                            button.setTag(userEntity);
                            button.setText(String.format(Locale.US, "%s", userEntity.userId));
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

    @Override
    public void detach() {
        super.detach();
    }
}