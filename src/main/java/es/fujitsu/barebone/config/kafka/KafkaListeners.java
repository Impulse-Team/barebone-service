package es.fujitsu.barebone.config.kafka;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import es.fujitsu.barebone.common.Constants;
import es.fujitsu.barebone.dto.BareboneDto;
import es.fujitsu.barebone.events.BareboneEvent;
import es.fujitsu.barebone.service.BareboneService;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka topic consumer
 * 
 * @author
 *
 */
@Configuration
@Slf4j
@ConditionalOnProperty(name = "kafka.enabled", matchIfMissing = false)
public class KafkaListeners {

	@Value(value = "${kafka.topic.barebone}")
	private String topicName;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private BareboneService bareboneService;

	@Autowired
	private ModelMapper mapper;

	/**
	 * Kafka "barebone" topic listener.
	 */
	@KafkaListener(id = "${spring.kafka.consumer.group.barebone}", topics = "${kafka.topic.barebone}")
	@ConditionalOnProperty(name = "kafka.enabled", matchIfMissing = false)
	public void bareboneTopicListener(@Payload BareboneDto bareboneDto, @Headers Map<String, Object> headers) {
		try {

			log.info(Constants.RECEIVED_EVENT_FROM_KAFKA, bareboneDto.toString());
			BareboneEvent event = new BareboneEvent(bareboneDto, Constants.RECEIVED_EVENT_FROM_KAFKA);

			applicationEventPublisher.publishEvent(event);

		} catch (Exception e) {
			log.error("%s Error consuming kafka topic %s");

			log.error(e.getMessage());
		}

	}

}
