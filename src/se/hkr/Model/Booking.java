package se.hkr.Model;

import se.hkr.Model.Vehicle.VehicleOption;

import java.util.Date;
import java.util.List;

public class Booking {
    private int id;
    private Date startDate;
    private Date endDate;
    private double totalPrice;

    private List<VehicleOption> vehicleOptions;

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

    public Booking(int id, Date startDate, Date endDate, double totalPrice) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;

    }
}

