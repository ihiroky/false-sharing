package com.ihiroky;

public class FalseSharing implements Runnable {
    private VolatileLong[] array_;
    private final int arrayIndex_;
    private final long iteration_;

    public FalseSharing(VolatileLong[] array, int arrayIndex, long iteration) {
        array_ = array;
        arrayIndex_ = arrayIndex;
        iteration_ = iteration;
    }

    @Override
    public void run() {
        for (long i = 0; i < iteration_; i++) {
            array_[arrayIndex_].value = i;
        }
    }
}
