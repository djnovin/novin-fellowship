package edu.monash;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public String getDirection() {
        if (!scanner.hasNextLine()) {
            throw new NoSuchElementException("No input available for direction.");
        }
        return scanner.nextLine().toLowerCase();
    }
}
