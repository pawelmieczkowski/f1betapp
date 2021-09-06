package pl.salata.f1betapp.datapopulating;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class InputProcessor {
    public static <T extends Number> Optional<T> parseNumber(String value, Class<T> type) {
        if (value == null) {
            return Optional.empty();
        }

        if (value.matches("\\d+")) {
            if (type == Integer.class) {
                return Optional.of(type.cast(Integer.valueOf(value)));
            } else if (type == Long.class) {
                return Optional.of(type.cast(Long.valueOf(value)));
            }
        }
        if (value.matches("\\d*\\.?\\d+")) {
            if (type == Float.class) {
                return Optional.of(type.cast(Float.valueOf(value)));
            }
        }
        return Optional.empty();
    }

    //dataset has \N symbol to indicate there is no data, this validator is making sure not to write that symbol to db
    public static Optional<String> validateString(String value) {
        if (value == null) {
            return Optional.empty();
        }
        return value.matches("\\\\N") ? Optional.empty() : Optional.of(value);
    }

    public static Optional<LocalDate> parseDate(String date) {
        if (date == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalDate.parse(date, ISO_LOCAL_DATE));
        } catch (DateTimeParseException e) {
            System.out.println("Date parsing exception for value " + date + e);
        }
        return Optional.empty();
    }

    public static Optional<LocalTime> parseTime(String time) {
        if (time == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(LocalTime.parse(time));
        } catch (DateTimeParseException e) {
            System.out.println("Time parsing exception for value " + time + e);
        }
        return Optional.empty();
    }
}

