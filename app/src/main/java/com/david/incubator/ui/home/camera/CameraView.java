package com.david.incubator.ui.home.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;

import com.david.R;
import com.david.core.model.SystemModel;
import com.david.core.ui.layout.BindingBasicLayout;
import com.david.core.util.CameraUtil;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.FileUtil;
import com.david.core.util.IntervalUtil;
import com.david.core.util.LazyLiveData;
import com.david.core.util.LoggerUtil;
import com.david.core.util.TimeUtil;
import com.david.core.util.ViewUtil;
import com.david.databinding.ViewCameraBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;

public class CameraView extends BindingBasicLayout<ViewCameraBinding> implements Consumer<Long> {

    @Inject
    IntervalUtil intervalUtil;
    @Inject
    SystemModel systemModel;

    public final LazyLiveData<Boolean> isRecordingVideo = new LazyLiveData<>(false);

    private CameraViewModel cameraViewModel;
    //Camera2
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;
    private CameraCaptureSession cameraCaptureSession;

    //handler
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;

    private final TextureView.SurfaceTextureListener surfaceTextureListener;
    private final CameraDevice.StateCallback stateCallback;

    private MediaRecorder mediaRecorder;
    private String recordingFileName;
    private int startTime;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ContextUtil.getComponent().inject(this);

        cameraViewModel = new CameraViewModel();
        binding.setViewModel(cameraViewModel);

        if (!FileUtil.makeDirectory(CameraUtil.buildDirectory(CameraUtil.VIDEO_DIRECTORY))) {
            cameraViewModel.enableCapture.set(false);
            cameraViewModel.showNoButton();
        }
        if (!FileUtil.makeDirectory(CameraUtil.buildDirectory(CameraUtil.IMAGE_DIRECTORY))) {
            cameraViewModel.enableCapture.set(false);
            cameraViewModel.showNoButton();
        }

        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };

        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                CameraView.this.cameraDevice = cameraDevice;
                try {
                    startPreview();
                } catch (Exception e) {
                    cameraViewModel.hasError.post(true);
                    LoggerUtil.e(e);
                }
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                cameraDevice.close();
                CameraView.this.cameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                CameraView.this.cameraDevice = null;
            }
        };

        isRecordingVideo.observeForever(aBoolean -> {
            if (isRecordingVideo.getValue()) {
                cameraViewModel.showRecordButton();
            } else {
                cameraViewModel.showCaptureButton();
            }
        });

        binding.ivLeft.setOnClickListener(new OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                if (!isRecordingVideo.getValue()) {
                    String fileName = getFileName();
                    saveImage(fileName);
                    ViewUtil.showToast(String.format(ContextUtil.getString(R.string.capture_confirm), fileName));
                }
            }
        });

        binding.ivRight.setOnClickListener(new OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                try {
                    if (isRecordingVideo.getValue()) {
                        isRecordingVideo.set(false);
                        intervalUtil.removeSecondConsumer(this.getClass());
                        stopSession();
                        stopRecordingVideo();
                        ViewUtil.showToast(String.format(ContextUtil.getString(R.string.capture_confirm), recordingFileName));
                    } else {
                        isRecordingVideo.set(true);
                        startTime = 0;
                        cameraViewModel.recordString.set("00:00:00");
                        intervalUtil.addSecondConsumer(this.getClass(), CameraView.this);

                        recordingFileName = getFileName();
                        startRecordingVideo(recordingFileName);
                    }
                } catch (Exception e) {
                    LoggerUtil.e(e);
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_camera;
    }

    @Override
    public void attach(LifecycleOwner lifeCycleOwner) {
        super.attach(lifeCycleOwner);
        systemModel.disableLock();
        if (!isRecordingVideo.getValue()) {
            openBackgroundThread();
            if (binding.tvCamera.isAvailable()) {
                openCamera();
            } else {
                binding.tvCamera.setSurfaceTextureListener(surfaceTextureListener);
            }
        }

        Observable.just(this)
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cameraView -> {
                    binding.tvCamera.setVisibility(View.VISIBLE);
                    cameraViewModel.showCaptureButton();
                    isRecordingVideo.notifyChange();
                });
    }

    @Override
    public void detach() {
        super.detach();
        binding.tvCamera.setVisibility(View.GONE);
        cameraViewModel.showNoButton();
        if (!isRecordingVideo.getValue()) {
            closeCamera();
            closeBackgroundThread();
        }
        systemModel.enableLock();
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                throw new Exception("No camera permission");
            }
            mediaRecorder = new MediaRecorder();
            CameraManager cameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
            cameraManager.openCamera("0", stateCallback, backgroundHandler);
        } catch (Exception e) {
            cameraViewModel.hasError.set(true);
            LoggerUtil.e(e);
        }
    }

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("CameraThread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void closeCamera() {
        closePreviewSession();

        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }

        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    private void startPreview() throws Exception {
        SurfaceTexture surfaceTexture = binding.tvCamera.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(640, 480);
        previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

        Surface previewSurface = new Surface(surfaceTexture);
        previewBuilder.addTarget(previewSurface);

        cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                new CameraCaptureSession.StateCallback() {

                    @Override
                    public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                        try {
                            CameraView.this.cameraCaptureSession = cameraCaptureSession;
                            cameraCaptureSession.setRepeatingRequest(previewBuilder.build(),
                                    null, backgroundHandler);
                        } catch (CameraAccessException e) {
                            ViewUtil.showToast(ContextUtil.getString(R.string.capture_failed));
                            LoggerUtil.e(e);
                        }
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                        ViewUtil.showToast(ContextUtil.getString(R.string.capture_failed));
                    }
                }, backgroundHandler);
    }

    private void saveImage(String fileName) {
        File file = new File(CameraUtil.buildFile(CameraUtil.IMAGE_DIRECTORY, fileName));
        FileOutputStream outputPhoto = null;
        try {
            lock();
            outputPhoto = new FileOutputStream(file);
            binding.tvCamera.getBitmap()
                    .compress(Bitmap.CompressFormat.JPEG, 100, outputPhoto);

            removeImageFile();
        } catch (Exception e) {
            LoggerUtil.e(e);
        } finally {
            try {
                unlock();
                if (outputPhoto != null) {

                    outputPhoto.close();
                }
            } catch (Exception e) {
                LoggerUtil.e(e);
            }
        }
    }

    private void lock() throws Exception {
        cameraCaptureSession.capture(previewBuilder.build(),
                null, backgroundHandler);
    }

    private void unlock() throws Exception {
        cameraCaptureSession.setRepeatingRequest(previewBuilder.build(),
                null, backgroundHandler);
    }

    private void startRecordingVideo(String fileName) throws Exception {
        closePreviewSession();
        setUpMediaRecorder(CameraUtil.buildFile(CameraUtil.VIDEO_DIRECTORY, fileName));
        SurfaceTexture texture = binding.tvCamera.getSurfaceTexture();

        texture.setDefaultBufferSize(640, 480);
        previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
        List<Surface> surfaces = new ArrayList<>();

        // Set up Surface for the camera preview
        Surface previewSurface = new Surface(texture);
        surfaces.add(previewSurface);
        previewBuilder.addTarget(previewSurface);

        // Set up Surface for the MediaRecorder
        Surface recorderSurface = mediaRecorder.getSurface();
        surfaces.add(recorderSurface);
        previewBuilder.addTarget(recorderSurface);

        // Start a capture session
        // Once the session starts, we can update the UI and start recording
        cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                previewSession = cameraCaptureSession;
                try {
                    previewSession.setRepeatingRequest(previewBuilder.build(), null, backgroundHandler);
                } catch (Exception e) {
                    ViewUtil.showToast(ContextUtil.getString(R.string.capture_failed));
                    LoggerUtil.e(e);
                }
                mediaRecorder.start();
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                ViewUtil.showToast(ContextUtil.getString(R.string.capture_failed));
            }
        }, backgroundHandler);
    }

    private void closePreviewSession() {
        if (previewSession != null) {
            previewSession.close();
            previewSession = null;
        }
    }

    private void setUpMediaRecorder(String fullFileName) throws IOException {
        mediaRecorder.reset();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setVideoEncodingBitRate(1024 * 128);
        mediaRecorder.setVideoFrameRate(5);
        mediaRecorder.setVideoSize(320, 240);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(fullFileName);
        mediaRecorder.prepare();
    }

    private void stopSession() throws Exception {
        previewSession.stopRepeating();
    }

    private void stopRecordingVideo() throws Exception {
//        previewSession.stopRepeating();
        Thread.sleep(2000);
        if (mediaRecorder == null) {
            return;
        }
        previewSession.abortCaptures();

        // Stop recording

        try {
            //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();
            //报try错为：RuntimeException:stop failed
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            mediaRecorder.setPreviewDisplay(null);
            mediaRecorder.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();
            //报try错为：RuntimeException:stop failed
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            mediaRecorder.setPreviewDisplay(null);
            mediaRecorder.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // clear recorder configuration
        mediaRecorder.reset();
        removeVideoFile();
        startPreview();
    }

    private void removeImageFile() {
        File[] files = FileUtil.listSortedFile(CameraUtil.buildDirectory(CameraUtil.IMAGE_DIRECTORY), Long.MIN_VALUE, Long.MAX_VALUE);
        if (files != null && files.length > Constant.IMAGE_MAX) {
            for (int index = 0; index < files.length - Constant.IMAGE_MAX; index++) {
                files[index].delete();
            }
        }
    }

    private void removeVideoFile() {
        File[] files = FileUtil.listSortedFile(CameraUtil.buildDirectory(CameraUtil.VIDEO_DIRECTORY), Long.MIN_VALUE, Long.MAX_VALUE);
        if (files != null) {
            int sizeSum = 0;
            for (int index = 0; index < files.length; index++) {
                sizeSum += files[index].length() / 1024;
                if (sizeSum > Constant.VIDEO_MAX_SIZE) {
                    files[index].delete();
                }
            }
        }
    }

    @Override
    public void accept(Long aLong) throws Exception {
        cameraViewModel.recordString.post(String.format(Locale.US, "%02d:%02d:%02d",
                startTime / 3600 % 24, startTime / 60 % 60, startTime % 60));

        if (startTime % 3600 == 3594) {
            stopRecordingVideo();
        } else if (startTime % 3600 == 3599) {
            recordingFileName = getFileName();
            startRecordingVideo(recordingFileName);
            cameraViewModel.showRecordButton();
        } else if (startTime % 3600 == 3592) {
            stopSession();
            cameraViewModel.showTimeNoButton();
        }

//        if (startTime % 60 == 54) {
//            stopRecordingVideo();
//        } else if (startTime % 60 == 59) {
//            recordingFileName = TimeUtil.getFileName();
//            startRecordingVideo(recordingFileName);
//        } else if (startTime % 60 == 52) {
//            stopSession();
//        }


//        if (startTime % 120 == 119) {
//            stopRecordingVideo();
//            recordingFileName = TimeUtil.getFileName();
//            cameraViewModel.showTimeNoButton();
//            Observable.just(this)
//                    .delay(2, TimeUnit.SECONDS)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(cameraView -> {
//                        cameraViewModel.showRecordButton();
//                        startRecordingVideo(recordingFileName);
//                    });
//        }
        startTime++;
    }

    public static String getFileName() {
        long time = TimeUtil.getCurrentTimeInSecond();
        return TimeUtil.getTimeFromSecond(time, TimeUtil.FILENAME);
    }
}