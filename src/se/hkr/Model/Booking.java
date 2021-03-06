package se.hkr.Model;

import javafx.util.Pair;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;

import java.util.Date;
import java.util.List;

public class Booking implements Model {
    private int id;
    private Date startDate;
    private Date endDate;
    private double totalPrice;
    private String member;
    private boolean isReturned;

    private List<Pair<Vehicle, VehicleOption>> vehicleOptions;
    private List<Vehicle> vehicles;

    public Booking(int id, Date startDate, Date endDate, double totalPrice, String member, boolean isReturned) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.member = member;
        this.isReturned = isReturned;
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

    public List<Pair<Vehicle, VehicleOption>> getVehicleOptions() {
        return vehicleOptions;
    }

    public void setVehicleOptions(List<Pair<Vehicle, VehicleOption>> vehicleOptions) {
        this.vehicleOptions = vehicleOptions;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    @Override
    public boolean matches(String key) {
        if (Integer.toString(id).equals(key)) {
            return true;
        } else if (member.matches(".*"+key+".*")) {
            return true;
        } else {
            return false;
        }
    }
}

