package io.github.smlv05.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ConsoleMenu")
class ConsoleMenuTest {

    private String runWithInput(String... lines) {
        Scanner scanner = new Scanner(String.join("\n", lines));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        new ConsoleMenu(scanner, new PrintStream(output)).run();
        return output.toString();
    }

    private void assertContains(String output, String expected) {
        assertTrue(output.contains(expected),
                "Output does not contain: " + expected + "\nFull output:\n" + output);
    }

    @Nested
    @DisplayName("Parsing numbers")
    class ParseNumber {

        @ParameterizedTest(name = "\"{0}\" is parsed as {1}")
        @CsvSource({
                "'42', 42",
                "'-7.5', -7.5",
                "'  12  ', 12",
                "'3,5', 3.5",
                "'0', 0",
                "'1e3', 1000"
        })
        void parsesValidNumbers(String input, double expected) {
            assertEquals(expected, ConsoleMenu.parseNumber(input));
        }

        @ParameterizedTest(name = "\"{0}\" is rejected")
        @EmptySource
        @ValueSource(strings = {"   ", "abc", "5a", "1.2.3", "NaN", "Infinity", "-Infinity", "1e400"})
        void rejectsInvalidInput(String input) {
            assertThrows(CalculationException.class, () -> ConsoleMenu.parseNumber(input));
        }

        @ParameterizedTest(name = "\"{0}\" is parsed as {1}")
        @CsvSource({
                "'5d', 5",
                "'1,000', 1"
        })
        @DisplayName("documents surprising inputs")
        void documentsSurprisingInputs(String input, double expected) {
            assertEquals(expected, ConsoleMenu.parseNumber(input));
        }
    }

    @Nested
    @DisplayName("Formatting results")
    class Format {

        @ParameterizedTest(name = "{0} is shown as {1}")
        @CsvSource({
                "5, 5",
                "0.30000000000000004, 0.3",
                "0.333333333, 0.3333",
                "2.00005, 2.0001",
                "-2.00005, -2.0001",
                "1234.5, 1234.5",
                "-0.0, 0",
                "0.00004, 0",
                "1e20, 100000000000000000000"
        })
        void roundsToFourDecimals(double value, String expected) {
            assertEquals(expected, ConsoleMenu.format(value));
        }
    }

    @Nested
    @DisplayName("Menu")
    class Menu {

        @Test
        @DisplayName("exits on 0")
        void exitsOnZero() {
            assertContains(runWithInput("0"), "Bye!");
        }

        @Test
        @DisplayName("unknown option shows message")
        void unknownOption() {
            assertContains(runWithInput("9", "0"), "Unknown option, try again");
        }

        @Test
        @DisplayName("adds two numbers")
        void addsTwoNumbers() {
            assertContains(runWithInput("1", "1", "2", "3", "0"), "Result: 2 + 3 = 5");
        }

        @Test
        @DisplayName("result is rounded to 4 decimals")
        void roundsResult() {
            assertContains(runWithInput("1", "4", "1", "3", "0"), "1 / 3 = 0.3333");
        }

        @Test
        @DisplayName("division by zero shows error and program continues")
        void divisionByZeroDoesNotCrash() {
            String output = runWithInput("1", "4", "5", "0", "0");

            assertContains(output, "Error: Cannot divide by zero");
            assertContains(output, "Bye!");
        }

        @Test
        @DisplayName("invalid number asks again")
        void invalidNumberAsksAgain() {
            String output = runWithInput("1", "1", "abc", "", "2", "3", "0");

            assertContains(output, "Not a number: abc");
            assertContains(output, "Input is empty");
            assertContains(output, "Result: 2 + 3 = 5");
        }

        @Test
        @DisplayName("converts length with uppercase unit and spaces")
        void convertsWithMessyUnits() {
            assertContains(runWithInput("2", "1", "5", "KM", " m ", "0"), "Result: 5 km = 5000 m");
        }

        @Test
        @DisplayName("unknown unit asks again")
        void unknownUnitAsksAgain() {
            String output = runWithInput("2", "1", "5", "mm", "km", "m", "0");

            assertContains(output, "Invalid choice, options: m, km, cm, mi");
            assertContains(output, "Result: 5 km = 5000 m");
        }

        @Test
        @DisplayName("temperature below absolute zero shows error")
        void belowAbsoluteZero() {
            assertContains(runWithInput("2", "3", "-300", "c", "k", "0"),
                    "Error: Temperature cannot be below absolute zero");
        }

        @Test
        @DisplayName("history shows previous operation")
        void historyShowsOperation() {
            String output = runWithInput("1", "1", "2", "3", "3", "1", "0");

            int first = output.indexOf("2 + 3 = 5");
            int last = output.lastIndexOf("2 + 3 = 5");
            assertTrue(first != last, "Operation should appear as result and in history");
        }

        @Test
        @DisplayName("cleared history is empty")
        void clearedHistoryIsEmpty() {
            assertContains(runWithInput("1", "1", "2", "3", "3", "2", "3", "1", "0"), "History is empty");
        }
    }
}