package es.fujitsu.barebone.service;

import es.fujitsu.barebone.dto.BareboneDto;
import es.fujitsu.barebone.events.BareboneEvent;

public interface BareboneService {
	String generateResponse();

	void processingFromEvent(BareboneEvent bareboneEvent);

	void onDemandPublishToKafka(BareboneDto bareboneDto);
}
