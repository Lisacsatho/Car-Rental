package se.hkr.Database.VehicleDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public void readAvailableVehicles(Date startDate, Date endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String nestedQuery = String.format("SELECT vehicleId FROM Booking_has_Vehicle WHERE" +
                                                    "(startDate BETWEEN '%1$s' AND '%2$s')" +
                                                    "OR (endDate BETWEEN '%1$s' AND '%2$s')" +
                                                    "OR (startDate <= '%1$s' AND endDate >= '%2$s')",
                                                    dateFormat.format(startDate), dateFormat.format(endDate));

            String query = String.format("SELECT * FROM Vehicle WHERE id NOT IN (%s)", nestedQuery);
            ResultSet set = statement.executeQuery(query);

            while (set.next()) {
                System.out.printf("%s, %s%n", set.getString("brand"), set.getString("model"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
