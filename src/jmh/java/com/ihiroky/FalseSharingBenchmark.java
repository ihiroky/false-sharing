package com.ihiroky;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class FalseSharingBenchmark {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void falseSharingNoPad() throws Exception {
        run(VolatileLong::new);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void falseSharingWithPad() throws Exception {
        run(VolatileLongPad::new);
    }

    void run(Supplier<VolatileLong> supplier) throws Exception {
        final var NUM_THREADS = 4;
        var array = new VolatileLong[NUM_THREADS];
        for (int i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
        var threads = new Thread[NUM_THREADS];
        for (var i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(array, i, 1000 * 1000));
        }
        for (var t : threads) {
            t.start();
        }
        for (var t : threads) {
            t.join();
        }
    }
}