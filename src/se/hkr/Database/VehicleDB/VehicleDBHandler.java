package se.hkr.Database.VehicleDB;
import com.mysql.cj.jdbc.exceptions.SQLError;
import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class VehicleDBHandler <V extends Vehicle> extends ModelDBHandler<V> {

    public static <V extends Vehicle> VehicleDBHandler getHandlerFor(V model) {
        if (model instanceof Car) {
            return new CarDBHandler();
        } else {
            System.out.println("No!");
            return null;
        }
    }

    public static List<? extends Vehicle> readAvailableVehicles(Date startDate, Date endDate) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        try (CarDBHandler dbHandler = new CarDBHandler()) {
            vehicles.addAll(dbHandler.readAvailable(startDate, endDate));
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return vehicles;
    }

    public abstract List<? extends Vehicle> readAvailable(Date startDate, Date endDate) throws SQLException;

    public static List<Vehicle> readForBooking(Booking booking) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        try (CarDBHandler carDBHandler = new CarDBHandler()) {
            vehicles.addAll(carDBHandler.readForBookingSpecific(booking));
        } catch (Exception e) {
            throw new SQLException("Trouble reading vehicles for booking.", e);
        }
        return vehicles;
    }

    public static Vehicle readAbstractByPrimaryKey(String key) throws SQLException {
        try (CarDBHandler carDBHandler = new CarDBHandler()) {
            return carDBHandler.readByPrimaryKey(key);
        } catch (Exception e) {
            throw new SQLException("Could not read abstract vehicle", e);
        }
    }

    public static List<Vehicle> readAbstractAll() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        try (CarDBHandler carDBHandler = new CarDBHandler()){
            vehicles.addAll(carDBHandler.readAll());
        } catch (Exception e) {
            throw new SQLException("Cannot read vehicles: " + e.getMessage(), e);
        }
        return vehicles;
    }

    public static List<Vehicle> readAbstractAllIncludingInactive() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        try (CarDBHandler carDBHandler = new CarDBHandler()) {
            vehicles.addAll(carDBHandler.readAllIncludingInactive());
        } catch (Exception e) {
            throw new SQLException("Cannot read vehicles: " + e.getMessage(), e);
        }
        return vehicles;
    }

    // come up with a better name for abstract method.
    public abstract List<V> readForBookingSpecific(Booking booking) throws SQLException;

    @Override
    public void insert(V model) throws SQLException {
        String insert = "INSERT INTO vehicle (fuelType, gearBox, price, description, modelName, modelYear, passengers, brand)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String getId = "SELECT LAST_INSERT_ID() as id";
        try (PreparedStatement insertStatement = connection.prepareStatement(insert);
            PreparedStatement getIdStatement = connection.prepareStatement(getId)) {
            insertStatement.setInt(1, model.getFuelType().getId());
            insertStatement.setInt(2, model.getGearBox().getId());
            insertStatement.setDouble(3, model.getBasePrice());
            insertStatement.setString(4, model.getDescription());
            insertStatement.setString(5, model.getModelName());
            insertStatement.setInt(6, model.getModelYear());
            insertStatement.setInt(7, model.getPassengers());
            insertStatement.setInt(8, model.getBrand().getId());
            insertStatement.execute();
            // Get the id inserted and set the models id to it
            ResultSet set = getIdStatement.executeQuery();
            while (set.next()) {
                model.setId(set.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(V model) throws SQLException {
        String query = "UPDATE vehicle SET price=?, readyForRent =? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, model.getBasePrice());
            statement.setBoolean(2, model.isReadyForRent());
            statement.setInt(3, model.getId());
            if (statement.executeUpdate() != 1) {
                throw new SQLException("No vehicle with id: " + model.getId());
            }
        } catch (Exception e) {
            throw new SQLException("Could not update vehicle: " + model.getModelName(), e);
        }
    }

    public void inactivate(V model) throws SQLException {
        String query = "UPDATE vehicle SET inInventory=0 WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, model.getId());
            if (statement.executeUpdate() != 1) {
                throw new SQLException("No vehicle with id: " + model.getId() + " found.");
            }
        } catch (Exception e) {
            throw new SQLException("Could not disable vehicle.");
        }
    }

    @Override
    public void delete(V model) throws SQLException {

    }

    public static Vehicle buildSingle(ResultSet set) {
        return CarDBHandler.buildSingle(set);
    }
}
