package se.hkr.Database;

import javafx.fxml.Initializable;
import javafx.util.Pair;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Database.VehicleDB.VehicleOptionDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Model.User.Member;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;
import se.hkr.UserSession;

import javax.xml.transform.Result;
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
        String removeBooking = "DELETE FROM booking WHERE id=?";
        String removeVehicleRelations = "DELETE FROM bookinghasvehicle WHERE bookingId=?";
        String removeVehicleOptionRelations = "DELETE FROM bookinghasvehicleoption WHERE bookingId=?";
        try (PreparedStatement removeBookingStmt = connection.prepareStatement(removeBooking);
            PreparedStatement removeVehicleRelationsStmt = connection.prepareStatement(removeVehicleRelations);
            PreparedStatement removeVehicleOptionRelationsStmt = connection.prepareStatement(removeVehicleOptionRelations)) {
            removeBookingStmt.setInt(1, model.getId());
            removeVehicleRelationsStmt.setInt(1, model.getId());
            removeVehicleOptionRelationsStmt.setInt(1, model.getId());
            removeVehicleRelationsStmt.executeUpdate();
            removeVehicleOptionRelationsStmt.executeUpdate();
            removeBookingStmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Cannot remove booking" + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> readAll() throws SQLException {
        String query = "SELECT * FROM AllBookings";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet set = statement.executeQuery();
            return buildModels(set);
        } catch (Exception e) {
            throw new SQLException("Trouble fetching booking from database.", e);
        }
    }

    @Override
    public Booking readByPrimaryKey(String key) throws SQLException {
        String query = "SELECT * FROM AllBookings WHERE bookingId=? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(key));
            ResultSet set = statement.executeQuery();
            List<Booking> result = buildModels(set);
            if (!result.isEmpty()) {
                return result.get(0);
            }
        } catch (Exception e) {
            throw new SQLException("Trouble fetching booking from database.", e);
        }
        return null;
    }

    public List<Booking> readAllSimple() throws SQLException {
        String query = "SELECT * FROM AllBookings";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            return buildSimpleModels(statement.executeQuery());
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    public List<Booking> readForMemberSimple(Member member) throws SQLException {
        String query = "SELECT * FROM AllBookings WHERE member=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getSocialSecurityNo());
            return buildSimpleModels(statement.executeQuery());
        }
    }

    @Override
    public List<Booking> buildModels(ResultSet set) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        try (VehicleOptionDBHandler vehicleOptionDBHandler = new VehicleOptionDBHandler()) {
            while (set.next()) {
                Booking booking = new Booking(set.getInt("bookingId"), set.getDate("startDate"), set.getDate("endDate"), set.getDouble("totalPrice"));
                // It's important that the vehicles are read before the vehicle options.
                List<Vehicle> vehicles = VehicleDBHandler.readForBooking(booking);
                booking.setVehicles(vehicles);
                List<Pair<Vehicle, VehicleOption>> vehicleOptions = vehicleOptionDBHandler.readForBooking(booking);
                booking.setVehicleOptions(vehicleOptions);
                bookings.add(booking);
            }
        } catch (Exception e) {
            throw new SQLException("Trouble building bookings from database.", e);
        }
        return bookings;
    }

    // Build simple refers to bookings that doesn't necessarily need the vehicles and vehicle options.
    public List<Booking> buildSimpleModels(ResultSet set) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        try {
            while (set.next()) {
                Booking booking = new Booking(set.getInt("bookingId"), set.getDate("startDate"), set.getDate("endDate"), set.getDouble("totalPrice"));
                bookings.add(booking);
            }
        } catch (Exception e) {
            throw new SQLException("Trouble building simple bookings from database.", e);
        }
        return bookings;
    }
}
