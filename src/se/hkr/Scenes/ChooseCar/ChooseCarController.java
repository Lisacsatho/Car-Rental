package se.hkr.Scenes.ChooseCar;

import javafx.fxml.Initializable;
import se.hkr.BookingSession;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class ChooseCarController implements ReadController, Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date startDate = BookingSession.getInstance().getBooking().getStartDate();
        Date endDate = BookingSession.getInstance().getBooking().getEndDate();
        try {
            System.out.println(VehicleDBHandler.readAvailableVehicles(startDate, endDate));
        } catch (SQLException e) {
            // Placeholder
            System.out.println("Database interaction failed, please try again later.");
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public void search() {

    }
}
