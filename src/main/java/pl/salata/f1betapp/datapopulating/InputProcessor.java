package pl.salata.f1betapp.datapopulating;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class InputProcessor {
    public static <T extends Number> Optional<T> parseNumber(String value, Class<T> type) {

        if (value.matches("\\d+")) {
            if (type == Integer.class) {
                return Optional.of(type.cast(Integer.valueOf(value)));
            } else if (type == Long.class) {
                return Optional.of(type.cast(Long.valueOf(value)));
            }
        }
        if (value.matches("\\d+\\.?\\d*")) {
            if (type == Float.class) {
                return Optional.of(type.cast(Float.valueOf(value)));
            }
        }
        return Optional.empty();
    }

    public static String validateString(String value) {
        return value.matches("\\\\N") ? null : value;
    }

    public static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Date parsing exception for value " + date + e);
        }
        return null;
    }

    public static LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            System.out.println("Time parsing exception for value " + time + e);
        }
        return null;
    }
}

