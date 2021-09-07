package pl.salata.f1betapp.datapopulating.batch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputProcessorTest {

    @ParameterizedTest
    @MethodSource("provideCorrectNumberParameters")
    <T extends Number> void shouldReturnOptionalWithNumericValue(String s, Class<T> type, T expected) {
        //when
        Object result = InputProcessor.parseNumber(s, type);
        //then
        assertEquals(Optional.of(expected), result);
    }

    private static Stream<Arguments> provideCorrectNumberParameters() {
        return Stream.of(
                Arguments.of("0", Integer.class, 0),
                Arguments.of("15", Integer.class, 15),

                Arguments.of("0", Long.class, 0L),
                Arguments.of("25", Long.class, 25L),
                Arguments.of("21474836470", Long.class, 21474836470L),

                Arguments.of("0", Float.class, 0f),
                Arguments.of("35", Float.class, 35f),
                Arguments.of("4.5", Float.class, 4.5f),
                Arguments.of("0.1", Float.class, 0.1f),
                Arguments.of("00.155", Float.class, 00.155f),
                Arguments.of("100.124", Float.class, 100.124f),
                Arguments.of(".6", Float.class, 0.6f)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongNumberParameters")
    <T extends Number> void shouldReturnEmptyOptionalWithNumericValue(String s, Class<T> type) {
        //when
        Object result = InputProcessor.parseNumber(s, type);

        //then
        assertEquals(Optional.empty(), result);
    }

    private static Stream<Arguments> provideWrongNumberParameters() {
        return Stream.of(
                Arguments.of(null, Integer.class),
                Arguments.of("", Integer.class),
                Arguments.of(" ", Integer.class),
                Arguments.of("a", Integer.class),
                Arguments.of("52a", Integer.class),
                Arguments.of("52,a", Integer.class),
                Arguments.of("0.5", Integer.class),

                Arguments.of("dd", Long.class),
                Arguments.of("25d", Long.class),
                Arguments.of("25.d", Long.class),
                Arguments.of("0.2", Long.class),

                Arguments.of("ab", Float.class),
                Arguments.of(".ad", Float.class),
                Arguments.of("4.ad", Float.class),
                Arguments.of("asd.4", Float.class),
                Arguments.of("", Float.class),
                Arguments.of(" ", Float.class),
                Arguments.of("4,5", Float.class),
                Arguments.of("97.", Float.class)
        );
    }

    @ParameterizedTest
    @MethodSource("provideWrongStringParameters")
    void shouldReturnEmptyOptionalForValidateString(String s) {
        //when
        Optional<String> result = InputProcessor.validateString(s);

        //then
        assertEquals(Optional.empty(), result);
    }

    private static Stream<Arguments> provideWrongStringParameters() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of("\\N")
        );
    }

    @ParameterizedTest
    @MethodSource("provideCorrectStringParameters")
    void shouldReturnOptionalWithString(String s) {
        //when
        Optional<String> result = InputProcessor.validateString(s);
        //then
        assertEquals(Optional.of(s), result);
    }

    private static Stream<Arguments> provideCorrectStringParameters() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("asd"),
                Arguments.of("123"),
                Arguments.of("12.3"),
                Arguments.of("asd dd a sd")
        );
    }

    @Test
    void shouldReturnOptionalWithLocalDate() {
        //given
        String input = "2011-12-03";
        //when
        Optional<LocalDate> result = InputProcessor.parseDate(input);
        //then
        assertEquals(Optional.of(LocalDate.of(2011, 12, 3)), result);
    }

    @ParameterizedTest
    @MethodSource("provideWrongDateParameters")
    void shouldReturnEmptyOptionalForParseDate(String s) {
        //when
        Optional<LocalDate> result = InputProcessor.parseDate(s);
        //then
        assertEquals(Optional.empty(), result);
    }

    private static Stream<Arguments> provideWrongDateParameters() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of("x"),
                Arguments.of("xxxx-xx-xx")
        );
    }

    @Test
    void shouldReturnOptionalWithLocalTime() {
        //given
        String input = "06:30:05";
        //when
        Optional<LocalTime> result = InputProcessor.parseTime(input);
        //then
        assertEquals(Optional.of(LocalTime.of(6, 30, 5)), result);
    }

    @ParameterizedTest
    @MethodSource("provideWrongTimeParameters")
    void shouldReturnEmptyOptionalForParseTime(String s) {
        //when
        Optional<LocalTime> result = InputProcessor.parseTime(s);
        //then
        assertEquals(Optional.empty(), result);
    }

    private static Stream<Arguments> provideWrongTimeParameters() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of("x"),
                Arguments.of("xx:xx:xx")
        );
    }
}