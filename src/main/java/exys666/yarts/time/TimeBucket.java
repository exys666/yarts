package exys666.yarts.time;

import exys666.yarts.model.RawStatistics;

import java.util.concurrent.atomic.AtomicReference;

public class TimeBucket {

    private final AtomicReference<RawStatistics> stats = new AtomicReference<>(RawStatistics.ZERO);
    private final long second;

    public TimeBucket(long second) {
        this.second = second;
    }

    public void add(double value) {
        stats.accumulateAndGet(new RawStatistics(value), RawStatistics::merge);
    }

    public long getSecond() {
        return second;
    }

    public RawStatistics getStatistics() {
        return stats.get();
    }
}
