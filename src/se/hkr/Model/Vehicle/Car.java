package se.hkr.Model.Vehicle;

import java.util.List;

public class Car extends Vehicle {
    private int suitcases;
    private CarType carType;

    public Car(int id, double basePrice, String brand, String model, int modelYear, String description, int passengerSeats, FuelType fuelType, GearBox gearBox, List<VehicleOption> vehicleOptions, int suitcases, CarType carType) {
        super(id, basePrice, brand, model, modelYear, description, passengerSeats, fuelType, gearBox, vehicleOptions);
        this.suitcases = suitcases;
        this.carType = carType;
    }

    public Car(int id, double basePrice, String brand, String model, int modelYear, String description, int passengerSeats, FuelType fuelType, GearBox gearBox, int suitcases, CarType carType) {
        super(id, basePrice, brand, model, modelYear, description, passengerSeats, fuelType, gearBox);
        this.suitcases = suitcases;
        this.carType = carType;
    }

    public int getSuitcases() {
        return suitcases;
    }

    public void setSuitcases(int suitcases) {
        this.suitcases = suitcases;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }
}
