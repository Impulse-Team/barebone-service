package es.fujitsu.barebone.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "kafka.enabled", matchIfMissing = false)
public class KafkaTopics {

	@Value(value = "${kafka.topic.barebone}")
	private String topicName;
	@Value(value = "${kafka.numPartitions.barebone}")
	private int numPartitions;
	@Value(value = "${kafka.replicationFactor.barebone}")
	private int replicationFactor;

	@Bean
	@ConditionalOnProperty(name = "kafka.enabled", matchIfMissing = false)
	public NewTopic bareboneTopics() {
		return buildTopic(topicName, numPartitions, replicationFactor);
	}

	/**
	 * @param topicName
	 * @param numPartitions     - parallelism, ideally one partition per instance
	 * @param replicationFactor - fail tolerance / high availability
	 * @return
	 */
	private NewTopic buildTopic(final String topicName, final int numPartitions, final int replicationFactor) {
		log.debug("configuring topic '{}' in {} partitions with replication factor = {}", topicName, numPartitions,
				replicationFactor);
		return new NewTopic(topicName, numPartitions, (short) replicationFactor);
	}
}
