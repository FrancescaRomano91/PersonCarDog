package org.example.service;

import org.example.model.Car;
import org.example.model.Dog;
import org.example.model.DogAdoption;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.utils.ResultOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Part 2 - Many-to-Many Dog Adoption (shared custody).
 * Covers Service.adoptDog / returnDog / getDogsByOwner / getOwnersByDog
 * and the cascade logic in deletePerson / deleteDog.
 */
public class AdoptDogServiceTest {

    private Repository repo;

    @BeforeEach
    void setUp() {
        repo = new Repository();
    }

    // =========================================================
    // adoptDog - success cases
    // =========================================================

    @Test
    void adoptDog_success() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, 150, repo);

        assertTrue(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNotNull(res.getUpdatedObject());
        assertTrue(res.getUpdatedObject() instanceof DogAdoption);

        DogAdoption adoption = (DogAdoption) res.getUpdatedObject();
        assertSame(adopter, adoption.getAdopter());
        assertSame(dog, adoption.getDog());
        assertEquals(150, adoption.getFee());
        assertEquals("ACTIVE", adoption.getStatus());
        assertNotNull(adoption.getContract());
        assertNotNull(adoption.getDate());
        assertNotNull(adoption.getId());

        // Bidirectional consistency (both sides of the relation)
        assertTrue(adopter.getDogs().contains(dog));
        assertTrue(dog.getOwners().contains(adopter));

        // Stored in repository
        assertEquals(1, repo.getAllDogAdoptions().size());
        assertSame(adoption, repo.getDogAdoptionById(adoption.getId()));
    }

    @Test
    void adoptDog_success_feeZero() {
        Person adopter = new Person("Luis", 25);
        Dog dog = new Dog("Max", "Beagle", 3);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, 0, repo);

        assertTrue(res.isSuccess());
        DogAdoption adoption = (DogAdoption) res.getUpdatedObject();
        assertEquals(0, adoption.getFee());
        assertEquals("ACTIVE", adoption.getStatus());
        assertTrue(adopter.getDogs().contains(dog));
        assertTrue(dog.getOwners().contains(adopter));
    }

    @Test
    void adoptDog_success_secondOwnerAllowed() {
        // Shared custody: a dog can have up to 2 active owners
        Person owner1 = new Person("Ana", 30);
        Person owner2 = new Person("Luis", 25);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(owner1);
        repo.addPerson(owner2);
        repo.addDog(dog);

        ResultOperation first = Service.adoptDog(owner1, dog, 100, repo);
        ResultOperation second = Service.adoptDog(owner2, dog, 120, repo);

        assertTrue(first.isSuccess());
        assertTrue(second.isSuccess());
        assertEquals(2, dog.getOwners().size());
        assertTrue(dog.getOwners().contains(owner1));
        assertTrue(dog.getOwners().contains(owner2));
        assertEquals(1, owner1.getDogs().size());
        assertEquals(1, owner2.getDogs().size());
        assertEquals(2, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_success_personWithThreeDogs() {
        // A person can have up to 3 active dogs
        Person adopter = new Person("Ana", 30);
        Dog d1 = new Dog("Rex", "Labrador", 5);
        Dog d2 = new Dog("Max", "Beagle", 3);
        Dog d3 = new Dog("Toby", "Mix", 4);
        repo.addPerson(adopter);
        repo.addDog(d1);
        repo.addDog(d2);
        repo.addDog(d3);

        assertTrue(Service.adoptDog(adopter, d1, 100, repo).isSuccess());
        assertTrue(Service.adoptDog(adopter, d2, 100, repo).isSuccess());
        assertTrue(Service.adoptDog(adopter, d3, 100, repo).isSuccess());

        assertEquals(3, adopter.getDogs().size());
        assertEquals(3, repo.getAllDogAdoptions().size());
    }

    // =========================================================
    // adoptDog - validation failures (1..7)
    // =========================================================

    @Test
    void adoptDog_fail_adopterNotInRepo() {
        Person adopter = new Person("Ana", 30); // not added
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, 100, repo);

        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
        assertEquals(0, repo.getAllDogAdoptions().size());
        assertTrue(dog.getOwners().isEmpty());
    }

    @Test
    void adoptDog_fail_dogNotInRepo() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5); // not added
        repo.addPerson(adopter);

        ResultOperation res = Service.adoptDog(adopter, dog, 100, repo);

        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
        assertTrue(adopter.getDogs().isEmpty());
        assertEquals(0, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_notAdult() {
        Person adopter = new Person("Mina", 17); // under 18
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, 100, repo);

        assertFalse(res.isSuccess());
        assertEquals("Adopter must be an adult.", res.getMessage());
        assertNull(res.getUpdatedObject());
        assertTrue(adopter.getDogs().isEmpty());
        assertTrue(dog.getOwners().isEmpty());
        assertEquals(0, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_feeNegative() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, -10, repo);

        assertFalse(res.isSuccess());
        assertEquals("Fee cannot be negative.", res.getMessage());
        assertNull(res.getUpdatedObject());
        assertTrue(adopter.getDogs().isEmpty());
        assertTrue(dog.getOwners().isEmpty());
        assertEquals(0, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_duplicateActivePair() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation first = Service.adoptDog(adopter, dog, 100, repo);
        assertTrue(first.isSuccess());

        // Same person + same dog while still ACTIVE -> must fail
        ResultOperation second = Service.adoptDog(adopter, dog, 100, repo);

        assertFalse(second.isSuccess());
        assertEquals("DogAdoption", second.getType());
        assertNull(second.getUpdatedObject());
        assertEquals(1, adopter.getDogs().size()); // unchanged
        assertEquals(1, dog.getOwners().size());   // unchanged
        assertEquals(1, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_personMaxDogs() {
        Person adopter = new Person("Ana", 30);
        Dog d1 = new Dog("Rex", "Labrador", 5);
        Dog d2 = new Dog("Max", "Beagle", 3);
        Dog d3 = new Dog("Toby", "Mix", 4);
        Dog d4 = new Dog("Luna", "Poodle", 2);
        repo.addPerson(adopter);
        repo.addDog(d1);
        repo.addDog(d2);
        repo.addDog(d3);
        repo.addDog(d4);

        assertTrue(Service.adoptDog(adopter, d1, 100, repo).isSuccess());
        assertTrue(Service.adoptDog(adopter, d2, 100, repo).isSuccess());
        assertTrue(Service.adoptDog(adopter, d3, 100, repo).isSuccess());

        // 4th dog -> limit reached
        ResultOperation fourth = Service.adoptDog(adopter, d4, 100, repo);

        assertFalse(fourth.isSuccess());
        assertEquals("Person already has the maximum number of dogs.", fourth.getMessage());
        assertEquals(3, adopter.getDogs().size());
        assertTrue(d4.getOwners().isEmpty());
        assertEquals(3, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_dogMaxOwners() {
        Person p1 = new Person("Ana", 30);
        Person p2 = new Person("Luis", 25);
        Person p3 = new Person("Marta", 40);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(p1);
        repo.addPerson(p2);
        repo.addPerson(p3);
        repo.addDog(dog);

        assertTrue(Service.adoptDog(p1, dog, 100, repo).isSuccess());
        assertTrue(Service.adoptDog(p2, dog, 100, repo).isSuccess());

        // 3rd owner -> limit reached
        ResultOperation third = Service.adoptDog(p3, dog, 100, repo);

        assertFalse(third.isSuccess());
        assertEquals("Dog already has the maximum number of owners.", third.getMessage());
        assertEquals(2, dog.getOwners().size());
        assertTrue(p3.getDogs().isEmpty());
        assertEquals(2, repo.getAllDogAdoptions().size());
    }

    // =========================================================
    // returnDog
    // =========================================================

    @Test
    void returnDog_success() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation created = Service.adoptDog(adopter, dog, 200, repo);
        DogAdoption adoption = (DogAdoption) created.getUpdatedObject();

        ResultOperation res = Service.returnDog(adopter, dog, repo);

        assertTrue(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertSame(adoption, res.getUpdatedObject());
        assertEquals("RETURNED", adoption.getStatus());
        assertTrue(adoption.getContract().endsWith(" - RETURNED"));

        // Both sides cleared
        assertFalse(adopter.getDogs().contains(dog));
        assertFalse(dog.getOwners().contains(adopter));

        // Record stays in history
        assertEquals(1, repo.getAllDogAdoptions().size());
        assertSame(adoption, repo.getDogAdoptionById(adoption.getId()));
    }

    @Test
    void returnDog_success_keepsOtherOwner() {
        Person owner1 = new Person("Ana", 30);
        Person owner2 = new Person("Luis", 25);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(owner1);
        repo.addPerson(owner2);
        repo.addDog(dog);

        Service.adoptDog(owner1, dog, 100, repo);
        Service.adoptDog(owner2, dog, 120, repo);

        ResultOperation res = Service.returnDog(owner1, dog, repo);

        assertTrue(res.isSuccess());
        assertFalse(owner1.getDogs().contains(dog));
        assertFalse(dog.getOwners().contains(owner1));
        // The other owner keeps the dog
        assertTrue(owner2.getDogs().contains(dog));
        assertTrue(dog.getOwners().contains(owner2));
        assertEquals(1, dog.getOwners().size());
    }

    @Test
    void returnDog_fail_noActiveAdoption() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.returnDog(adopter, dog, repo);

        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
    }

    @Test
    void returnDog_fail_personNotInRepo() {
        Person adopter = new Person("Ana", 30); // not added
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addDog(dog);

        ResultOperation res = Service.returnDog(adopter, dog, repo);

        assertFalse(res.isSuccess());
    }

    @Test
    void returnDog_fail_dogNotInRepo() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5); // not added
        repo.addPerson(adopter);

        ResultOperation res = Service.returnDog(adopter, dog, repo);

        assertFalse(res.isSuccess());
    }

    @Test
    void adoptDog_success_readoptAfterReturn() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        DogAdoption first = (DogAdoption) Service.adoptDog(adopter, dog, 100, repo).getUpdatedObject();
        Service.returnDog(adopter, dog, repo);

        // Re-adopt the same dog after returning it -> new ACTIVE record
        ResultOperation again = Service.adoptDog(adopter, dog, 150, repo);

        assertTrue(again.isSuccess());
        DogAdoption second = (DogAdoption) again.getUpdatedObject();
        assertNotSame(first, second);
        assertEquals("ACTIVE", second.getStatus());

        // History: one RETURNED + one ACTIVE
        assertEquals(2, repo.getAllDogAdoptions().size());
        assertEquals("RETURNED", first.getStatus());
        assertTrue(adopter.getDogs().contains(dog));
        assertTrue(dog.getOwners().contains(adopter));
    }

    // =========================================================
    // Read-only helpers
    // =========================================================

    @Test
    void getDogsByOwner_success() {
        Person adopter = new Person("Ana", 30);
        Dog d1 = new Dog("Rex", "Labrador", 5);
        Dog d2 = new Dog("Max", "Beagle", 3);
        repo.addPerson(adopter);
        repo.addDog(d1);
        repo.addDog(d2);
        Service.adoptDog(adopter, d1, 100, repo);
        Service.adoptDog(adopter, d2, 100, repo);

        ResultOperation res = Service.getDogsByOwner(adopter, repo);

        assertTrue(res.isSuccess());
        assertEquals("Dog", res.getType());
        assertNotNull(res.getUpdatedObject());
        @SuppressWarnings("unchecked")
        ArrayList<Dog> list = (ArrayList<Dog>) res.getUpdatedObject();
        assertEquals(2, list.size());
        assertTrue(list.contains(d1));
        assertTrue(list.contains(d2));
    }

    @Test
    void getDogsByOwner_personNotInRepo() {
        Person adopter = new Person("Ana", 30); // not added
        ResultOperation res = Service.getDogsByOwner(adopter, repo);
        assertFalse(res.isSuccess());
        assertNull(res.getUpdatedObject());
    }

    @Test
    void getOwnersByDog_success() {
        Person o1 = new Person("Ana", 30);
        Person o2 = new Person("Luis", 25);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(o1);
        repo.addPerson(o2);
        repo.addDog(dog);
        Service.adoptDog(o1, dog, 100, repo);
        Service.adoptDog(o2, dog, 120, repo);

        ResultOperation res = Service.getOwnersByDog(dog, repo);

        assertTrue(res.isSuccess());
        assertEquals("Person", res.getType());
        assertNotNull(res.getUpdatedObject());
        @SuppressWarnings("unchecked")
        ArrayList<Person> list = (ArrayList<Person>) res.getUpdatedObject();
        assertEquals(2, list.size());
        assertTrue(list.contains(o1));
        assertTrue(list.contains(o2));
    }

    @Test
    void getOwnersByDog_dogNotInRepo() {
        Dog dog = new Dog("Rex", "Labrador", 5); // not added
        ResultOperation res = Service.getOwnersByDog(dog, repo);
        assertFalse(res.isSuccess());
        assertNull(res.getUpdatedObject());
    }

    // =========================================================
    // Keep getDogAdoptionById / getAllDogAdoptions
    // =========================================================

    @Test
    void getDogAdoptionById_found() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        DogAdoption adoption = (DogAdoption) Service.adoptDog(adopter, dog, 200, repo).getUpdatedObject();

        ResultOperation found = Service.getDogAdoptionById(adoption.getId(), repo);
        assertTrue(found.isSuccess());
        assertEquals("DogAdoption", found.getType());
        assertSame(adoption, found.getUpdatedObject());
    }

    @Test
    void getDogAdoptionById_notFound() {
        ResultOperation res = Service.getDogAdoptionById("nonexistent-id", repo);
        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
    }

    @Test
    void getDogAdoptionById_emptyId() {
        ResultOperation res = Service.getDogAdoptionById("", repo);
        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
    }

    @Test
    void getAllDogAdoptions_empty() {
        ResultOperation res = Service.getAllDogAdoptions(repo);
        assertTrue(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNotNull(res.getUpdatedObject());
        assertTrue(res.getUpdatedObject() instanceof ArrayList);
        @SuppressWarnings("unchecked")
        ArrayList<DogAdoption> list = (ArrayList<DogAdoption>) res.getUpdatedObject();
        assertEquals(0, list.size());
    }

    @Test
    void getAllDogAdoptions_multiple() {
        Person a1 = new Person("Ana", 30);
        Person a2 = new Person("Luis", 25);
        Dog d1 = new Dog("Rex", "Labrador", 5);
        Dog d2 = new Dog("Max", "Beagle", 3);
        repo.addPerson(a1);
        repo.addPerson(a2);
        repo.addDog(d1);
        repo.addDog(d2);

        Service.adoptDog(a1, d1, 100, repo);
        Service.adoptDog(a2, d2, 150, repo);

        ResultOperation res = Service.getAllDogAdoptions(repo);
        assertTrue(res.isSuccess());
        @SuppressWarnings("unchecked")
        ArrayList<DogAdoption> list = (ArrayList<DogAdoption>) res.getUpdatedObject();
        assertEquals(2, list.size());
    }

    // =========================================================
    // Cascade on delete
    // =========================================================

    @Test
    void deletePerson_cascadesAndMarksAdoptionsReturned() {
        Person adopter = new Person("Ana", 30);
        Dog d1 = new Dog("Rex", "Labrador", 5);
        Dog d2 = new Dog("Max", "Beagle", 3);
        repo.addPerson(adopter);
        repo.addDog(d1);
        repo.addDog(d2);
        Service.adoptDog(adopter, d1, 100, repo);
        Service.adoptDog(adopter, d2, 100, repo);

        ResultOperation del = Service.deletePerson(adopter.getId(), repo);

        assertTrue(del.isSuccess());
        assertNull(repo.getPersonById(adopter.getId())); // person gone
        assertNotNull(repo.getDogById(d1.getId()));      // dogs remain
        assertNotNull(repo.getDogById(d2.getId()));
        assertFalse(d1.getOwners().contains(adopter));   // links cleaned
        assertFalse(d2.getOwners().contains(adopter));

        // Every related adoption marked RETURNED, records kept (audit trail)
        assertEquals(2, repo.getAllDogAdoptions().size());
        for (DogAdoption da : repo.getAllDogAdoptions()) {
            assertEquals("RETURNED", da.getStatus());
            assertTrue(da.getContract().endsWith(" - RETURNED"));
        }
    }

    @Test
    void deleteDog_cascadesAndMarksAdoptionsReturned() {
        Person o1 = new Person("Ana", 30);
        Person o2 = new Person("Luis", 25);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(o1);
        repo.addPerson(o2);
        repo.addDog(dog);
        Service.adoptDog(o1, dog, 100, repo);
        Service.adoptDog(o2, dog, 120, repo);

        ResultOperation del = Service.deleteDog(dog.getId(), repo);

        assertTrue(del.isSuccess());
        assertNull(repo.getDogById(dog.getId()));          // dog gone
        assertNotNull(repo.getPersonById(o1.getId()));     // people remain
        assertNotNull(repo.getPersonById(o2.getId()));
        assertFalse(o1.getDogs().contains(dog));           // links cleaned
        assertFalse(o2.getDogs().contains(dog));

        assertEquals(2, repo.getAllDogAdoptions().size());
        for (DogAdoption da : repo.getAllDogAdoptions()) {
            assertEquals("RETURNED", da.getStatus());
            assertTrue(da.getContract().endsWith(" - RETURNED"));
        }
    }

    @Test
    void deletePerson_keepsReturnedHistoryOnly() {
        // Only ACTIVE adoptions get flipped; previously RETURNED stay untouched
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        Service.adoptDog(adopter, dog, 100, repo);
        Service.returnDog(adopter, dog, repo); // now RETURNED
        Service.adoptDog(adopter, dog, 150, repo); // ACTIVE again

        Service.deletePerson(adopter.getId(), repo);

        assertEquals(2, repo.getAllDogAdoptions().size());
        for (DogAdoption da : repo.getAllDogAdoptions()) {
            assertEquals("RETURNED", da.getStatus());
        }
    }

    // =========================================================
    // Regression + safety
    // =========================================================

    @Test
    void buyCar_stillWorks_afterAdoptionLogic() {
        // Regression: existing flow unchanged
        Person seller = new Person("Seller", 40);
        Person buyer = new Person("Buyer", 30);
        Car car = new Car("Ford", "Focus", 2019);
        repo.addPerson(seller);
        repo.addPerson(buyer);
        repo.addCar(car);
        seller.setCar(car);

        ResultOperation res = Service.buyCar(buyer, seller, 5000, repo);
        assertTrue(res.isSuccess());
        assertEquals("CarTransaction", res.getType());
        assertNull(seller.getCar());
        assertSame(car, buyer.getCar());
    }

    @Test
    void personToString_noInfiniteRecursion() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);
        Service.adoptDog(adopter, dog, 100, repo);

        assertNotNull(adopter.toString());
        assertDoesNotThrow(() -> adopter.toString());
    }

    @Test
    void dogToString_noInfiniteRecursion() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);
        Service.adoptDog(adopter, dog, 100, repo);

        assertNotNull(dog.toString());
        assertDoesNotThrow(() -> dog.toString());
    }
}
