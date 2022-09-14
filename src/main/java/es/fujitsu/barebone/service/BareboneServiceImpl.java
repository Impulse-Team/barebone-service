package es.fujitsu.barebone.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import es.fujitsu.barebone.dto.BareboneDto;
import es.fujitsu.barebone.events.BareboneEvent;
import es.fujitsu.kafka.common.events.KafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BareboneServiceImpl implements BareboneService {

	@Value(value = "${kafka.topic.barebone-writting:kafka-is-disabled}")
	private String topicName;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public String generateResponse() {
		String response = "Hello From Dummy Endpoint";
		log.debug("Response: " + response);
		return response;
	}

	/**
	 * Produces (write) an event in Kafka based of the consumed event received as a
	 * parameter when invoked from the listener
	 */
	@Override
	public void processingFromEvent(BareboneEvent bareboneEvent) {

		BareboneDto bareboneDto = mapper.map(bareboneEvent.getSource(), BareboneDto.class);

		KafkaEvent<BareboneDto> event = new KafkaEvent<>(bareboneDto, "Publishing to Kafka");
		applicationEventPublisher.publishEvent(event);
	}

	@Override
	public void onDemandPublishToKafka(BareboneDto bareboneDto) {
		KafkaEvent<BareboneDto> event = new KafkaEvent<>(bareboneDto, "Publishing to Kafka on demand");
		applicationEventPublisher.publishEvent(event);
	}
}