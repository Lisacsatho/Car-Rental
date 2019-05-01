package se.hkr.Model.Vehicle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import se.hkr.Model.Model;

import java.util.List;

public abstract class Vehicle implements Model {

    private int id;
    private double basePrice;
    private String description;
    private int passengers;
    private FuelType fuelType;
    private GearBox gearBox;
    private String modelName;
    private int modelYear;
    private VehicleBrand brand;

    private List<VehicleOption> vehicleOptions;

    public Vehicle(int id, double basePrice, String description, int passengers, FuelType fuelType, GearBox gearBox, String modelName, int modelYear, VehicleBrand brand, List<VehicleOption> vehicleOptions) {
        this.id = id;
        this.basePrice = basePrice;
        this.description = description;
        this.passengers = passengers;
        this.fuelType = fuelType;
        this.gearBox = gearBox;
        this.modelName = modelName;
        this.modelYear = modelYear;
        this.brand = brand;
        this.vehicleOptions = vehicleOptions;
    }

    public Vehicle(int id, double basePrice, String description, int passengers, FuelType fuelType, GearBox gearBox, String modelName, int modelYear, VehicleBrand brand) {
        this.id = id;
        this.basePrice = basePrice;
        this.description = description;
        this.passengers = passengers;
        this.fuelType = fuelType;
        this.gearBox = gearBox;
        this.modelName = modelName;
        this.modelYear = modelYear;
        this.brand = brand;
    }

    public Vehicle(double basePrice, String description, int passengers, FuelType fuelType, GearBox gearBox, String modelName, int modelYear, VehicleBrand brand) {
        this.basePrice = basePrice;
        this.description = description;
        this.passengers = passengers;
        this.fuelType = fuelType;
        this.gearBox = gearBox;
        this.modelName = modelName;
        this.modelYear = modelYear;
        this.brand = brand;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public VehicleBrand getBrand() {
        return brand;
    }

    public void setBrand(VehicleBrand brand) {
        this.brand = brand;
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
