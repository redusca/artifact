package resource.artifact.utils;

import java.time.format.DateTimeFormatter;

public class DateTimeFormat
{
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter DATE_TIME_FORMATTER_SECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
