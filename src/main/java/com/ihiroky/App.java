package com.ihiroky;

import java.lang.reflect.Constructor;

public class App {

    private void run(String[] args) throws Exception {
        final int NUM_THREADS = 4;
        final long ITERATION = 500L * 1000 * 1000;
        Constructor<?> c = Class.forName(args[0]).getDeclaredConstructor();

        var array = new VolatileLong[NUM_THREADS];
        for (var i = 0; i < array.length; i++) {
            array[i] = (VolatileLong) c.newInstance();
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
        System.out.println("duration: " + ((System.nanoTime() - start) / 100000) + "ms.");
    }

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.run(args);
    }


}
