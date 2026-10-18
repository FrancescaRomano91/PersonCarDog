
package org.example.utils;

import com.github.javafaker.Faker;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.repository.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSeeder {

    public static void seedRepository(Repository repo) {
        Faker faker = new Faker();

        List<Car> temporaryCars = new ArrayList<>();
        List<Person> temporaryPeople = new ArrayList<>();

        // 1. Generate 3 Cars
        for (int i = 0; i < 3; i++) {
            Car car = new Car(
                    faker.company().name(),
                    faker.commerce().productName(),
                    faker.number().numberBetween(1995, 2025)
            );

            repo.addCar(car);
            temporaryCars.add(car);
        }

        // 2. Generate 3 People
        for (int i = 0; i < 3; i++) {
            Person person = new Person(
                    faker.name().fullName(),
                    faker.number().numberBetween(18, 80)
            );

            temporaryPeople.add(person);
        }

        // Assign cars to people
        temporaryPeople.get(0).setCar(temporaryCars.get(0));
        temporaryPeople.get(2).setCar(temporaryCars.get(1));

        // Add people to repository
        for (Person person : temporaryPeople) {
            repo.addPerson(person);
        }

        // 3. Generate 2 Dogs
        for (int i = 0; i < 2; i++) {
            Dog dog = new Dog(
                    faker.dog().name(),
                    faker.dog().breed(),
                    faker.number().numberBetween(1, 15)
            );

            repo.addDog(dog);
        }

        // 4. Generate 2 Car Transactions
        CarTransaction tx1 = new CarTransaction(
                temporaryPeople.get(1),
                temporaryPeople.get(2),
                new Date(),
                temporaryCars.get(2),
                faker.lorem().sentence()
        );

        repo.addCarTransaction(tx1);

        CarTransaction tx2 = new CarTransaction(
                temporaryPeople.get(2),
                temporaryPeople.get(0),
                new Date(),
                temporaryCars.get(2),
                faker.lorem().sentence()
        );

        repo.addCarTransaction(tx2);

        // Print verification
        System.out.println("=== DATA SEEDING COMPLETE ===");
        System.out.println("People: 3 | Cars: 3 | Dogs: 2 | Transactions: 2");
        System.out.println("TOTAL OBJECTS IN REPOSITORY: 10");
        System.out.println("=============================");
    }
}
