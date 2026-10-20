package io.github.smlv05.calculator;

public class UnitConverter {

    public double convertLength(double value, String from, String to) {
        if (value < 0) {
            throw new CalculationException("Length cannot be negative");
        }
        double meters = value * lengthFactor(from);
        return checkResult(meters / lengthFactor(to));
    }

    public double convertWeight(double value, String from, String to) {
        if (value < 0) {
            throw new CalculationException("Weight cannot be negative");
        }
        double kilograms = value * weightFactor(from);
        return checkResult(kilograms / weightFactor(to));
    }

    private double lengthFactor(String unit) {
        return switch (unit) {
            case "m" -> 1;
            case "km" -> 1000;
            case "cm" -> 0.01;
            case "mi" -> 1609.344;
            default -> throw new CalculationException("Unknown length unit: " + unit);
        };
    }

    private double weightFactor(String unit) {
        return switch (unit) {
            case "kg" -> 1;
            case "g" -> 0.001;
            case "lb" -> 0.45359237;
            default -> throw new CalculationException("Unknown weight unit: " + unit);
        };
    }

    private double checkResult(double result) {
        if (Double.isNaN(result)) {
            throw new CalculationException("Result is undefined");
        }
        if (Double.isInfinite(result)) {
            throw new CalculationException("Result is out of range");
        }
        return result;
    }

    private static final double ABSOLUTE_ZERO_CELSIUS = -273.15;

    public double convertTemperature(double value, String from, String to) {
        double celsius = toCelsius(value, from);
        if (celsius < ABSOLUTE_ZERO_CELSIUS) {
            throw new CalculationException("Temperature cannot be below absolute zero");
        }
        return checkResult(fromCelsius(celsius, to));
    }

    private double toCelsius(double value, String unit) {
        return switch (unit) {
            case "c" -> value;
            case "f" -> (value - 32) * 5 / 9;
            case "k" -> value + ABSOLUTE_ZERO_CELSIUS;
            default -> throw new CalculationException("Unknown temperature unit: " + unit);
        };
    }

    private double fromCelsius(double celsius, String unit) {
        return switch (unit) {
            case "c" -> celsius;
            case "f" -> celsius * 9 / 5 + 32;
            case "k" -> celsius - ABSOLUTE_ZERO_CELSIUS;
            default -> throw new CalculationException("Unknown temperature unit: " + unit);
        };
    }
}