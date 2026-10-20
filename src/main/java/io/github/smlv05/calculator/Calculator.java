package io.github.smlv05.calculator;

public class Calculator {

    public double add(double a, double b) {
        return checkResult(a + b);
    }

    public double subtract(double a, double b) {
        return checkResult(a - b);
    }

    public double multiply(double a, double b) {
        return checkResult(a * b);
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new CalculationException("Cannot divide by zero");
        }
        return checkResult(a / b);
    }

    public double modulo(double a, double b) {
        if (b == 0) {
            throw new CalculationException("Cannot calculate modulo by zero");
        }
        return a % b;
    }

    public double power(double base, double exponent) {
        return checkResult(Math.pow(base, exponent));
    }

    public double sqrt(double a) {
        if (a < 0) {
            throw new CalculationException("Cannot calculate square root of a negative number");
        }
        return Math.sqrt(a);
    }

    public double percent(double number, double percent) {
        // percent / 100 first, so number * percent does not overflow
        return checkResult(number * (percent / 100));
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