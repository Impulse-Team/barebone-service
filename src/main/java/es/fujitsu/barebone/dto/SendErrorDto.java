package es.fujitsu.barebone.dto;

import es.fujitsu.kafka.common.error.ErrorResponse;
import lombok.Data;

@Data
public class SendErrorDto {

	private ErrorResponse error;

	public SendErrorDto(ErrorResponse error) {
		super();
		this.error = error;
	}
}