package io.github.smlv05.calculator;

public class Calculator {

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new CalculationException("Cannot divide by zero");
        }
        return a / b;
    }

    public double modulo(double a, double b) {
        if (b == 0) {
            throw new CalculationException("Cannot calculate modulo by zero");
        }
        return a % b;
    }
}