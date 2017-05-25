package mediathek.tool;


import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Central collection class for used string formatters.
 * Since {@link FastDateFormat} is threadsafe we can use it this way.
 */
public final class FormatterUtil {
    public static final DateTimeFormatter FORMATTER_ddMMyyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter FORMATTER_yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter FORMATTER_HHmmss = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
    public static final DateTimeFormatter FORMATTER_ddMMyyyyHHmm = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
    public static final DateTimeFormatter FORMATTER_ddMMyyyyHHmmss = DateTimeFormatter.ofPattern("dd.MM.yyyyHH:mm:ss");
}
