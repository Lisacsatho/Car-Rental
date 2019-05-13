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
            statement.setString(4, UserSession.getInstance().getSessionObject().getSocialSecurityNo());
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
        String query = "UPDATE booking SET totalPrice=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, model.getTotalPrice());
            statement.setInt(2, model.getId());
            if (statement.executeUpdate() < 1) {
                throw new SQLException("No booking found with id: " + model.getId());
            }
        } catch (Exception e) {
            throw new SQLException("Could not update booking.", e);
        }
    }

    public void removeVehicleFromBooking(Vehicle vehicle, Booking model) throws SQLException {
        String removeVehicle = "DELETE FROM bookinghasvehicle WHERE vehicleId=? AND bookingId=?";
        String removeVehicleOption = "DELETE FROM bookinghasvehicleoption WHERE vehicleId=? AND bookingId=?";
        try (PreparedStatement removeVehicleStatement = connection.prepareStatement(removeVehicle);
            PreparedStatement removeVehicleOptionStatement = connection.prepareStatement(removeVehicleOption)) {
            removeVehicleStatement.setInt(1, vehicle.getId());
            removeVehicleStatement.setInt(2, model.getId());
            removeVehicleOptionStatement.setInt(1, vehicle.getId());
            removeVehicleOptionStatement.setInt(2, model.getId());
            removeVehicleStatement.executeUpdate();
            removeVehicleOptionStatement.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Could not remove vehicle from booking", e);
        }
    }

    public void removeVehicleOptionFromBooking(Pair<Vehicle, VehicleOption> vehicleOption, Booking model) throws SQLException {
        String removeVehicleOption = "DELETE FROM bookinghasvehicleoption WHERE vehicleOptionId=? AND bookingId=? AND vehicleId=?";
        try (PreparedStatement statement = connection.prepareStatement(removeVehicleOption)) {
            statement.setInt(1, vehicleOption.getValue().getId());
            statement.setInt(2, model.getId());
            statement.setInt(3, vehicleOption.getKey().getId());
            if (statement.executeUpdate() < 1) {
                throw new SQLException(String.format("No vehicle option %d for booking %d and vehicle %d", vehicleOption.getValue().getId(), model.getId(), vehicleOption.getKey().getId()));
            }
        } catch (Exception e) {

        }
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

    public List<Booking> readForMember(Member member) throws SQLException {
        String query = "SELECT * FROM booking WHERE member =?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getSocialSecurityNo());
            ResultSet set = statement.executeQuery();
            return buildModels(set);
        } catch (Exception e) {
            throw new SQLException("Trouble fetching booking from database.", e);
        }
    }

    public List<Booking> readAllSimple() throws SQLException {
        String query = "SELECT * FROM booking";
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
                Booking booking = new Booking(set.getInt("bookingId"), set.getDate("startDate"), set.getDate("endDate"), set.getDouble("totalPrice"), set.getString("member"));
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
                Booking booking = new Booking(set.getInt("id"), set.getDate("startDate"), set.getDate("endDate"), set.getDouble("totalPrice"), set.getString("member"));
                bookings.add(booking);
            }
        } catch (Exception e) {
            throw new SQLException("Trouble building simple bookings from database.", e);
        }
        return bookings;
    }
}

