package edu.monash.adapters.presenters;

public class ConsoleOutputAdapter {

    public void displayWelcomeMessage() {
        System.out.println("Welcome to Fellowship of Code: A Java Adventure in Middle Earth!");
        System.out.println("--------------------------------------------------------------");
        System.out.println("In this game, you lead a group of adventurers known as the Fellowship.");
        System.out.println("Your mission is to deliver a secret code to a Java wizard on Mount Api,");
        System.out.println("navigating through a labyrinth of caves and fighting off evil creatures.");
        System.out.println("Good luck on your quest!");
        System.out.println("--------------------------------------------------------------");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }
}
