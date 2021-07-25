package pl.salata.f1betapp.datapopulating;

import java.util.Optional;

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
}

