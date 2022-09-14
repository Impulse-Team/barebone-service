package es.fujitsu.barebone.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import es.fujitsu.barebone.events.BareboneEvent;
import es.fujitsu.barebone.service.BareboneService;

/**
 * Captures the application events launched from the kafka consumer
 * (KafkaListeners)
 * 
 * @author
 *
 */
@Component
public class BareboneListener implements ApplicationListener<BareboneEvent> {

	@Autowired
	BareboneService bareboneService;

	@Override
	public void onApplicationEvent(BareboneEvent event) {
		bareboneService.processingFromEvent(event);
	}
}
