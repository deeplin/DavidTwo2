package com.david.core.util;

import androidx.lifecycle.LifecycleOwner;

public interface ILifeCycleOwner {
    void attach(LifecycleOwner lifecycleOwner);

    void detach();
}
