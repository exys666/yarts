package exys666.yarts.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RawStatisticsTest {

    static Stream<Arguments> shouldMergeArgs() {
        return Stream.of(
                arguments(RawStatistics.ZERO, RawStatistics.ZERO, RawStatistics.ZERO),
                arguments(RawStatistics.ZERO, new RawStatistics(1.1d), new RawStatistics(1.1d)),
                arguments(new RawStatistics(1.1d), RawStatistics.ZERO, new RawStatistics(1.1d)),
                arguments(new RawStatistics(1.1d), new RawStatistics(2.2d), new RawStatistics(1.1d + 2.2d, 2.2d, 1.1d, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("shouldMergeArgs")
    void shouldMerge(RawStatistics a, RawStatistics b, RawStatistics expected) {
        // given

        // when
        var c = RawStatistics.merge(a, b);

        // then
        assertThat(c).isEqualTo(expected);
    }

    static Stream<Arguments> shouldConvertToStatisticsArgs() {
        return Stream.of(
                arguments(RawStatistics.ZERO, new Statistics(0.0d, 0.0d, 0.0d, 0)),
                arguments(new RawStatistics(1.1d), new Statistics(1.1d, 1.1d, 1.1d, 1)),
                arguments(new RawStatistics(1.1d, 1.0d, 0.1d, 2), new Statistics(1.1d / 2, 1.0d, 0.1d, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("shouldConvertToStatisticsArgs")
    void shouldConvertToStatistics(RawStatistics raw, Statistics expected) {
        // given

        // when
        var stats = raw.toStatistics();

        // then
        assertThat(stats).isEqualTo(expected);
    }
}
