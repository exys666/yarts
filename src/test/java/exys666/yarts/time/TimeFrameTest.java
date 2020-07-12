package exys666.yarts.time;

import exys666.yarts.model.RawStatistics;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

class TimeFrameTest {

    @Test
    void emptyShouldReturn0Sum() {
        // given
        var frame = new TimeFrame();

        // when

        // then
        assertThat(frame.getStatistics()).isEqualTo(RawStatistics.ZERO);
    }

    @Test
    void shouldSumUpBuckets() {
        // given
        var now = Instant.now();
        var frame = new TimeFrame();

        // when
        frame.add(0.1d, now.minus(30, ChronoUnit.SECONDS));
        frame.add(0.2d, now.minus(20, ChronoUnit.SECONDS));
        frame.add(0.3d, now.minus(10, ChronoUnit.SECONDS));

        // then
        var stats = frame.getStatistics();
        assertThat(stats.min()).isEqualTo(0.1d);
        assertThat(stats.max()).isEqualTo(0.3d);
        assertThat(stats.count()).isEqualTo(3);
        assertThat(stats.sum()).isEqualTo(0.1d + 0.2d + 0.3d, offset(0.00001));
    }

    @Test
    void shouldNotAddValueIfTooOld() {
        // given
        var now = Instant.now();
        var frame = new TimeFrame();

        // when
        var result = frame.add(0.1d, now.minus(61, ChronoUnit.SECONDS));

        // then
        assertThat(result).isFalse();
        assertThat(frame.getStatistics()).isEqualTo(RawStatistics.ZERO);
    }

    @Test
    void shouldOverrideBucket() {
        // given
        var now = Instant.now();
        var frame = new TimeFrame();

        // when
        frame.setClock(Clock.fixed(now.minus(60, ChronoUnit.SECONDS), ZoneOffset.UTC));
        var result1 = frame.add(0.1d, now.minus(61, ChronoUnit.SECONDS));

        // then
        assertThat(result1).isTrue();
        assertThat(frame.getStatistics()).isEqualTo(new RawStatistics(0.1d));

        // when
        frame.setClock(Clock.fixed(now, ZoneOffset.UTC));
        var result2 = frame.add(0.2d, now.minus(1, ChronoUnit.SECONDS));

        // then
        assertThat(result2).isTrue();
        assertThat(frame.getStatistics()).isEqualTo(new RawStatistics(0.2d));
    }
}
