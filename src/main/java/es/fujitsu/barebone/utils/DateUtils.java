package es.fujitsu.barebone.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	// Reference
	// https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
	// example yyyy-MM-dd'T'hh:mm:ss.SSSXXX
	public static final String DATE_FORMAT = "+yyyy-MM-dd'T'HH:mm:ss'Z'";

	private DateUtils() {
	}

	public static Date fromLocalDateToDate(LocalDateTime ldt) {
		// local date + atStartOfDay() + default time zone + toInstant() = Date
		return Date.from(ldt.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate fromDateToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static final OffsetDateTime fromStringToOffsetDateTime(String strDate) {
		return OffsetDateTime.parse(strDate);
	}

	public static final String fromOffsetDateTimeToString(OffsetDateTime offsetDateTime) {
		return offsetDateTime.toString();
	}
}
