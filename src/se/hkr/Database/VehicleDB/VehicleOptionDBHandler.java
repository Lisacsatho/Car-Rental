package se.hkr.Database.VehicleDB;

import javafx.util.Pair;
import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleOptionDBHandler extends ModelDBHandler<VehicleOption> {
    @Override
    public void insert(VehicleOption model) throws SQLException {

    }

    @Override
    public void update(VehicleOption model) throws SQLException {

    }

    @Override
    public void delete(VehicleOption model) throws SQLException {

    }

    @Override
    public List<VehicleOption> readAll() throws SQLException {
        return null;
    }

    @Override
    public VehicleOption readByPrimaryKey(String key) throws SQLException {
        String query = "SELECT * FROM vehicleoption WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(key));
            ResultSet set = statement.executeQuery();
            if (!buildModels(set).isEmpty()) {
                return buildModels(set).get(0);
            }
        } catch (Exception e) {
            throw new SQLException("Problem fetching vehicle options from database.", e);
        }
        return null;
    }

    public List<VehicleOption> readForVehicle(Vehicle vehicle) throws SQLException {
        String query = "SELECT * FROM vehicleoption JOIN vehiclehasvehicleoption ON vehicleoption.id=vehiclehasvehicleoption.vehicleOptionId WHERE vehiclehasvehicleoption.vehicleId=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, vehicle.getId());
            ResultSet set = statement.executeQuery();
            return buildModels(set);
        } catch (SQLException e) {
            throw new SQLException("Trouble fetching vehicle options for vehicle: " + vehicle.getModelName(), e);
        }
    }

    public List<Pair<Vehicle, VehicleOption>> readForBooking(Booking booking) throws SQLException {
        List<Pair<Vehicle, VehicleOption>> vehicleOptions = new ArrayList<>();
        String query = "SELECT vehicleoption.id id, vehicleoption.name name, vehicleoption.price price, vehicleoption.description description, vehicle.* FROM vehicleoption JOIN bookinghasvehicleoption ON vehicleoption.id = bookinghasvehicleoption.vehicleOptionId JOIN vehicle ON vehicle.id=bookinghasvehicleoption.vehicleId WHERE bookinghasvehicleoption.bookingId=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getId());
            ResultSet set = statement.executeQuery();
            return buildForBooking(set, booking);
        } catch (SQLException e) {
            throw new SQLException("Trouble fetching vehicle options for booking: " + booking.getId(), e);
        }
    }

    public List<Pair<Vehicle, VehicleOption>> buildForBooking(ResultSet set, Booking booking) throws SQLException {
        List<Pair<Vehicle, VehicleOption>> vehicleOptions = new ArrayList<>();
        try {
            while (set.next()) {
                VehicleOption vehicleOption = new VehicleOption(set.getInt("id"), set.getString("name"), set.getString("description"), set.getDouble("price"));
                Vehicle vehicle = null;
                for (Vehicle v : booking.getVehicles()) {
                    if (v.getId() == set.getInt("vehicle.id")) {
                        vehicle = v;
                    }
                }
                if (vehicle != null) {
                    vehicleOptions.add(new Pair<Vehicle, VehicleOption>(vehicle, vehicleOption));
                } else {
                    throw new SQLException("Relationship error between vehicles and bookings.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Trouble building vehicle options", e);
        }
        return vehicleOptions;
    }

    @Override
    public List<VehicleOption> buildModels(ResultSet set) throws SQLException {
        List<VehicleOption> vehicleOptions = new ArrayList<>();
        while (set.next()) {
            VehicleOption vehicleOption = new VehicleOption(set.getInt("id"), set.getString("name"), set.getString("description"), set.getDouble("price"));
            vehicleOptions.add(vehicleOption);
        }
        return vehicleOptions;
    }
}
