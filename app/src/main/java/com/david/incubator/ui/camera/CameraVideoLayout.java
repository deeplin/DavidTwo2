package com.david.incubator.ui.camera;

import android.content.Context;
import android.view.View;
import android.widget.VideoView;

import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.LoggerUtil;
import com.david.core.util.ViewUtil;

public class CameraVideoLayout extends BaseLayout {

    public static String filePath;

    private VideoView videoView;

    public CameraVideoLayout(Context context) {
        super(context);
        super.init(LayoutPageEnum.CAMERA_VIDEO);
    }

    @Override
    public void attach() {
        super.attach();
        try {
            videoView = new VideoView(getContext());
            videoView.setId(View.generateViewId());
            ViewUtil.addInnerView(this, videoView, 742, 512, 4, 56, 4, 4);
            videoView.stopPlayback();
            videoView.setVideoPath(filePath);
            videoView.start();
        } catch (Exception e) {
            LoggerUtil.e(e);
        }
    }

    @Override
    public void detach() {
        super.detach();
        if (videoView != null) {
            try {
                videoView.stopPlayback();
                removeView(videoView);
                videoView = null;
            } catch (Exception e) {
                LoggerUtil.e(e);
            }
        }
    }
}