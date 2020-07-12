package exys666.yarts.time;

import exys666.yarts.model.RawStatistics;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

class TimeBucketTest {

    @Test
    void shouldInitWith0() {
        // given

        // when
        var bucket = new TimeBucket(0);

        // then
        assertThat(bucket.getStatistics()).isEqualTo(RawStatistics.ZERO);
    }

    @Test
    void shouldAccumulate() {
        // given
        var bucket = new TimeBucket(0);

        // when
        bucket.add(0.1d);
        bucket.add(0.2d);
        bucket.add(0.3d);

        // then
        var stats = bucket.getStatistics();
        assertThat(stats.min()).isEqualTo(0.1d);
        assertThat(stats.max()).isEqualTo(0.3d);
        assertThat(stats.count()).isEqualTo(3);
        assertThat(stats.sum()).isEqualTo(0.1d + 0.2d + 0.3d, offset(0.00001));
    }
}
