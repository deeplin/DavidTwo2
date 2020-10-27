package com.david.core.util;

import androidx.lifecycle.LiveData;

import java.util.Objects;

public class LazyLiveData<T> extends LiveData<T> {

    private T data;

    public LazyLiveData(T value) {
        super();
        data = value;
        super.setValue(value);
    }

    public LazyLiveData() {
        super();
    }

    public synchronized void post(T value) {
        if (!Objects.equals(value, data)) {
            data = value;
            super.postValue(value);
        }
    }

    public synchronized void set(T value) {
        if (!Objects.equals(value, data)) {
            data = value;
            super.setValue(value);
        }
    }

    @Override
    public T getValue() {
        return data;
    }

    public void notifyChange() {
        super.postValue(data);
    }
}