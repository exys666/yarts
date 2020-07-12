package exys666.yarts.service;

import exys666.yarts.model.RawStatistics;
import exys666.yarts.model.Statistics;
import exys666.yarts.model.Tick;
import exys666.yarts.time.TimeFrame;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatisticsService {

    private final TimeFrame globalFrame = new TimeFrame();
    private final ConcurrentHashMap<String, TimeFrame> frames = new ConcurrentHashMap<>();

    public boolean add(Tick tick) {
        var timestamp = Instant.ofEpochMilli(tick.timestamp());
        var result = globalFrame.add(tick.price(), timestamp);
        frames.computeIfAbsent(tick.instrument(), (i) -> new TimeFrame()).add(tick.price(), timestamp);
        return result;
    }

    public Statistics getGlobalStats() {
        return globalFrame.getStatistics().toStatistics();
    }

    public Statistics getInstrumentStats(String instrument) {
        return Optional.ofNullable(frames.get(instrument))
                .map(TimeFrame::getStatistics)
                .map(RawStatistics::toStatistics)
                .orElse(new Statistics(0.0d, 0.0d, 0.0d, 0));
    }
}
