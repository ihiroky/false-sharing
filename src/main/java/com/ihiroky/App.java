package com.ihiroky;

import java.util.concurrent.atomic.AtomicLongArray;
import java.util.function.Supplier;

public class App {

    private void run(String name, Supplier<VolatileLong> supplier) throws Exception {
        final int NUM_THREADS = 3;
        final long ITERATION = 500_000_000;

        var array = new VolatileLong[NUM_THREADS];
        for (var i = 0; i < array.length; i++) {
            array[i] = supplier.get();
        }
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {                
            threads[i] = new Thread(new FalseSharing(array, i, ITERATION));
        }

        long start = System.nanoTime();
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.println(name + " - duration: " + ((System.nanoTime() - start) / 1000_000) + "ms.");
    }

    private void runArray(String name, int width) throws Exception {
        final int NUM_THREADS = 3;
        final long ITERATION = 500_000_000;

        var array = new AtomicLongArray(NUM_THREADS * width);
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharingArray(array, i * width, ITERATION));
        }

        long start = System.nanoTime();
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.println(name + " - duration: " + ((System.nanoTime() - start) / 1000_000) + "ms.");
    }

    public static void main(String[] args) throws Exception {
        App app = new App();

        System.out.println(" === Start warm up === ");
        app.run("without pad", VolatileLong::new);
        app.run("with pad", VolatileLongPad::new);
        app.runArray("array without pad", 1);
        app.runArray("array with pad", 8);
        System.out.println(" === End warm up === ");

        app.run("without pad", VolatileLong::new);
        app.run("with pad", VolatileLongPad::new);
        app.runArray("array without pad", 1);
        app.runArray("array with pad", 8);
    }
}
