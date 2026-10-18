package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dog {

    private String id;
    private String name;
    private String breed;
    private int age;
    private List<Person> owners = new ArrayList<>();

    public Dog() {}

    public Dog(String name, String breed, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.breed = breed;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public List<Person> getOwners() {
        return owners;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setOwners(List<Person> owners) {
        this.owners = owners;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString() {
        return "Dog{id='" + id + "', name='" + name + "', breed='" + breed + "', age=" + age + "}";
    }
}
