# Calculator & Converter

![Build](https://github.com/Smlv05/calculator-converter/actions/workflows/build.yml/badge.svg)

A console calculator and unit converter in Java. I made it as a study project
to practice unit testing with JUnit 5.

## What it does

- Calculator: add, subtract, multiply, divide, modulo, power, square root, percent
- Unit converter:
    - length: m, km, cm, mi
    - weight: kg, g, lb
    - temperature: C, F, K
- History of the last 10 operations (show and clear)

Wrong input (letters, empty input, unknown unit) shows a message and asks again.
Errors like division by zero or temperature below absolute zero show a message,
the program does not crash. Results are rounded to 4 decimal places.

## Requirements

- Java 17 or newer
- Maven

## How to run

    mvn compile
    java -cp target/classes io.github.smlv05.calculator.Main

## How to run tests

    mvn test

Tests are in `src/test/java`. I used parameterized tests (`@CsvSource`,
`@ValueSource`, `@MethodSource`), `@Nested` groups and `assertThrows`
for error cases.

## Code coverage

    mvn verify

This runs the tests and checks that `Calculator` and `UnitConverter` have
at least 80% line and branch coverage (JaCoCo). The report is in
`target/site/jacoco/index.html`.

## CI

GitHub Actions runs `mvn verify` on every push. I use `verify` instead of
`test` so the coverage check also runs in CI.

## Docs

- [Test cases](docs/test-cases.md) - main test cases and test design techniques
- [Bugs](docs/bugs) - bugs I found while testing

## Tech

Java 17, Maven, JUnit 5, JaCoCo, GitHub Actions