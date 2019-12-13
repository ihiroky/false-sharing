package com.ihiroky;

import java.util.concurrent.atomic.AtomicLongArray;

public class FalseSharingArray implements Runnable {
    private AtomicLongArray array_;
    private final int arrayIndex_;
    private final long iteration_;

    public FalseSharingArray(AtomicLongArray array, int arrayIndex, long iteration) {
        array_ = array;
        arrayIndex_ = arrayIndex;
        iteration_ = iteration;
    }

    @Override
    public void run() {
        for (long i = 0; i < iteration_; i++) {
            array_.set(arrayIndex_, i);
        }
    }
}