package com.david.incubator.ui.menu;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.david.R;
import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.serial.incubator.IncubatorCommandSender;
import com.david.core.serial.incubator.command.other.VersionCommand;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ContextUtil;
import com.david.core.util.ViewUtil;

import javax.inject.Inject;

public class MenuVersionLayout extends BaseLayout {

    @Inject
    IncubatorCommandSender incubatorCommandSender;

    private final TextView masterTextView;
    private final TextView slaveATextView;
    private final TextView isoSlaveTextView;
    private final TextView alarmTextView;
    private final TextView androidTextView;

    public MenuVersionLayout(Context context) {
        super(context);
        ContextUtil.getComponent().inject(this);
        super.init(LayoutPageEnum.MENU_VERSION);

        masterTextView = ViewUtil.buildTextView(getContext());
        addFullInnerView(0, masterTextView);

        slaveATextView = ViewUtil.buildTextView(getContext());
        addFullInnerView(1, slaveATextView);

        isoSlaveTextView = ViewUtil.buildTextView(getContext());
        addFullInnerView(2, isoSlaveTextView);

        alarmTextView = ViewUtil.buildTextView(getContext());
        addFullInnerView(3, alarmTextView);

        androidTextView = ViewUtil.buildTextView(getContext());
        addFullInnerView(4, androidTextView);
    }

    @Override
    public void attach() {
        super.attach();

        setMaster("");
        setSlaveA("");
        setIsoSlave("");
        setAlarm("");

        PackageManager manager = getContext().getPackageManager();
        String versionName = "";
        try {
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        setAndroid(versionName);

        incubatorCommandSender.getVersion((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                masterTextView.post(() -> {
                    VersionCommand versionCommand = (VersionCommand) baseSerialMessage;
                    setMaster(versionCommand.getMAIN());
                    setSlaveA(versionCommand.getSLAVE_A());
                    setIsoSlave(versionCommand.getISO_SLAVE());
                    setAlarm(versionCommand.getALARM());
                });
            }
        });
    }

    @Override
    public void detach() {
        super.detach();
    }

    private void setMaster(String version) {
        String title = ContextUtil.getString(R.string.master_version);
        masterTextView.setText(String.format("%s: %s", title, version));
    }

    private void setSlaveA(String version) {
        String title = ContextUtil.getString(R.string.slave_version_a);
        slaveATextView.setText(String.format("%s: %s", title, version));
    }

    private void setIsoSlave(String version) {
        String title = ContextUtil.getString(R.string.slave_version_iso);
        isoSlaveTextView.setText(String.format("%s: %s", title, version));
    }

    private void setAlarm(String version) {
        String title = ContextUtil.getString(R.string.alarm_version);
        alarmTextView.setText(String.format("%s: %s", title, version));
    }

    private void setAndroid(String version) {
        String title = ContextUtil.getString(R.string.android_version);
        androidTextView.setText(String.format("%s: %s", title, version));
    }
}