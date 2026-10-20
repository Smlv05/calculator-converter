package io.github.smlv05.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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
}