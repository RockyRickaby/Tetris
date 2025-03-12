package com.mauro.tetris.tetris;

/**
 * The Timer class manages elapsed time since some moment in time.
 * The time is measured in Nanoseconds.
 */
public class Timer {
    private long firstTime;

    /**
     * Creates a new Timer with starting point
     * being the one returned by calling System.nanoTime().
     */
    public Timer() {
        this.firstTime = System.nanoTime();
    }

    /**
     * Returns the elapsed time since the last time this method
     * was called, since the creation of this Timer or since
     * it was reset, whichever happened
     * last.
     * @return the time elapsed in nanoseconds.
     */
    public long getTimeElapsed() {
        long currTime = System.nanoTime();
        long timeElapsed = currTime - firstTime;
        this.firstTime = currTime;
        return timeElapsed;
    }

    /**
     * Resets this Timer
     */
    public void reset() {
        this.firstTime = System.nanoTime();
    }
}
