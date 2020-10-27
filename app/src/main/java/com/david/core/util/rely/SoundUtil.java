package com.david.core.util.rely;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import androidx.lifecycle.Observer;

import com.david.R;
import com.david.core.control.ConfigRepository;
import com.david.core.enumeration.ConfigEnum;
import com.david.core.util.Constant;
import com.david.core.util.ContextUtil;
import com.david.core.util.ILifeCycle;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SoundUtil implements ILifeCycle {

    @Inject
    ConfigRepository configRepository;

    private SoundPool soundPool;
    private final SparseIntArray soundArray = new SparseIntArray();

    private final Observer<Integer> setNotificationObserver;

    @Inject
    public SoundUtil() {
        AudioManager audioManager = (AudioManager) ContextUtil.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Constant.SYSTEM_VOLUME, 0);

        setNotificationObserver = integer -> {
            AudioManager audioManager1 = (AudioManager) ContextUtil.getApplicationContext()
                    .getSystemService(Context.AUDIO_SERVICE);
            audioManager1.setStreamVolume(AudioManager.STREAM_NOTIFICATION, getVolumeMax7(integer), 0);
        };
    }

    @Override
    public void attach() {
        soundPool = new SoundPool.Builder().setMaxStreams(3).build();

        soundArray.put(R.raw.pulse_80, loadSound(R.raw.pulse_80));
        soundArray.put(R.raw.pulse_85, loadSound(R.raw.pulse_85));
        soundArray.put(R.raw.pulse_90, loadSound(R.raw.pulse_90));
        soundArray.put(R.raw.pulse_91, loadSound(R.raw.pulse_91));
        soundArray.put(R.raw.pulse_92, loadSound(R.raw.pulse_92));
        soundArray.put(R.raw.pulse_93, loadSound(R.raw.pulse_93));
        soundArray.put(R.raw.pulse_94, loadSound(R.raw.pulse_94));
        soundArray.put(R.raw.pulse_95, loadSound(R.raw.pulse_95));
        soundArray.put(R.raw.pulse_96, loadSound(R.raw.pulse_96));
        soundArray.put(R.raw.pulse_97, loadSound(R.raw.pulse_97));
        soundArray.put(R.raw.pulse_98, loadSound(R.raw.pulse_98));
        soundArray.put(R.raw.pulse_99, loadSound(R.raw.pulse_99));
        soundArray.put(R.raw.pulse_100, loadSound(R.raw.pulse_100));
        soundArray.put(R.raw.heart_beep, loadSound(R.raw.heart_beep));
        soundArray.put(R.raw.apgar, loadSound(R.raw.apgar));
        soundArray.put(R.raw.cpr1, loadSound(R.raw.cpr1));
        soundArray.put(R.raw.cpr2, loadSound(R.raw.cpr2));

        configRepository.getConfig(ConfigEnum.NotificationVolume).observeForever(setNotificationObserver);
    }

    @Override
    public void detach() {
        configRepository.getConfig(ConfigEnum.NotificationVolume).removeObserver(setNotificationObserver);
        soundArray.clear();
        soundPool.release();
    }

    private int loadSound(int soundId) {
        return soundPool.load(ContextUtil.getApplicationContext(), soundId, 0);
    }

    /*Volume: 1-5*/
    private int getVolumeMax7(int volume) {
        return (int) (volume / 0.7);
    }

    public void playPulse(int rawSoundId) {
        float volume = configRepository.getConfig(ConfigEnum.PulseVolume).getValue() / 5.0f;
        soundPool.play(soundArray.get(rawSoundId), volume, volume, 1, 0, 1);
        soundPool.play(soundArray.get(rawSoundId), 0, 0, 1, -1, 1);
    }

    public void playApgar(int rawSoundId) {
        float volume = configRepository.getConfig(ConfigEnum.ApgarVolume).getValue() / 5.0f;
        soundPool.play(soundArray.get(rawSoundId), volume, volume, 1, 0, 1);
    }
}