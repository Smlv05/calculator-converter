# BUG-001: Overflow returns Infinity

Status: Fixed
Severity: Major
Priority: Medium
Environment: Windows 11, Java 17

## Steps

1. Call `calculator.multiply(Double.MAX_VALUE, 2)`

## Expected

CalculationException with an error message.

## Actual

The method returns `Infinity`, no error.

Test output:

    Expected CalculationException to be thrown, but nothing was thrown.

## Note

Java does not throw an error when a double is too big, it returns Infinity.
Same problem with add, subtract and divide.
Fixed by checking the result with `Double.isInfinite()` and `Double.isNaN()`.