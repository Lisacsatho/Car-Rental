package se.hkr.Model.Vehicle;

import se.hkr.Model.Model;

import java.util.List;

public abstract class Vehicle implements Model {

    private int id;
    private double basePrice;
    private String brand;
    private String model;
    private int modelYear;
    private String description;
    private int passengerSeats;
    private FuelType fuelType;
    private GearBox gearBox;

    private List<VehicleOption> vehicleOptions;

    public Vehicle(int id, double basePrice, String brand, String model, int modelYear, String description, int passengerSeats, FuelType fuelType, GearBox gearBox, List<VehicleOption> vehicleOptions) {
        this.id = id;
        this.basePrice = basePrice;
        this.brand = brand;
        this.model = model;
        this.modelYear = modelYear;
        this.description = description;
        this.passengerSeats = passengerSeats;
        this.fuelType = fuelType;
        this.gearBox = gearBox;
        this.vehicleOptions = vehicleOptions;
    }

    public Vehicle(int id, double basePrice, String brand, String model, int modelYear, String description, int passengerSeats, FuelType fuelType, GearBox gearBox) {
        this.id = id;
        this.basePrice = basePrice;
        this.brand = brand;
        this.model = model;
        this.modelYear = modelYear;
        this.description = description;
        this.passengerSeats = passengerSeats;
        this.fuelType = fuelType;
        this.gearBox = gearBox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public GearBox getGearBox() {
        return gearBox;
    }

    public void setGearBox(GearBox gearBox) {
        this.gearBox = gearBox;
    }

    public List<VehicleOption> getVehicleOptions() {
        return vehicleOptions;
    }

    public void setVehicleOptions(List<VehicleOption> vehicleOptions) {
        this.vehicleOptions = vehicleOptions;
    }

    @Override
    public boolean matches(String key) {
        // TODO
        return false;
    }
}
