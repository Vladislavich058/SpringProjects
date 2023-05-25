package restaurant.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public final class DateFormatter {

	private static DateFormatter instanceDateFormatter;

	private DateFormatter() {
	}

	public static DateFormatter getInstance() {
		if (instanceDateFormatter == null) {
			instanceDateFormatter = new DateFormatter();
		}
		return instanceDateFormatter;
	}

	public String format(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date.format(formatter);
	}
}
