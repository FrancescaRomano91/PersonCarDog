package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dog {

    private String id;
    private String name;
    private String breed;
    private int age;
    private Person owner;
    private List<Person> owners = new ArrayList<>();

    public Dog() {}

    public Dog(String name, String breed, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.breed = breed;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Person> getOwners() {
        return owners;
    }

    public void setOwners(List<Person> owners) {
        this.owners = owners;
    }

    @Override
    public String toString() {
        StringBuilder ownerIds = new StringBuilder();
        for (Person p : owners) {
            if (ownerIds.length() > 0) {
                ownerIds.append(", ");
            }
            ownerIds.append(p.getId());
        }
        return "Dog{id='" + id + "', name='" + name + "', breed='" + breed + "', age=" + age +
                ", ownerIds=[" + ownerIds + "]}";
    }
}
