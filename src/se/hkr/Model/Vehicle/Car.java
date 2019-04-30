package se.hkr.Model.Vehicle;

import java.util.List;

public class Car extends Vehicle {
    private int suitcases;
    private CarType carType;

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

    public Car(int id, double basePrice, String description, int passengerSeats, FuelType fuelType, GearBox gearBox, String modelName, int modelyear, VehicleBrand brand, List<VehicleOption> vehicleOptions, int suitcases, CarType carType) {
        super(id, basePrice, description, passengerSeats, fuelType, gearBox, modelName, modelyear, brand, vehicleOptions);
        this.suitcases = suitcases;
        this.carType = carType;


    }

}

