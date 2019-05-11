package se.hkr.Database;

import javafx.fxml.Initializable;
import javafx.util.Pair;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Database.VehicleDB.VehicleOptionDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;
import se.hkr.UserSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingDBHandler extends ModelDBHandler<Booking> {
    @Override
    public void insert(Booking model) throws SQLException {
        String insert = "INSERT INTO booking (totalPrice, startDate, endDate, member) VALUES (?, DATE(?), DATE(?), ?)";
        String insertVehicles = "INSERT INTO bookinghasvehicle VALUES(?, ?)";
        String insertVehicleOptions = "INSERT INTO bookinghasvehicleoption VALUES(?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insert);
             PreparedStatement insertVehicleRelation = connection.prepareStatement(insertVehicles);
             PreparedStatement insertVehicleOptionRelation = connection.prepareStatement(insertVehicleOptions)) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            statement.setDouble(1, model.getTotalPrice());
            statement.setString(2, simpleDateFormat.format(model.getStartDate()));
            statement.setString(3, simpleDateFormat.format(model.getEndDate()));
            statement.setString(4, UserSession.getInstance().getLoggedInUser().getSocialSecurityNo());
            statement.executeUpdate();

            String getId = "SELECT LAST_INSERT_ID() AS id";
            ResultSet set = statement.executeQuery(getId);
            set.next();
            model.setId(set.getInt("id"));

            for (Vehicle vehicle : model.getVehicles()) {
                insertVehicleRelation.setInt(1, model.getId());
                insertVehicleRelation.setInt(2, vehicle.getId());
                insertVehicleRelation.executeUpdate();
            }
            for (Pair<Vehicle, VehicleOption> pair : model.getVehicleOptions()) {
                insertVehicleOptionRelation.setInt(1, pair.getValue().getId());
                insertVehicleOptionRelation.setInt(2, model.getId());
                insertVehicleOptionRelation.setInt(3, pair.getKey().getId());
                insertVehicleOptionRelation.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException("Could not insert booking in database: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Booking model) throws SQLException {

    }

    @Override
    public void delete(Booking model) throws SQLException {

    }

    @Override
    public List<Booking> readAll() throws SQLException {
        return null;
    }

    @Override
    public Booking readByPrimaryKey(String key) throws SQLException {
        String query = "SELECT * FROM booking WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(key));
            ResultSet set = statement.executeQuery();
            if (!buildModels(set).isEmpty()) {
                return buildModels(set).get(0);
            }
        } catch (Exception e) {
            throw new SQLException("Trouble fetching booking from database.", e);
        }
        return null;
    }

    @Override
    public List<Booking> buildModels(ResultSet set) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        try (VehicleOptionDBHandler vehicleOptionDBHandler = new VehicleOptionDBHandler()) {
            while (set.next()) {
                Booking booking = new Booking(set.getInt("id"), set.getDate("startDate"), set.getDate("endDate"), set.getDouble("totalPrice"));
                List<Vehicle> vehicles = VehicleDBHandler.readForBooking(booking);
                List<Pair<Vehicle, VehicleOption>> vehicleOptions = vehicleOptionDBHandler.readForBooking(booking);
                booking.setVehicles(vehicles);
                booking.setVehicleOptions(vehicleOptions);
                bookings.add(booking);
            }
        } catch (Exception e) {
            throw new SQLException("Trouble building bookings from database.", e);
        }
        return bookings;
    }

    public Booking readForBooking(String key) throws SQLException {
        String query = "SELECT * FROM booking WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(key));
            ResultSet set = statement.executeQuery();
            if (!buildModels(set).isEmpty()) {
                return buildModels(set).get(0);
            }
        } catch (Exception e) {
            throw new SQLException("Trouble fetching booking from database.", e);
        }
        return null;
    }
}
