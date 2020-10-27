package com.david.core.util;

import android.util.ArrayMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class IntervalUtil implements ILifeCycle {

    private final Map<String, Consumer<Long>> secondMap;
    private final Map<String, Consumer<Long>> millisecond500Map;

    private Disposable disposable;

    @Inject
    public IntervalUtil() {
        secondMap = new ArrayMap<>();
        millisecond500Map = new ArrayMap<>();
    }

    @Override
    public synchronized void attach() {
        disposable = Observable.interval(1000, 500, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .subscribe(aLong -> {
                    synchronized (IntervalUtil.this) {
                        for (Consumer<Long> consumer : millisecond500Map.values()) {
                            try {
                                consumer.accept(aLong);
                            } catch (Throwable e) {
                                LoggerUtil.e(e);
                            }
                        }

                        if (aLong % 2 == 0) {
                            for (Consumer<Long> consumer : secondMap.values()) {
                                try {
                                    consumer.accept(aLong);
                                } catch (Throwable e) {
                                    LoggerUtil.e(e);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public synchronized void detach() {
        disposable.dispose();
        disposable = null;
        secondMap.clear();
        millisecond500Map.clear();
    }

    public synchronized void addSecondConsumer(Class clazz, Consumer<Long> consumer) {
        secondMap.put(clazz.getSimpleName(), consumer);
    }

    public synchronized void removeSecondConsumer(Class clazz) {
        secondMap.remove(clazz.getSimpleName());
    }

    public synchronized void addMilliSecond500Consumer(Class clazz, Consumer<Long> consumer) {
        millisecond500Map.put(clazz.getSimpleName(), consumer);
    }

    public synchronized void removeMillisecond500Consumer(Class clazz) {
        millisecond500Map.remove(clazz.getSimpleName());
    }
}