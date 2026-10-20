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
}