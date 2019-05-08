package se.hkr.Database.VehicleDB;

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

    public List<VehicleOption> readForBooking(Booking booking) throws SQLException {
        String query = "SELECT * FROM vehicleoption JOIN bookinghasvehicleoption ON vehicleoption.id=bookinghasvehicleoption.vehicleOptionId WHERE vehicleoption.id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getId());
            ResultSet set = statement.executeQuery();
            return buildModels(set);
        } catch (SQLException e) {
            throw new SQLException("Trouble fetching vehicle options for booking: " + booking.getId(), e);
        }
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
