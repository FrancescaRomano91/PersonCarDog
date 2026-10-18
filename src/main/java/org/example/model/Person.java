package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Person {

    private String id;
    private String name;
    private int age;
    private Car car;
    private List <Dog> dogs = new ArrayList<>();

    public Person(){
        this.id = UUID.randomUUID().toString();
    }

    public Person(String name, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Car getCar() {
        return car;
    }

    public List<Dog> getDogs() {
        return dogs;
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

    public void setCar(Car car) {
        this.car = car;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }

    @Override
    public String toString() {
        return "Person{id='" + id + "', name='" + name + "', age=" + age + "}";
    }
}
