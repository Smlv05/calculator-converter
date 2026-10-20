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

@DisplayName("Calculator")
class CalculatorTest {

    private static final double DELTA = 1e-9;

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Nested
    @DisplayName("Addition")
    class Addition {

        @ParameterizedTest(name = "{0} + {1} = {2}")
        @CsvSource({
                "2, 3, 5",
                "-2, -3, -5",
                "-2, 5, 3",
                "1.5, 2.25, 3.75",
                "5, 0, 5",
                "0, 0, 0"
        })
        void addsTwoNumbers(double a, double b, double expected) {
            assertEquals(expected, calculator.add(a, b), DELTA);
        }

        @Test
        @DisplayName("0.1 + 0.2 equals 0.3 with delta")
        void addsDecimalsWithPrecisionError() {
            assertEquals(0.3, calculator.add(0.1, 0.2), DELTA);
        }
    }

    @Nested
    @DisplayName("Subtraction")
    class Subtraction {

        @ParameterizedTest(name = "{0} - {1} = {2}")
        @CsvSource({
                "5, 3, 2",
                "3, 5, -2",
                "-2, -3, 1",
                "-2, 3, -5",
                "5, 0, 5",
                "0, 5, -5",
                "5, 5, 0"
        })
        void subtractsTwoNumbers(double a, double b, double expected) {
            assertEquals(expected, calculator.subtract(a, b), DELTA);
        }
    }

    @Nested
    @DisplayName("Multiplication")
    class Multiplication {

        @ParameterizedTest(name = "{0} * {1} = {2}")
        @CsvSource({
                "2, 3, 6",
                "-2, -3, 6",
                "-2, 3, -6",
                "1.5, 2, 3",
                "5, 0, 0",
                "5, 1, 5",
                "5, -1, -5"
        })
        void multipliesTwoNumbers(double a, double b, double expected) {
            assertEquals(expected, calculator.multiply(a, b), DELTA);
        }
    }

    @Nested
    @DisplayName("Division")
    class Division {

        @ParameterizedTest(name = "{0} / {1} = {2}")
        @CsvSource({
                "10, 2, 5",
                "10, 4, 2.5",
                "1, 3, 0.3333333333333333",
                "-10, -2, 5",
                "-10, 2, -5",
                "10, -2, -5"
        })
        void dividesTwoNumbers(double a, double b, double expected) {
            assertEquals(expected, calculator.divide(a, b), DELTA);
        }

        @Test
        @DisplayName("0 / 5 = 0")
        void zeroDividedByNumberIsZero() {
            assertEquals(0, calculator.divide(0, 5), DELTA);
        }

        @ParameterizedTest(name = "{0} / 0 throws exception")
        @ValueSource(doubles = {5, -5, 0, 1.5})
        void throwsOnDivisionByZero(double a) {
            CalculationException e = assertThrows(
                    CalculationException.class,
                    () -> calculator.divide(a, 0)
            );
            assertEquals("Cannot divide by zero", e.getMessage());
        }

        @Test
        @DisplayName("division by -0.0 throws exception")
        void throwsOnDivisionByNegativeZero() {
            assertThrows(CalculationException.class, () -> calculator.divide(5, -0.0));
        }
    }

    @Nested
    @DisplayName("Modulo")
    class Modulo {

        @ParameterizedTest(name = "{0} % {1} = {2}")
        @CsvSource({
                "10, 3, 1",
                "10, 2, 0",
                "3, 10, 3",
                "-7, 3, -1",
                "7, -3, 1",
                "5.5, 2, 1.5"
        })
        void returnsRemainder(double a, double b, double expected) {
            assertEquals(expected, calculator.modulo(a, b), DELTA);
        }

        @ParameterizedTest(name = "{0} % 0 throws exception")
        @ValueSource(doubles = {10, -10, 0})
        void throwsOnModuloByZero(double a) {
            CalculationException e = assertThrows(
                    CalculationException.class,
                    () -> calculator.modulo(a, 0)
            );
            assertEquals("Cannot calculate modulo by zero", e.getMessage());
        }
    }
}