package com.david.core.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import javax.inject.Inject;

public class MyLifecycleOwner implements LifecycleOwner {

    private final LifecycleRegistry lifecycleRegistry;

    @Inject
    public MyLifecycleOwner() {
        lifecycleRegistry = new LifecycleRegistry(this);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    public void start() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.RESUMED);
    }

    public void stop() {
        lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
    }
}
