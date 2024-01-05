package com.lenzetech.blelib.ble;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveData<T> extends MutableLiveData<T> {
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (hasActiveObservers()) {
            // Remove any existing observers when new ones are added
            super.removeObservers(owner);
        }

        // Observe the LiveData with the new observer
        super.observe(owner, value -> {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(value);
            }
        });
    }

    @Override
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }

    @Override
    public void postValue(T value) {
        mPending.set(true);
        super.postValue(value);
    }

    public void call() {
        mPending.set(true);
        setValue(null);
    }
}
