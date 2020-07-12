package exys666.yarts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationTests {

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
	void shouldAddTick() {
		http.post()
				.uri("/ticks")
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(Mono.just("""
      					{
							"instrument": "abc",
							"price": 1.23,
							"timestamp": 1478192204000
						}"""), String.class))
				.exchange()
				.expectStatus().isCreated();
	}

}
