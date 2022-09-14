package es.fujitsu.barebone.events.listeners;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.fujitsu.kafka.common.dto.KafkaMessage;
import es.fujitsu.kafka.common.events.KafkaProducerEventListener;

/**
 * Captures the application events launched from ApplicationListener
 * (BareboneListener), which in turn captures the events from the kafka consumer
 * (KafkaListeners).
 * 
 * In addition to being an ApplicationListener, he acts as a Kafka Producer when
 * it writes to a new topic.
 * 
 * @author
 *
 */
@Component
public class KafkaBareboneProducerEventListener<BareboneDto extends KafkaMessage>
		extends KafkaProducerEventListener<BareboneDto> {

	@Value(value = "${kafka.topic.barebone-writting:kafka-is-disabled}")
	String topicName;

	@Override
	public String getTopicName() {
		return topicName;
	}
}
