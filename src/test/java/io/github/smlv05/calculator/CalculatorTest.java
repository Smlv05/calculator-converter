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

    @Nested
    @DisplayName("Overflow")
    class Overflow {

        @Test
        @DisplayName("MAX_VALUE + MAX_VALUE throws exception")
        void addOverflow() {
            assertThrows(CalculationException.class,
                    () -> calculator.add(Double.MAX_VALUE, Double.MAX_VALUE));
        }

        @Test
        @DisplayName("-MAX_VALUE - MAX_VALUE throws exception")
        void subtractOverflow() {
            assertThrows(CalculationException.class,
                    () -> calculator.subtract(-Double.MAX_VALUE, Double.MAX_VALUE));
        }

        @Test
        @DisplayName("MAX_VALUE * 2 throws exception")
        void multiplyOverflow() {
            assertThrows(CalculationException.class,
                    () -> calculator.multiply(Double.MAX_VALUE, 2));
        }

        @Test
        @DisplayName("MAX_VALUE / 0.5 throws exception")
        void divideOverflow() {
            assertThrows(CalculationException.class,
                    () -> calculator.divide(Double.MAX_VALUE, 0.5));
        }

        @Test
        @DisplayName("MAX_VALUE + 1 stays MAX_VALUE because of precision loss")
        void addOneToMaxValue() {
            assertEquals(Double.MAX_VALUE, calculator.add(Double.MAX_VALUE, 1));
        }
    }

    @Nested
    @DisplayName("Power")
    class Power {

        @ParameterizedTest(name = "{0} ^ {1} = {2}")
        @CsvSource({
                "2, 10, 1024",
                "2, -1, 0.5",
                "5, 0, 1",
                "0, 0, 1",
                "-2, 3, -8",
                "-2, 2, 4",
                "4, 0.5, 2",
                "1.5, 2, 2.25"
        })
        void raisesToPower(double base, double exponent, double expected) {
            assertEquals(expected, calculator.power(base, exponent), DELTA);
        }

        @ParameterizedTest(name = "{0} ^ {1} throws exception")
        @CsvSource({
                "0, -1",
                "-8, 0.3333333333333333",
                "10, 400"
        })
        void throwsOnInvalidResult(double base, double exponent) {
            assertThrows(CalculationException.class,
                    () -> calculator.power(base, exponent));
        }
    }

    @Nested
    @DisplayName("Square root")
    class SquareRoot {

        @ParameterizedTest(name = "sqrt({0}) = {1}")
        @CsvSource({
                "4, 2",
                "0, 0",
                "0.25, 0.5",
                "2, 1.4142135623730951"
        })
        void calculatesSquareRoot(double a, double expected) {
            assertEquals(expected, calculator.sqrt(a), DELTA);
        }

        @ParameterizedTest(name = "sqrt({0}) throws exception")
        @ValueSource(doubles = {-0.0001, -1, -100})
        void throwsOnNegativeNumber(double a) {
            CalculationException e = assertThrows(
                    CalculationException.class,
                    () -> calculator.sqrt(a)
            );
            assertEquals("Cannot calculate square root of a negative number", e.getMessage());
        }
    }

    @Nested
    @DisplayName("Percent")
    class Percent {

        @ParameterizedTest(name = "{1}% of {0} = {2}")
        @CsvSource({
                "200, 10, 20",
                "200, 0, 0",
                "200, 100, 200",
                "200, 150, 300",
                "200, -10, -20",
                "0, 50, 0",
                "-200, 10, -20",
                "50, 12.5, 6.25"
        })
        void calculatesPercent(double number, double percent, double expected) {
            assertEquals(expected, calculator.percent(number, percent), DELTA);
        }

        @Test
        @DisplayName("50% of MAX_VALUE does not overflow")
        void percentOfMaxValue() {
            assertEquals(Double.MAX_VALUE / 2, calculator.percent(Double.MAX_VALUE, 50), DELTA);
        }
    }
}