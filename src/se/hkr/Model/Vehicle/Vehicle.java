package se.hkr.Model.Vehicle;

import se.hkr.Model.Model;

import java.util.List;

public abstract class Vehicle implements Model {

    private int Id;
    private double basePrice;
    private String brand;
    private String model;
    private int modelYear;
    private String description;
    private int passengerSeats;

    private List<VehicleOption> vehicleOptions;

    public Vehicle(int id, double basePrice, String brand, String model, int modelYear, String description, int passengerSeats) {
        Id = id;
        this.basePrice = basePrice;
        this.brand = brand;
        this.model = model;
        this.modelYear = modelYear;
        this.description = description;
        this.passengerSeats = passengerSeats;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPassengerSeats() {
        return passengerSeats;
    }

    public void setPassengerSeats(int passengerSeats) {
        this.passengerSeats = passengerSeats;
    }

    @Override
    public boolean matches(String key) {
        // TODO
        return false;
    }
}
