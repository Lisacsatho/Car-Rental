package se.hkr.Database.VehicleDB;

import javafx.stage.Stage;
import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class VehicleDBHandler <V extends Vehicle> extends ModelDBHandler<V> {

    public static <V extends Vehicle> VehicleDBHandler getHandlerFor(V model) {
        if (model instanceof Car) {
            return new CarDBHandler();
        } else {
            return null;
        }
    }

    // Using prints to test out the SQL statement, should be returning objects
    public List<? extends Vehicle> readAvailableVehicles(Date startDate, Date endDate) {
        List<Vehicle> vehicles = new ArrayList<>();
        try (CarDBHandler dbHandler = new CarDBHandler()) {
            vehicles.addAll(dbHandler.readAvailableVehicles(startDate, endDate));
        } catch (Exception e) {

        }
        return vehicles;
    }

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

    }

    @Override
    public void delete(V model) throws SQLException {

    }
}
