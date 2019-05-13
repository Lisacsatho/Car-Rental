package se.hkr;

import se.hkr.Database.BookingDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;

import java.sql.SQLException;
import java.util.List;

public class CarStatus {

    private static CarStatus ourInstance = new CarStatus();

    public static CarStatus getInstance() {
        return ourInstance;
    }

    public static boolean makeCarUnavailable(Car car) throws SQLException {
        boolean isAvailable;

        if (isAvailable = true) {

            try {
                BookingDBHandler bookingDBHandler = new BookingDBHandler() {

                    Booking booking = BookingSession.getInstance().getBooking();
                    List<? extends Vehicle> controlList = VehicleDBHandler.readAvailableVehicles(booking.getStartDate(),
                            booking.getEndDate());
                };

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static void makeCarAvailable(Car car) throws SQLException {
        boolean isAvailable = true;
    }
}

