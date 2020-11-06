package com.david.incubator.ui.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.david.core.enumeration.LayoutPageEnum;
import com.david.core.ui.layout.BaseLayout;
import com.david.core.util.ViewUtil;

public class CameraImageLayout extends BaseLayout {

    public static String filePath;

    private final ImageView imageView;

    public CameraImageLayout(Context context) {
        super(context);
        super.init(LayoutPageEnum.CAMERA_IMAGE);

        imageView = new ImageView(context);
        imageView.setId(View.generateViewId());
        ViewUtil.addInnerView(this, imageView, 742, 512, 4, 56, 4, 4);
    }

    @Override
    public void attach() {
        super.attach();
        Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
        imageView.setImageBitmap(myBitmap);
    }

    @Override
    public void detach() {
        super.detach();
        imageView.setImageBitmap(null);
    }
}