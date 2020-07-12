package exys666.yarts.model;

public record RawStatistics(
        double sum,
        double max,
        double min,
        long count
) {
    public static final RawStatistics ZERO = new RawStatistics(0.0d, 0.0d, 0.0d, 0);

    public static RawStatistics merge(RawStatistics a, RawStatistics b) {
        return new RawStatistics(
                a.sum() + b.sum(),
                Math.max(a.max, b.max),
                a.count() == 0 || b.count == 0 ? Math.max(a.min, b.min) : Math.min(a.min, b.min),
                a.count() + b.count());
    }

    public RawStatistics(double value) {
        this(value, value, value, 1);
    }

    public Statistics toStatistics() {
        return new Statistics(sum == 0.0d || count == 0 ? 0.0d : sum / count, max, min, count);
    }
}
