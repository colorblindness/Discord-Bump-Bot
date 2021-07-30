package ohare.da.boss;

public class TimerUtil {
    private long time;

    public TimerUtil() {
        time = System.nanoTime() / 1000000L;
    }

    public void reset() {
        time = System.nanoTime() / 1000000L;
    }

    public boolean sleep(final long time) {
        if (time() >= time) {
            reset();
            return true;
        }
        return false;
    }

    public long time() {
        return System.nanoTime() / 1000000L - time;
    }
}