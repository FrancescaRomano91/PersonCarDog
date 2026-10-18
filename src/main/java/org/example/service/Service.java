
package org.example.service;

import org.example.model.*;
import org.example.repository.Repository;

import java.util.ArrayList;
import java.util.Date;

public class Service {

    // ========== PERSON CRUD ==========

    public static Person createPerson(String name, int age, Repository repo) {
        System.out.println("Welcome to CREATE PERSON");

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return null;
        }

        if (age < 0) {
            System.out.println("Age cannot be negative.");
            return null;
        }

        Person p = new Person(name.trim(), age);
        repo.addPerson(p);

        System.out.println("Person created: " + p);
        return p;
    }

    public static Person getPersonById(String id, Repository repo) {
        System.out.println("Welcome to GET PERSON BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            System.out.println("Person not found with id: " + id);
        } else {
            System.out.println("Found: " + p);
        }

        return p;
    }

    public static ArrayList<Person> getAllPeople(Repository repo) {
        System.out.println("Welcome to LIST ALL PEOPLE");

        ArrayList<Person> list = repo.getAllPeople();
        System.out.println("Total people: " + list.size());

        for (Person p : list) {
            System.out.println(p);
        }

        return list;
    }

    public static boolean updatePerson(
            String id, String newName, int newAge, Repository repo) {

        System.out.println("Welcome to UPDATE PERSON");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            System.out.println("Person not found.");
            return false;
        }

        if (newName != null && !newName.trim().isEmpty()) {
            p.setName(newName.trim());
        }

        if (newAge >= 0) {
            p.setAge(newAge);
        }

        System.out.println("Person updated: " + p);
        return true;
    }

    public static boolean deletePerson(String id, Repository repo) {
        System.out.println("Welcome to DELETE PERSON");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            System.out.println("Person not found.");
            return false;
        }

        // Clear car reference
        if (p.getCar() != null) {
            p.setCar(null);
        }

        // Close active adoptions involving this person
        for (DogAdoption adoption : repo.getAllDogAdoptions()) {
            if (adoption.getAdopter() != null
                    && adoption.getAdopter().getId().equals(p.getId())
                    && "ACTIVE".equals(adoption.getStatus())) {

                Dog dog = adoption.getDog();

                if (dog != null) {
                    p.getDogs().remove(dog);
                    dog.getOwners().remove(p);
                }

                adoption.setStatus("RETURNED");
                adoption.setContract(
                        (adoption.getContract() == null
                                ? "" : adoption.getContract())
                                + " - RETURNED"
                );
            }
        }

        boolean removed = repo.removePerson(id.trim());

        if (removed) {
            System.out.println("Person deleted successfully.");
        }

        return removed;
    }


    // ========== CAR CRUD ==========
    // Manteniamo la tua implementazione originale.

    public static Car createCar(
            String make, String model, int year, Repository repo) {

        System.out.println("Welcome to CREATE CAR");

        if (make == null || make.trim().isEmpty()) {
            System.out.println("Make cannot be empty.");
            return null;
        }

        if (model == null || model.trim().isEmpty()) {
            System.out.println("Model cannot be empty.");
            return null;
        }

        if (year < 1886) {
            System.out.println("Year looks invalid.");
            return null;
        }

        Car c = new Car(make.trim(), model.trim(), year);
        repo.addCar(c);

        System.out.println("Car created: " + c);
        return c;
    }

    public static Car getCarById(String id, Repository repo) {
        System.out.println("Welcome to GET CAR BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            System.out.println("Car not found with id: " + id);
        } else {
            System.out.println("Found: " + c);
        }

        return c;
    }

    public static ArrayList<Car> getAllCars(Repository repo) {
        System.out.println("Welcome to LIST ALL CARS");

        ArrayList<Car> list = repo.getAllCars();
        System.out.println("Total cars: " + list.size());

        for (Car c : list) {
            System.out.println(c);
        }

        return list;
    }

    public static boolean updateCar(
            String id, String newMake, String newModel,
            int newYear, Repository repo) {

        System.out.println("Welcome to UPDATE CAR");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            System.out.println("Car not found.");
            return false;
        }

        if (newMake != null && !newMake.trim().isEmpty()) {
            c.setMake(newMake.trim());
        }

        if (newModel != null && !newModel.trim().isEmpty()) {
            c.setModel(newModel.trim());
        }

        if (newYear >= 1886) {
            c.setYear(newYear);
        }

        System.out.println("Car updated: " + c);
        return true;
    }

    public static boolean deleteCar(String id, Repository repo) {
        System.out.println("Welcome to DELETE CAR");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            System.out.println("Car not found.");
            return false;
        }

        // Clear from any person owning it
        for (Person p : repo.getAllPeople()) {
            if (p.getCar() != null
                    && p.getCar().getId().equals(id.trim())) {
                p.setCar(null);
                System.out.println(
                        "Removed car reference from owner: " + p);
            }
        }

        boolean removed = repo.removeCar(id.trim());

        if (removed) {
            System.out.println("Car deleted successfully.");
        }

        return removed;
    }


    // ========== DOG CRUD ==========

    public static Dog createDog(
            String name, String breed, int age, Repository repo) {

        System.out.println("Welcome to CREATE DOG");

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return null;
        }

        if (breed == null || breed.trim().isEmpty()) {
            System.out.println("Breed cannot be empty.");
            return null;
        }

        if (age < 0) {
            System.out.println("Age cannot be negative.");
            return null;
        }

        Dog d = new Dog(name.trim(), breed.trim(), age);
        repo.addDog(d);

        System.out.println("Dog created: " + d);
        return d;
    }

    public static Dog getDogById(String id, Repository repo) {
        System.out.println("Welcome to GET DOG BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        Dog d = repo.getDogById(id.trim());

        if (d == null) {
            System.out.println("Dog not found with id: " + id);
        } else {
            System.out.println("Found: " + d);
        }

        return d;
    }

    public static ArrayList<Dog> getAllDogs(Repository repo) {
        System.out.println("Welcome to LIST ALL DOGS");

        ArrayList<Dog> list = repo.getAllDogs();
        System.out.println("Total dogs: " + list.size());

        for (Dog d : list) {
            System.out.println(d);
        }

        return list;
    }

    public static boolean updateDog(
            String id, String newName, String newBreed,
            int newAge, Repository repo) {

        System.out.println("Welcome to UPDATE DOG");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Dog d = repo.getDogById(id.trim());

        if (d == null) {
            System.out.println("Dog not found.");
            return false;
        }

        if (newName != null && !newName.trim().isEmpty()) {
            d.setName(newName.trim());
        }

        if (newBreed != null && !newBreed.trim().isEmpty()) {
            d.setBreed(newBreed.trim());
        }

        if (newAge >= 0) {
            d.setAge(newAge);
        }

        System.out.println("Dog updated: " + d);
        return true;
    }

    public static boolean deleteDog(String id, Repository repo) {
        System.out.println("Welcome to DELETE DOG");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Dog dog = repo.getDogById(id.trim());

        if (dog == null) {
            System.out.println("Dog not found.");
            return false;
        }

        // Close active adoptions involving this dog
        for (DogAdoption adoption : repo.getAllDogAdoptions()) {
            if (adoption.getDog() != null
                    && adoption.getDog().getId().equals(dog.getId())
                    && "ACTIVE".equals(adoption.getStatus())) {

                Person person = adoption.getAdopter();

                if (person != null) {
                    person.getDogs().remove(dog);
                    dog.getOwners().remove(person);
                }

                adoption.setStatus("RETURNED");
                adoption.setContract(
                        (adoption.getContract() == null
                                ? "" : adoption.getContract())
                                + " - RETURNED"
                );
            }
        }

        boolean removed = repo.removeDog(id.trim());

        if (removed) {
            System.out.println("Dog deleted successfully.");
        }

        return removed;
    }


    // ========== CAR TRANSACTION: READ ONLY ==========

    public static CarTransaction getCarTransactionById(
            String id, Repository repo) {

        System.out.println("Welcome to GET CAR TRANSACTION BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        CarTransaction ct = repo.getCarTransactionById(id.trim());

        if (ct == null) {
            System.out.println(
                    "CarTransaction not found with id: " + id);
        } else {
            System.out.println("Found: " + ct);
        }

        return ct;
    }

    public static ArrayList<CarTransaction> getAllCarTransactions(
            Repository repo) {

        System.out.println("Welcome to LIST ALL CAR TRANSACTIONS");

        ArrayList<CarTransaction> list =
                repo.getAllCarTransactions();

        System.out.println("Total transactions: " + list.size());

        for (CarTransaction ct : list) {
            System.out.println(ct);
        }

        return list;
    }


    // ========== DOG ADOPTION: READ ONLY ==========

    public static DogAdoption getDogAdoptionById(
            String id, Repository repo) {

        System.out.println("Welcome to GET ADOPTION BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        DogAdoption dg = repo.getDogAdoptionById(id.trim());

        if (dg == null) {
            System.out.println(
                    "DogAdoption not found with id: " + id);
        } else {
            System.out.println("Found: " + dg);
        }

        return dg;
    }

    public static ArrayList<DogAdoption> getAllDogAdoptions(
            Repository repo) {

        System.out.println("Welcome to LIST ALL DOG ADOPTIONS");

        ArrayList<DogAdoption> list =
                repo.getAllDogAdoptions();

        System.out.println("Total adoptions: " + list.size());

        for (DogAdoption dg : list) {
            System.out.println(dg);
        }

        return list;
    }


    // ========== BUY CAR ==========
    // Mantenuto come nel tuo codice originale.

    public static boolean buyCar(
            Person buyer, Person seller, int price, Repository repo) {

        System.out.println("Welcome to BUY MENU");

        // Buyer exists in repository
        if (!repo.getAllPeople().contains(buyer)) {
            System.out.println("Buyer not found in repository.");
            return false;
        }

        System.out.println("Buyer exists: " + buyer);

        // Seller exists in repository
        if (!repo.getAllPeople().contains(seller)) {
            System.out.println("Seller not found in repository.");
            return false;
        }

        System.out.println("Seller exists: " + seller);

        // Seller must have a car
        if (seller.getCar() == null) {
            System.out.println("Seller does not have a car to sell.");
            return false;
        }

        Car car = seller.getCar();
        System.out.println("Seller can sell a car: " + car);

        // Buyer must not already have a car
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car.");
            return false;
        }

        System.out.println("Buyer can buy a car.");

        buyer.setCar(car);
        seller.setCar(null);

        System.out.println(
                "Settings done, now creating CarTransaction ...");

        CarTransaction transaction = new CarTransaction(
                buyer,
                seller,
                new Date(),
                car,
                "Car sold for " + price
        );

        repo.addCarTransaction(transaction);

        System.out.println(transaction);

        return true;
    }


    // ========== DOG ADOPTION: CREATE ==========

    public static ResultOperation adoptDog(
            Person adopter, Dog dog, int fee, Repository repo) {

        // 1. Check person exists
        if (adopter == null
                || repo.getPersonById(adopter.getId()) == null) {
            return new ResultOperation("ERROR", "Person not found");
        }

        // 2. Check dog exists
        if (dog == null
                || repo.getDogById(dog.getId()) == null) {
            return new ResultOperation("ERROR", "Dog not found");
        }

        // 3. Person must be an adult
        if (adopter.getAge() < 18) {
            return new ResultOperation(
                    "ERROR", "Person must be at least 18");
        }

        // 4. Fee cannot be negative
        if (fee < 0) {
            return new ResultOperation(
                    "ERROR", "Fee cannot be negative");
        }

        // 5. Check duplicate active adoption
        for (DogAdoption existing : repo.getAllDogAdoptions()) {
            if (existing.getAdopter() != null
                    && existing.getDog() != null
                    && existing.getAdopter().getId().equals(adopter.getId())
                    && existing.getDog().getId().equals(dog.getId())
                    && "ACTIVE".equals(existing.getStatus())) {

                return new ResultOperation(
                        "ERROR",
                        "This person has already adopted this dog"
                );
            }
        }

        // 6. Person can have at most 3 active dogs
        if (adopter.getDogs().size() >= 3) {
            return new ResultOperation(
                    "ERROR",
                    "Person has reached the maximum number of dogs"
            );
        }

        // 7. Dog can have at most 2 active owners
        if (dog.getOwners().size() >= 2) {
            return new ResultOperation(
                    "ERROR",
                    "Dog has reached the maximum number of owners"
            );
        }

        // 8. Create adoption
        DogAdoption adoption =
                new DogAdoption(adopter, dog, "ACTIVE");

        adoption.setDate(new Date());
        adoption.setFee(fee);
        adoption.setContract("Dog adopted for " + fee);

        // 9. Update both sides of the relationship
        adopter.getDogs().add(dog);
        dog.getOwners().add(adopter);

        // 10. Save adoption in repository
        repo.addDogAdoption(adoption);

        return new ResultOperation(
                "SUCCESS", "Dog adopted successfully");
    }


    // ========== DOG ADOPTION: READ RELATIONSHIPS ==========

    public static ResultOperation getDogsByOwner(
            Person owner, Repository repo) {

        if (owner == null
                || repo.getPersonById(owner.getId()) == null) {
            return new ResultOperation("ERROR", "Person not found");
        }

        return new ResultOperation(
                "SUCCESS", owner.getDogs().toString());
    }

    public static ResultOperation getOwnersByDog(
            Dog dog, Repository repo) {

        if (dog == null
                || repo.getDogById(dog.getId()) == null) {
            return new ResultOperation("ERROR", "Dog not found");
        }

        return new ResultOperation(
                "SUCCESS", dog.getOwners().toString());
    }


    // ========== DOG ADOPTION: RETURN ==========

    public static ResultOperation returnDog(
            Person person, Dog dog, Repository repo) {

        if (person == null
                || repo.getPersonById(person.getId()) == null) {
            return new ResultOperation("ERROR", "Person not found");
        }

        if (dog == null
                || repo.getDogById(dog.getId()) == null) {
            return new ResultOperation("ERROR", "Dog not found");
        }

        for (DogAdoption adoption : repo.getAllDogAdoptions()) {
            if (adoption.getAdopter() != null
                    && adoption.getDog() != null
                    && adoption.getAdopter().getId().equals(person.getId())
                    && adoption.getDog().getId().equals(dog.getId())
                    && "ACTIVE".equals(adoption.getStatus())) {

                person.getDogs().remove(dog);
                dog.getOwners().remove(person);

                adoption.setStatus("RETURNED");
                adoption.setContract(
                        (adoption.getContract() == null
                                ? "" : adoption.getContract())
                                + " - RETURNED"
                );

                return new ResultOperation(
                        "SUCCESS", "Dog returned successfully");
            }
        }

        return new ResultOperation(
                "ERROR", "No active adoption found");
    }
}
