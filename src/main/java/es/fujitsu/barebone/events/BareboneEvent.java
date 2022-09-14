package es.fujitsu.barebone.events;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class BareboneEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public BareboneEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
}
