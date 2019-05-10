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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDBHandler extends ModelDBHandler<Booking> {
    @Override
    public void insert(Booking model) throws SQLException {

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
                List<Pair<String, VehicleOption>> vehicleOptions = vehicleOptionDBHandler.readForBooking(booking);
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
