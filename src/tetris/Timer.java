package tetris;

public class Timer {
    private long firstTime;

    public Timer() {
        this.firstTime = System.nanoTime();
    }

    public long getTimeElapsed() {
        long currTime = System.nanoTime();
        long timeElapsed = currTime - firstTime;
        this.firstTime = currTime;
        return timeElapsed;
    }

    public void reset() {
        this.firstTime = System.nanoTime();
    }
}
