package es.fujitsu.barebone.errorhandling;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import es.fujitsu.barebone.exception.DataCardinalityException;
import es.fujitsu.kafka.common.error.ErrorExtendedInfo;
import es.fujitsu.kafka.common.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class BareboneControllerAdvice {
	/**
	 * We are expecting to have an exception and handle it.
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ErrorResponse> handleRestException(Exception ex, WebRequest request) {
		log.error("Unexpected error", ex);
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
	}

	/**
	 * Handle NO-DATA-FOUND errors and TOO-MANY-DATA errors.
	 */
	@ExceptionHandler({ DataCardinalityException.class })
	public ResponseEntity<ErrorResponse> handleRestException(DataCardinalityException ex, WebRequest request) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, Exception ex) {
		return buildErrorResponse(status, ex.getMessage());
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String errorDescription) {
		return buildErrorResponse(status, errorDescription, null, null, null, null);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String errorDescription, String errorId,
			String errorAlias, String errorExtendedMessage, Map<String, String> errorMessages) {
		ErrorExtendedInfo errorExtendedInfo = null;
		if (!StringUtils.isAllBlank(errorId, errorAlias) || MapUtils.isNotEmpty(errorMessages)) {
			errorExtendedInfo = ErrorExtendedInfo.builder().errorNumber(status.value()).errorId(errorId)
					.errorAlias(errorAlias).errorMessage(errorExtendedMessage).errorMessages(errorMessages).build();
		}
		return buildErrorResponse(status, errorDescription, errorExtendedInfo);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String errorDescription,
			ErrorExtendedInfo errorExtendedInfo) {
		ErrorResponse errorResponse = ErrorResponse.builder().errorCode(status.value())
				.errorType(status.getReasonPhrase()).errorDateTime(new Date()).errorDescription(errorDescription)
				.errorExtendedInfo(errorExtendedInfo).build();

		return ResponseEntity.status(status).body(errorResponse);
	}

}
