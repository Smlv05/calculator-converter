package io.github.smlv05.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("UnitConverter")
class UnitConverterTest {

    private static final double DELTA = 1e-9;

    private UnitConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UnitConverter();
    }

    @Nested
    @DisplayName("Length")
    class Length {

        @ParameterizedTest(name = "{0} {1} = {3} {2}")
        @CsvSource({
                "1, km, m, 1000",
                "1, m, cm, 100",
                "250, cm, m, 2.5",
                "1, mi, m, 1609.344",
                "1609.344, m, mi, 1",
                "1, mi, km, 1.609344",
                "100, cm, km, 0.001",
                "1, mi, cm, 160934.4",
                "3.5, m, m, 3.5",
                "0, km, m, 0"
        })
        void convertsLength(double value, String from, String to, double expected) {
            assertEquals(expected, converter.convertLength(value, from, to), DELTA);
        }

        @ParameterizedTest(name = "{0} m throws exception")
        @ValueSource(doubles = {-0.0001, -1, -1000})
        void throwsOnNegativeLength(double value) {
            CalculationException e = assertThrows(
                    CalculationException.class,
                    () -> converter.convertLength(value, "m", "km")
            );
            assertEquals("Length cannot be negative", e.getMessage());
        }

        @ParameterizedTest(name = "unit \"{0}\" throws exception")
        @ValueSource(strings = {"mm", "", "M", "meters", "kg"})
        void throwsOnUnknownUnit(String unit) {
            assertThrows(CalculationException.class,
                    () -> converter.convertLength(1, unit, "m"));
            assertThrows(CalculationException.class,
                    () -> converter.convertLength(1, "m", unit));
        }

        @Test
        @DisplayName("MAX_VALUE km to cm throws exception")
        void throwsOnOverflow() {
            assertThrows(CalculationException.class,
                    () -> converter.convertLength(Double.MAX_VALUE, "km", "cm"));
        }
    }

    @Nested
    @DisplayName("Weight")
    class Weight {

        @ParameterizedTest(name = "{0} {1} = {3} {2}")
        @CsvSource({
                "1, kg, g, 1000",
                "500, g, kg, 0.5",
                "1, lb, kg, 0.45359237",
                "1, lb, g, 453.59237",
                "0.45359237, kg, lb, 1",
                "2, kg, kg, 2",
                "0, g, lb, 0"
        })
        void convertsWeight(double value, String from, String to, double expected) {
            assertEquals(expected, converter.convertWeight(value, from, to), DELTA);
        }

        @ParameterizedTest(name = "{0} kg throws exception")
        @ValueSource(doubles = {-0.0001, -1, -1000})
        void throwsOnNegativeWeight(double value) {
            CalculationException e = assertThrows(
                    CalculationException.class,
                    () -> converter.convertWeight(value, "kg", "g")
            );
            assertEquals("Weight cannot be negative", e.getMessage());
        }

        @ParameterizedTest(name = "unit \"{0}\" throws exception")
        @ValueSource(strings = {"mg", "", "KG", "pounds", "m"})
        void throwsOnUnknownUnit(String unit) {
            assertThrows(CalculationException.class,
                    () -> converter.convertWeight(1, unit, "kg"));
            assertThrows(CalculationException.class,
                    () -> converter.convertWeight(1, "kg", unit));
        }
    }

    @Nested
    @DisplayName("Temperature")
    class Temperature {

        @ParameterizedTest(name = "{0} {1} = {3} {2}")
        @CsvSource({
                "0, c, f, 32",
                "100, c, f, 212",
                "-40, c, f, -40",
                "32, f, c, 0",
                "212, f, k, 373.15",
                "0, c, k, 273.15",
                "300, k, c, 26.85",
                "0, k, f, -459.67",
                "36.6, c, c, 36.6"
        })
        void convertsTemperature(double value, String from, String to, double expected) {
            assertEquals(expected, converter.convertTemperature(value, from, to), DELTA);
        }

        @ParameterizedTest(name = "absolute zero {0} {1} is allowed")
        @CsvSource({
                "-273.15, c",
                "-459.67, f",
                "0, k"
        })
        void allowsAbsoluteZero(double value, String unit) {
            assertEquals(0, converter.convertTemperature(value, unit, "k"), DELTA);
        }

        @ParameterizedTest(name = "{0} {1} throws exception")
        @CsvSource({
                "-273.16, c",
                "-459.68, f",
                "-0.0001, k",
                "-1000, c"
        })
        void throwsBelowAbsoluteZero(double value, String unit) {
            CalculationException e = assertThrows(
                    CalculationException.class,
                    () -> converter.convertTemperature(value, unit, "c")
            );
            assertEquals("Temperature cannot be below absolute zero", e.getMessage());
        }

        @ParameterizedTest(name = "unit \"{0}\" throws exception")
        @ValueSource(strings = {"C", "", "celsius", "r", "m"})
        void throwsOnUnknownUnit(String unit) {
            assertThrows(CalculationException.class,
                    () -> converter.convertTemperature(1, unit, "c"));
            assertThrows(CalculationException.class,
                    () -> converter.convertTemperature(1, "c", unit));
        }
    }

    @Nested
    @DisplayName("Round trip")
    class RoundTrip {

        static List<Arguments> lengthPairs() {
            return allPairs(new String[]{"m", "km", "cm", "mi"}, new double[]{0, 1, 2.5, 12345.678});
        }

        static List<Arguments> weightPairs() {
            return allPairs(new String[]{"kg", "g", "lb"}, new double[]{0, 1, 2.5, 12345.678});
        }

        static List<Arguments> temperaturePairs() {
            return allPairs(new String[]{"c", "f", "k"}, new double[]{0, 36.6, 100, 1000});
        }

        @ParameterizedTest(name = "{0} {1} -> {2} -> {1}")
        @MethodSource("lengthPairs")
        void lengthRoundTrip(double value, String from, String to) {
            double converted = converter.convertLength(value, from, to);
            assertEquals(value, converter.convertLength(converted, to, from), DELTA);
        }

        @ParameterizedTest(name = "{0} {1} -> {2} -> {1}")
        @MethodSource("weightPairs")
        void weightRoundTrip(double value, String from, String to) {
            double converted = converter.convertWeight(value, from, to);
            assertEquals(value, converter.convertWeight(converted, to, from), DELTA);
        }

        @ParameterizedTest(name = "{0} {1} -> {2} -> {1}")
        @MethodSource("temperaturePairs")
        void temperatureRoundTrip(double value, String from, String to) {
            double converted = converter.convertTemperature(value, from, to);
            assertEquals(value, converter.convertTemperature(converted, to, from), DELTA);
        }
    }

    private static List<Arguments> allPairs(String[] units, double[] values) {
        List<Arguments> pairs = new ArrayList<>();
        for (double value : values) {
            for (String from : units) {
                for (String to : units) {
                    pairs.add(Arguments.of(value, from, to));
                }
            }
        }
        return pairs;
    }
}