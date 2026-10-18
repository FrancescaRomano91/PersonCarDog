
package org.example.controller;

import org.example.model.Car;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.model.ResultOperation;
import org.example.repository.Repository;
import org.example.service.Service;
import org.example.utils.DataSeeder;
import org.example.utils.Utils;

import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    public static void run() {

        System.out.println("Hello to Transaction Cars Person to Person!");

        Repository repo = new Repository();

        DataSeeder.seedRepository(repo);

        Scanner scan = new Scanner(System.in);

        while (true) {
            Utils.mainMenu();

            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1":
                    personSubMenu(scan, repo);
                    break;

                case "2":
                    dogSubMenu(scan, repo);
                    break;

                case "3":
                    carSubMenu(scan, repo);
                    break;

                case "4":
                    runBuyCarDemo(scan, repo);
                    break;

                case "5":
                    carTransactionSubMenu(scan, repo);
                    break;

                case "6":
                    dogAdoptionSubMenu(scan, repo);
                    break;

                case "7":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // ===== PERSON SUBMENU =====

    private static void personSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.personMenu();
            String opt = Utils.askMenuOption(scan);

            switch (opt) {
                case "1":
                    String name = Utils.askString(scan, "Enter name: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    Service.createPerson(name, age, repo);
                    break;

                case "2":
                    Service.getAllPeople(repo);
                    break;

                case "3":
                    String pid = Utils.askString(scan, "Enter person ID: ");
                    Service.getPersonById(pid, repo);
                    break;

                case "4":
                    String uid = Utils.askString(
                            scan, "Enter person ID to update: ");
                    String newName = Utils.askString(
                            scan, "New name (leave blank to keep): ");
                    int newAge = Utils.askInt(
                            scan, "New age (-1 to keep): ");
                    Service.updatePerson(uid, newName, newAge, repo);
                    break;

                case "5":
                    String did = Utils.askString(
                            scan, "Enter person ID to delete: ");
                    Service.deletePerson(did, repo);
                    break;

                case "6":
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== DOG SUBMENU =====

    private static void dogSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.dogMenu();
            String opt = Utils.askMenuOption(scan);

            switch (opt) {
                case "1":
                    String name = Utils.askString(scan, "Enter dog name: ");
                    String breed = Utils.askString(scan, "Enter breed: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    Service.createDog(name, breed, age, repo);
                    break;

                case "2":
                    Service.getAllDogs(repo);
                    break;

                case "3":
                    String did = Utils.askString(scan, "Enter dog ID: ");
                    Service.getDogById(did, repo);
                    break;

                case "4":
                    String uid = Utils.askString(
                            scan, "Enter dog ID to update: ");
                    String newName = Utils.askString(
                            scan, "New name (blank=keep): ");
                    String newBreed = Utils.askString(
                            scan, "New breed (blank=keep): ");
                    int newAge = Utils.askInt(
                            scan, "New age (-1=keep): ");
                    Service.updateDog(uid, newName, newBreed, newAge, repo);
                    break;

                case "5":
                    String delId = Utils.askString(
                            scan, "Enter dog ID to delete: ");
                    Service.deleteDog(delId, repo);
                    break;

                case "6":
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== CAR SUBMENU =====

    private static void carSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carMenu();
            String opt = Utils.askMenuOption(scan);

            switch (opt) {
                case "1":
                    String make = Utils.askString(scan, "Enter make: ");
                    String model = Utils.askString(scan, "Enter model: ");
                    int year = Utils.askInt(scan, "Enter year: ");
                    Service.createCar(make, model, year, repo);
                    break;

                case "2":
                    Service.getAllCars(repo);
                    break;

                case "3":
                    String cid = Utils.askString(scan, "Enter car ID: ");
                    Service.getCarById(cid, repo);
                    break;

                case "4":
                    String uid = Utils.askString(
                            scan, "Enter car ID to update: ");
                    String newMake = Utils.askString(
                            scan, "New make (blank=keep): ");
                    String newModel = Utils.askString(
                            scan, "New model (blank=keep): ");
                    int newYear = Utils.askInt(
                            scan, "New year (-1=keep): ");
                    Service.updateCar(uid, newMake, newModel, newYear, repo);
                    break;

                case "5":
                    String delId = Utils.askString(
                            scan, "Enter car ID to delete: ");
                    Service.deleteCar(delId, repo);
                    break;

                case "6":
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== CAR TRANSACTION SUBMENU =====

    private static void carTransactionSubMenu(
            Scanner scan, Repository repo) {

        while (true) {
            Utils.carTransactionMenu();
            String opt = Utils.askMenuOption(scan);

            switch (opt) {
                case "1":
                    Service.getAllCarTransactions(repo);
                    break;

                case "2":
                    String tid = Utils.askString(
                            scan, "Enter transaction ID: ");
                    Service.getCarTransactionById(tid, repo);
                    break;

                case "3":
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== BUY CAR DEMO =====

    private static void runBuyCarDemo(Scanner scan, Repository repo) {
        System.out.println("Welcome to BUY A CAR (person to person)");

        ArrayList<Person> people = repo.getAllPeople();

        if (people.size() < 2) {
            System.out.println(
                    "Need at least two people. Creating demo people...");

            Person seller = Service.createPerson("DemoSeller", 30, repo);
            Service.createPerson("DemoBuyer", 25, repo);

            Car demoCar = Service.createCar(
                    "DemoMake", "DemoModel", 2020, repo);

            if (seller != null) {
                seller.setCar(demoCar);
            }

            people = repo.getAllPeople();
        }

        Person seller = people.get(0);
        Person buyer = people.size() > 1
                ? people.get(1)
                : people.get(0);

        if (seller.getCar() == null) {
            System.out.println(
                    "Seller has no car. Create one for seller first.");
            return;
        }

        if (buyer.getCar() != null) {
            System.out.println(
                    "Buyer already has a car. Adjust the data first.");
            return;
        }

        int price = Utils.askInt(scan, "Enter price: ");

        if (price < 0) {
            price = 0;
        }

        Service.buyCar(buyer, seller, price, repo);
    }

    // ===== DOG ADOPTION SUBMENU =====

    private static void dogAdoptionSubMenu(
            Scanner scan, Repository repo) {

        while (true) {
            Utils.dogAdoptionMenu();
            String opt = Utils.askMenuOption(scan);

            switch (opt) {
                case "1":
                    Service.getAllDogAdoptions(repo);
                    break;

                case "2":
                    String adoptionId = Utils.askString(
                            scan, "Enter adoption ID: ");
                    Service.getDogAdoptionById(adoptionId, repo);
                    break;

                case "3":
                    String personId = Utils.askString(
                            scan, "Enter adopter person ID: ");
                    String dogId = Utils.askString(
                            scan, "Enter dog ID: ");

                    Person adopter = repo.getPersonById(personId);
                    Dog dog = repo.getDogById(dogId);

                    if (adopter == null) {
                        System.out.println("Person not found.");
                        break;
                    }

                    if (dog == null) {
                        System.out.println("Dog not found.");
                        break;
                    }

                    int fee = Utils.askInt(
                            scan, "Enter adoption fee: ");

                    ResultOperation adoptionResult =
                            Service.adoptDog(adopter, dog, fee, repo);

                    printResult(adoptionResult);
                    break;

                case "4":
                    String ownerId = Utils.askString(
                            scan, "Enter person ID: ");

                    Person owner = repo.getPersonById(ownerId);

                    if (owner == null) {
                        System.out.println("Person not found.");
                        break;
                    }

                    ResultOperation dogsResult =
                            Service.getDogsByOwner(owner, repo);

                    printResult(dogsResult);
                    break;

                case "5":
                    String searchedDogId = Utils.askString(
                            scan, "Enter dog ID: ");

                    Dog searchedDog = repo.getDogById(searchedDogId);

                    if (searchedDog == null) {
                        System.out.println("Dog not found.");
                        break;
                    }

                    ResultOperation ownersResult =
                            Service.getOwnersByDog(searchedDog, repo);

                    printResult(ownersResult);
                    break;

                case "6":
                    String returningPersonId = Utils.askString(
                            scan, "Enter person ID: ");
                    String returningDogId = Utils.askString(
                            scan, "Enter dog ID: ");

                    Person returningPerson =
                            repo.getPersonById(returningPersonId);
                    Dog returningDog =
                            repo.getDogById(returningDogId);

                    if (returningPerson == null) {
                        System.out.println("Person not found.");
                        break;
                    }

                    if (returningDog == null) {
                        System.out.println("Dog not found.");
                        break;
                    }

                    ResultOperation returnResult =
                            Service.returnDog(
                                    returningPerson, returningDog, repo);

                    printResult(returnResult);
                    break;

                case "7":
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== RESULT DISPLAY =====

    private static void printResult(ResultOperation result) {
        if (result == null) {
            System.out.println("No result returned.");
            return;
        }

        System.out.println(result.getResult());

        if (result.getReason() != null
                && !result.getReason().isBlank()) {
            System.out.println(result.getReason());
        }
    }
}
