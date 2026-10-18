
package org.example.model;

import java.util.Date;
import java.util.UUID;

public class DogAdoption {

    private String id;
    private Person adopter;
    private Dog dog;
    private Date date;
    private int fee;
    private String contract;
    private String status;

    public DogAdoption() {
        this.id = UUID.randomUUID().toString();
    }

    // Costruttore breve usato da Service
    public DogAdoption(Person adopter, Dog dog, String status) {
        this();
        this.adopter = adopter;
        this.dog = dog;
        this.status = status;
    }

    // Costruttore completo richiesto dalla traccia
    public DogAdoption(Person adopter, Dog dog, Date date,
                       int fee, String contract, String status) {
        this();
        this.adopter = adopter;
        this.dog = dog;
        this.date = date;
        this.fee = fee;
        this.contract = contract;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public Person getAdopter() {
        return adopter;
    }

    public Dog getDog() {
        return dog;
    }

    public Date getDate() {
        return date;
    }

    public int getFee() {
        return fee;
    }

    public String getContract() {
        return contract;
    }

    public String getStatus() {
        return status;
    }

    public void setAdopter(Person adopter) {
        this.adopter = adopter;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DogAdoption{" +
                "id='" + id + '\'' +
                ", adopter=" + adopter +
                ", dog=" + dog +
                ", date=" + date +
                ", fee=" + fee +
                ", contract='" + contract + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
