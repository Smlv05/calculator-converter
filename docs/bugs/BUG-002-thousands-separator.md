# BUG-002: "1,000" is read as 1

Status: Open
Severity: Minor
Priority: Low
Environment: Windows 11, Java 17

## Steps

1. Run the program
2. Choose 1 (Calculator), then 3 (a * b)
3. Enter a = 1,000
4. Enter b = 2

## Expected

Result 2000, or a message that the input is not clear.

## Actual

    Result: 1 * 2 = 2

## Note

The program treats a comma as a decimal point, so "3,5" works as 3.5.
But because of that "1,000" becomes 1. Not fixed for now.