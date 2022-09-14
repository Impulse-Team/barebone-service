package es.fujitsu.barebone.common;

public class Constants {

	private Constants() {
		throw new IllegalStateException("Utility class");
	}

	public static final String MESSAGE_RECEIVED_FROM_KAFKA = "message received from kafka";

	public static final String RECEIVED_EVENT_FROM_KAFKA = "Received event from kafka: {}";

	public static final String SENT_EVENT_TO_KAFKA_NEW = "Sent event to kafka: {}, topic new-commitment-date";

	public static final String SENT_EVENT_TO_KAFKA_MODIFY = "Sent event to kafka: {}, topic modify-commitment-date";
}
