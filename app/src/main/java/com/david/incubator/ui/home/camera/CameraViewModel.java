package com.david.incubator.ui.home.camera;


import com.david.R;
import com.david.core.util.LazyLiveData;

public class CameraViewModel {

    public final LazyLiveData<Boolean> hasError = new LazyLiveData(false);

    public final LazyLiveData<Boolean> enableCapture = new LazyLiveData<>(true);

    public final LazyLiveData<Integer> rightImage = new LazyLiveData<>();
    public final LazyLiveData<Integer> leftImage = new LazyLiveData<>();

    public final LazyLiveData<Boolean> recordIcon = new LazyLiveData<>();
    public final LazyLiveData<String> recordString = new LazyLiveData<>();

    public CameraViewModel() {
    }

    public void showCaptureButton() {
        if (enableCapture.getValue()) {
            leftImage.post(R.drawable.ic_photo_camera);
            rightImage.post(R.drawable.ic_video_camera);
            recordIcon.post(false);
        }
    }

    public void showRecordButton() {
        if (enableCapture.getValue()) {
            leftImage.post(0);
            rightImage.post(R.drawable.ic_stop);
            recordIcon.post(true);
        }
    }

    public void showNoButton() {
        leftImage.post(0);
        rightImage.post(0);
        recordIcon.post(false);
    }
    public void showTimeNoButton() {
        leftImage.post(0);
        rightImage.post(0);
        recordIcon.post(true);
    }
}
