package se.hkr.Model.Vehicle;

import se.hkr.Model.Model;

public class VehicleBrand implements Model {
    private int id;
    private String name;

    public VehicleBrand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean matches(String key) {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
