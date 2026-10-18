
package org.example.utils;

import java.util.Scanner;

public class Utils {

    // ===== INPUT METHODS =====

    public static String askMenuOption(Scanner scan) {
        System.out.print("Select an option: ");
        String option = scan.nextLine();
        return option;
    }

    public static String askString(Scanner scan, String prompt) {
        System.out.print(prompt);
        return scan.nextLine();
    }

    public static int askInt(Scanner scan, String prompt) {
        System.out.print(prompt);

        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Please enter an integer.");
            return -1;
        }
    }

    // ===== MAIN MENU =====

    public static void mainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Person operations");
        System.out.println("2. Dog operations");
        System.out.println("3. Car operations");
        System.out.println("4. Buy a car (person to person)");
        System.out.println("5. View car transactions (read only)");
        System.out.println("6. Dog adoptions");
        System.out.println("7. Quit");
    }

    // ===== PERSON MENU =====

    public static void personMenu() {
        System.out.println("\n===== PERSON MENU =====");
        System.out.println("1. Create person");
        System.out.println("2. List all people");
        System.out.println("3. Find person by ID");
        System.out.println("4. Update person");
        System.out.println("5. Delete person");
        System.out.println("6. Back");
    }

    // ===== DOG MENU =====

    public static void dogMenu() {
        System.out.println("\n===== DOG MENU =====");
        System.out.println("1. Create dog");
        System.out.println("2. List all dogs");
        System.out.println("3. Find dog by ID");
        System.out.println("4. Update dog");
        System.out.println("5. Delete dog");
        System.out.println("6. Back");
    }

    // ===== CAR MENU =====

    public static void carMenu() {
        System.out.println("\n===== CAR MENU =====");
        System.out.println("1. Create car");
        System.out.println("2. List all cars");
        System.out.println("3. Find car by ID");
        System.out.println("4. Update car");
        System.out.println("5. Delete car");
        System.out.println("6. Back");
    }

    // ===== CAR TRANSACTION MENU =====

    public static void carTransactionMenu() {
        System.out.println("\n===== CAR TRANSACTIONS (READ ONLY) =====");
        System.out.println("1. List all transactions");
        System.out.println("2. Find transaction by ID");
        System.out.println("3. Back");
    }

    // ===== DOG ADOPTION MENU =====

    public static void dogAdoptionMenu() {
        System.out.println("\n===== DOG ADOPTIONS =====");
        System.out.println("1. List all adoptions");
        System.out.println("2. Find adoption by ID");
        System.out.println("3. Adopt a dog");
        System.out.println("4. View dogs by owner");
        System.out.println("5. View owners of a dog");
        System.out.println("6. Return a dog");
        System.out.println("7. Back");
    }
}
