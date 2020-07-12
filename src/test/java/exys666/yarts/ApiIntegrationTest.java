package exys666.yarts;

import exys666.yarts.model.Statistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class ApiIntegrationTest {

    @Autowired
    private WebTestClient http;

    @ParameterizedTest
    @ValueSource(strings = {
            "{}",
            """
                    {
                    	"instrument": "",
                    	"price": 1.23,
                    	"timestamp": 1478192204000
                    }
                    """,
            """
                    {
                    	"price": 1.23,
                    	"timestamp": 1478192204000
                    }
                    """,
            """
                    {
                    	"instrument": "abc",
                    	"timestamp": 1478192204000
                    }
                    """,
            """
                    {
                    	"instrument": "abc",
                    	"price": 1.23,
                    }
                    """
    })
    void shouldFailedToAddInvalidTick(String json) {
        http.post()
                .uri("/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(json), String.class))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldSkipOldTick() {
        http.post()
                .uri("/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just("""
                        					{
                        	"instrument": "abc",
                        	"price": 1.23,
                        	"timestamp": %d
                        }""".formatted(Instant.now().minus(60, ChronoUnit.SECONDS).toEpochMilli())), String.class))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldCalculateStats() {
        // given
        tick("A", 0.1d);
        tick("A", 0.2d);
        tick("A", 0.3d);
        tick("B", 1.0d);
        tick("B", 2.0d);
        tick("B", 3.0d);

        // when
        var stats = getGlobalStats();
        var aStats = getInstrumentStats("A");
        var bStats = getInstrumentStats("B");

        // then
        assertThat(stats.avg()).isEqualTo((0.1d + 0.2d + 0.3d + 1.0d + 2.0d + 3.0d) / 6);
        assertThat(stats.max()).isEqualTo(3.0d);
        assertThat(stats.min()).isEqualTo(0.1d);
        assertThat(stats.count()).isEqualTo(6);

        assertThat(aStats.avg()).isEqualTo((0.1d + 0.2d + 0.3d) / 3);
        assertThat(aStats.max()).isEqualTo(0.3d);
        assertThat(aStats.min()).isEqualTo(0.1d);
        assertThat(aStats.count()).isEqualTo(3);

        assertThat(bStats.avg()).isEqualTo((1.0d + 2.0d + 3.0d) / 3);
        assertThat(bStats.max()).isEqualTo(3.0d);
        assertThat(bStats.min()).isEqualTo(1.0d);
        assertThat(bStats.count()).isEqualTo(3);
    }

    private void tick(String instrument, double value) {
        http.post()
                .uri("/ticks")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just("""
                        					{
                        	"instrument": "%s",
                        	"price": %f,
                        	"timestamp": %d
                        }""".formatted(instrument, value, Instant.now().toEpochMilli())), String.class))
                .exchange()
                .expectStatus().isCreated();
    }

    private Statistics getGlobalStats() {
        return http.get()
                .uri("/statistics")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Statistics.class).getResponseBody().blockFirst();
    }

    private Statistics getInstrumentStats(String instrument) {
        return http.get()
                .uri("/statistics/" + instrument)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Statistics.class).getResponseBody().blockFirst();
    }

}
