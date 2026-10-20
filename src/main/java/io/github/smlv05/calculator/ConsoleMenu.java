package io.github.smlv05.calculator;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private static final List<String> LENGTH_UNITS = List.of("m", "km", "cm", "mi");
    private static final List<String> WEIGHT_UNITS = List.of("kg", "g", "lb");
    private static final List<String> TEMPERATURE_UNITS = List.of("c", "f", "k");

    private final Scanner scanner;
    private final PrintStream out;
    private final Calculator calculator = new Calculator();
    private final UnitConverter converter = new UnitConverter();
    private final History history = new History();

    public ConsoleMenu(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        this.out = out;
    }

    public void run() {
        while (true) {
            out.println();
            out.println("=== Calculator & Converter ===");
            out.println("1. Calculator");
            out.println("2. Unit converter");
            out.println("3. History");
            out.println("0. Exit");
            out.print("Choose option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> calculatorMenu();
                case "2" -> converterMenu();
                case "3" -> historyMenu();
                case "0" -> {
                    out.println("Bye!");
                    return;
                }
                default -> out.println("Unknown option, try again");
            }
        }
    }

    private void calculatorMenu() {
        out.println("1. a + b   2. a - b   3. a * b   4. a / b");
        out.println("5. a % b   6. a ^ b   7. sqrt(a) 8. b% of a");
        String choice = readChoice("Choose operation: ", List.of("1", "2", "3", "4", "5", "6", "7", "8"));

        try {
            if (choice.equals("7")) {
                double a = readNumber("a = ");
                save("sqrt(" + format(a) + ") = " + format(calculator.sqrt(a)));
                return;
            }
            double a = readNumber("a = ");
            double b = readNumber("b = ");
            String left = format(a);
            String right = format(b);
            switch (choice) {
                case "1" -> save(left + " + " + right + " = " + format(calculator.add(a, b)));
                case "2" -> save(left + " - " + right + " = " + format(calculator.subtract(a, b)));
                case "3" -> save(left + " * " + right + " = " + format(calculator.multiply(a, b)));
                case "4" -> save(left + " / " + right + " = " + format(calculator.divide(a, b)));
                case "5" -> save(left + " % " + right + " = " + format(calculator.modulo(a, b)));
                case "6" -> save(left + " ^ " + right + " = " + format(calculator.power(a, b)));
                default -> save(right + "% of " + left + " = " + format(calculator.percent(a, b)));
            }
        } catch (CalculationException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void converterMenu() {
        out.println("1. Length (m, km, cm, mi)");
        out.println("2. Weight (kg, g, lb)");
        out.println("3. Temperature (c, f, k)");
        String choice = readChoice("Choose category: ", List.of("1", "2", "3"));

        List<String> units = switch (choice) {
            case "1" -> LENGTH_UNITS;
            case "2" -> WEIGHT_UNITS;
            default -> TEMPERATURE_UNITS;
        };
        double value = readNumber("Value: ");
        String from = readChoice("From unit: ", units);
        String to = readChoice("To unit: ", units);

        try {
            double result = switch (choice) {
                case "1" -> converter.convertLength(value, from, to);
                case "2" -> converter.convertWeight(value, from, to);
                default -> converter.convertTemperature(value, from, to);
            };
            save(format(value) + " " + from + " = " + format(result) + " " + to);
        } catch (CalculationException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void historyMenu() {
        out.println("1. Show history   2. Clear history");
        String choice = readChoice("Choose option: ", List.of("1", "2"));

        if (choice.equals("2")) {
            history.clear();
            out.println("History cleared");
            return;
        }
        if (history.isEmpty()) {
            out.println("History is empty");
            return;
        }
        for (String record : history.getAll()) {
            out.println(record);
        }
    }

    private void save(String record) {
        history.add(record);
        out.println("Result: " + record);
    }

    private String readChoice(String prompt, List<String> options) {
        while (true) {
            out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (options.contains(input)) {
                return input;
            }
            out.println("Invalid choice, options: " + String.join(", ", options));
        }
    }

    private double readNumber(String prompt) {
        while (true) {
            out.print(prompt);
            try {
                return parseNumber(scanner.nextLine());
            } catch (CalculationException e) {
                out.println(e.getMessage());
            }
        }
    }

    static double parseNumber(String text) {
        String cleaned = text.trim().replace(',', '.');
        if (cleaned.isEmpty()) {
            throw new CalculationException("Input is empty, please enter a number");
        }
        double value;
        try {
            value = Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            throw new CalculationException("Not a number: " + text.trim());
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new CalculationException("Not a number: " + text.trim());
        }
        return value;
    }

    static String format(double value) {
        return BigDecimal.valueOf(value)
                .setScale(4, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }
}