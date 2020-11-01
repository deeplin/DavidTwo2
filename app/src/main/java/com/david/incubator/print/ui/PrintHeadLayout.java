package com.david.incubator.print.ui;

import android.content.Context;

import com.david.R;
import com.david.core.database.entity.UserEntity;
import com.david.core.enumeration.BloodTypeEnum;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.ContextUtil;
import com.david.databinding.ViewPrintHeaderBinding;

import java.util.Locale;

public class PrintHeadLayout extends BindingBasicLayout<ViewPrintHeaderBinding> {

    public PrintHeadLayout(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_print_header;
    }

    public void setUserModel(UserEntity userEntity, String time) {
        String userId = "";
        String userName = "";
        String sex = "";
        String bloodType = "";

        if (userEntity != null) {
            userId = userEntity.userId;
            userName = userEntity.name;
            sex = userEntity.sex ? ContextUtil.getString(R.string.male) : ContextUtil.getString(R.string.female);
            bloodType = BloodTypeEnum.values()[userEntity.bloodType].name();
        }
        binding.lastName.setText(String.format(Locale.US, "%s: %s", ContextUtil.getString(R.string.id), userEntity != null ? userId : ""));
        binding.firstName.setText(String.format(Locale.US, "%s: %s", ContextUtil.getString(R.string.name), userName));
        binding.sex.setText(String.format(Locale.US, "%s: %s", ContextUtil.getString(R.string.sex), sex));
        binding.bloodType.setText(String.format(Locale.US, "%s: %s", ContextUtil.getString(R.string.blood_type), bloodType));
        binding.printTime.setText(String.format(Locale.US, "%s: %s", ContextUtil.getString(R.string.print_time), time));
    }
}