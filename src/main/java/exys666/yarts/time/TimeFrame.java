package exys666.yarts.time;

import exys666.yarts.model.RawStatistics;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

public class TimeFrame {

    private static final int SIZE = 60;

    private final AtomicReferenceArray<TimeBucket> buckets = new AtomicReferenceArray<>(SIZE);

    private Clock clock = Clock.systemUTC();

    public boolean add(double value, Instant timestamp) {
        if (timestamp.isBefore(Instant.now(clock).minus(SIZE, ChronoUnit.SECONDS))) {
            return false;
        }
        getBucket(timestamp).add(value);
        return true;
    }

    private TimeBucket getBucket(Instant timestamp) {
        long second = timestamp.getLong(ChronoField.INSTANT_SECONDS);
        return buckets.updateAndGet(
                (int) (second % SIZE),
                b -> b == null || b.getSecond() < second ? new TimeBucket(second) : b);
    }

    public RawStatistics getStatistics() {
        var now = Instant.now(clock).minus(SIZE, ChronoUnit.SECONDS).getLong(ChronoField.INSTANT_SECONDS);
        return IntStream.range(0, SIZE)
                .mapToObj(buckets::get)
                .filter(b -> b != null && b.getSecond() >= now)
                .map(TimeBucket::getStatistics)
                .reduce(RawStatistics::merge)
                .orElse(RawStatistics.ZERO);
    }

    void setClock(Clock clock) {
        this.clock = requireNonNull(clock);
    }
}
