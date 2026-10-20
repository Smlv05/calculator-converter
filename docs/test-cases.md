# Test cases

Here I list the main test cases and which technique I used to find them.

Techniques:
- EP - equivalence partitioning (one value from each group of inputs)
- BVA - boundary values (values on the edge and next to it)
- NEG - negative test (wrong input should give an error)

## Calculator

| ID | What I test | Input | Expected | Technique |
|---|---|---|---|---|
| C1 | add positive, negative, mixed numbers | 2 + 3, -2 + -3, -2 + 5 | 5, -5, 3 | EP |
| C2 | double precision | 0.1 + 0.2 | 0.3 (with delta) | EP |
| C3 | multiply by 0, 1, -1 | 5 * 0, 5 * 1, 5 * -1 | 0, 5, -5 | EP |
| C4 | zero divided by number | 0 / 5 | 0 | BVA |
| C5 | division by zero | 5 / 0, 0 / 0 | error | NEG |
| C6 | modulo with negative number | -7 % 3 | -1 | EP |
| C7 | modulo by zero | 10 % 0 | error | NEG |
| C8 | square root of 0 | sqrt(0) | 0 | BVA |
| C9 | square root of negative number | sqrt(-0.0001) | error | BVA, NEG |
| C10 | percent: 0%, 100%, more than 100% | 0%, 100%, 150% of 200 | 0, 200, 300 | BVA |
| C11 | overflow | Double.MAX_VALUE * 2 | error | BVA |

## Unit converter

| ID | What I test | Input | Expected | Technique |
|---|---|---|---|---|
| U1 | each length and weight unit | 1 km -> m, 1 lb -> kg | 1000, 0.45359237 | EP |
| U2 | negative length or weight | -1 m | error | NEG |
| U3 | unknown unit | mm, empty, M | error | NEG |
| U4 | known temperatures | 0 C -> F, 100 C -> F, -40 C -> F | 32, 212, -40 | EP |
| U5 | absolute zero | -273.15 C, 0 K | allowed | BVA |
| U6 | below absolute zero | -273.16 C, -0.0001 K | error | BVA |
| U7 | convert there and back | 5 km -> mi -> km | 5 | - |

## History

| ID | What I test | Input | Expected | Technique |
|---|---|---|---|---|
| H1 | limit of 10 records | add 9, 10, 11 records | size 9, 10, 10 | BVA |
| H2 | oldest record removed | add 11 records | first one is gone | BVA |
| H3 | empty record | null, "" | error | NEG |
| H4 | clear | add, clear | empty | EP |

## Console menu

| ID | What I test | Input | Expected | Technique |
|---|---|---|---|---|
| M1 | letters or empty input instead of number | abc, "" | message, ask again | NEG |
| M2 | comma in number | 3,5 | 3.5 | EP |
| M3 | rounding to 4 decimals | 1 / 3 | 0.3333 | EP |
| M4 | error does not stop the program | 5 / 0 | message, back to menu | NEG |

## Bugs

- [BUG-001](bugs/BUG-001-overflow-returns-infinity.md) - overflow returns Infinity (fixed)
- [BUG-002](bugs/BUG-002-thousands-separator.md) - "1,000" is read as 1 (open)