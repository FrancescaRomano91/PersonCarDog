package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Person {

    private String id;
    private String name;
    private int age;
    private Car car;
    private Dog dog;
    private List<Dog> dogs = new ArrayList<>();

    public Person(){
        this.id = UUID.randomUUID().toString();
    }

    public Person(String name, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }

    @Override
    public String toString() {
        StringBuilder dogIds = new StringBuilder();
        for (Dog d : dogs) {
            if (dogIds.length() > 0) {
                dogIds.append(", ");
            }
            dogIds.append(d.getId());
        }
        return "Person{id='" + id + "', name='" + name + "', age=" + age +
                ", dogIds=[" + dogIds + "]}";
    }
}
