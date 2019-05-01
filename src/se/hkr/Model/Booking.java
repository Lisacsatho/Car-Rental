package se.hkr.Model;

import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;

import java.util.Date;
import java.util.List;

public class Booking implements Model {
    private int id;
    private Date startDate;
    private Date endDate;
    private double totalPrice;

    private List<VehicleOption> vehicleOptions;
    private List<Vehicle> vehicles;

    public Booking(int id, Date startDate, Date endDate, double totalPrice) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
    }

    public Booking() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<VehicleOption> getVehicleOptions() {
        return vehicleOptions;
    }

    public void setVehicleOptions(List<VehicleOption> vehicleOptions) {
        this.vehicleOptions = vehicleOptions;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public boolean matches(String key) {
        // TODO: Specify search algorithm
        return false;
    }
}

