package io.github.smlv05.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Calculator")
class CalculatorTest {
    private static final double DELTA = 1e-9;
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "2, 3, 5",
            "-2, -3, -5",
            "-2, 5, 3",
            "1.5, 2.25, 3.75",
            "5, 0, 5",
            "0, 0, 0"
    })
    @DisplayName("add returns sum of two numbers")
    void addsTwoNumbers(double a, double b, double expected) {
        assertEquals(expected, calculator.add(a, b), DELTA);
    }

    @Test
    @DisplayName("0.1 + 0.2 equals 0.3 with delta")
    void addsDecimalsWithPrecisionError() {
        assertEquals(0.3, calculator.add(0.1, 0.2), DELTA);
    }

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
    @DisplayName("subtract returns difference of two numbers")
    void subtractsTwoNumbers(double a, double b, double expected) {
        assertEquals(expected, calculator.subtract(a, b), DELTA);
    }

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
    @DisplayName("multiply returns product of two numbers")
    void multipliesTwoNumbers(double a, double b, double expected) {
        assertEquals(expected, calculator.multiply(a, b), DELTA);
    }
}