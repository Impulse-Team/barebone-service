package es.fujitsu.barebone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.fujitsu.barebone.dto.BareboneDto;
import es.fujitsu.barebone.service.BareboneService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Reference api versioning:
 * https://dzone.com/articles/versioning-rest-api-with-spring-boot-and-swagger
 * 
 * @author
 *
 */
@RestController
@RequestMapping("/")
@Slf4j
public class BareboneController {

	@Autowired
	private BareboneService bareboneService;

	/**
	 * The endpoint 1.0 both in the api 1.0 and 1.1 versions
	 * 
	 * @return
	 */
	@GetMapping(value = "/dummy", produces = { "application/vnd.fujitsu.barebone-v1.0+json",
			"application/vnd.fujitsu.barebone-v1.1+json" })
	public ResponseEntity<String> dummyEndpoint() {
		log.debug("Executing dummyEndpoint");
		return ResponseEntity.ok(bareboneService.generateResponse());

	}

	/**
	 * The endpoint 1.1 will be available in the version of the api 1.1 and 1.2
	 * 
	 * @return
	 */
	@GetMapping(value = "/dummy11", produces = { "application/vnd.fujitsu.barebone-v1.1+json",
			"application/vnd.fujitsu.barebone-v1.2+json" })
	public ResponseEntity<String> dummyEndpoint11() {
		log.debug("Executing dummyEndpoint v1.1");
		return ResponseEntity.ok(bareboneService.generateResponse());
	}

	/**
	 * This endpoint solo will be available in the version of the api 1.2, and will
	 * replace the old one from the 1.0
	 * 
	 * @return
	 */
	@GetMapping(value = "/dummy", produces = { "application/vnd.fujitsu.barebone-v1.2+json" })
	public ResponseEntity<String> dummyEndpoint12() {
		log.debug("Executing dummyEndpoint v1.2");
		return ResponseEntity.ok(bareboneService.generateResponse());
	}

	/**
	 * Publish a test dto in the kafka topic configured for writting
	 * 
	 * @return
	 */
	@GetMapping(value = "/onDemandPublishToKafka", produces = { "application/vnd.fujitsu.barebone-v1.2+json" })
	public ResponseEntity<String> onDemandPublishToKafka() {
		log.debug("Executing onDemandPublishToKafka");
		BareboneDto barebone = new BareboneDto();
		barebone.setAddress("Calle street");
		barebone.setName("Juan");
		barebone.setPhone("555-41-34-67");
		barebone.setSurname("Nadie");
		bareboneService.onDemandPublishToKafka(barebone);
		return ResponseEntity.ok("Published to kafka");
	}

	@GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamFlux() {
		return Flux.interval(Duration.ofSeconds(1))
				.map(sequence -> "Flux - " + LocalTime.now().toString());
	}
	@GetMapping("/stream-sse")
	public Flux<ServerSentEvent<String>> streamEvents() {
		return Flux.interval(Duration.ofSeconds(1))
				.map(sequence -> ServerSentEvent.<String> builder()
						.id(String.valueOf(sequence))
						.event("periodic-event")
						.data("SSE - " + LocalTime.now().toString())
						.build());
	}
}
