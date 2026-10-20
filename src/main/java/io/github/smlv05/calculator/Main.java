package io.github.smlv05.calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ConsoleMenu menu = new ConsoleMenu(new Scanner(System.in), System.out);
        menu.run();
    }
}