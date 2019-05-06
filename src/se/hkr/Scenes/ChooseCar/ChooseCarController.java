package se.hkr.Scenes.ChooseCar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import se.hkr.BookingSession;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.sql.SQLException;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ChooseCarController implements ReadController, Initializable {

    @FXML
    TableView tblCars;

    @FXML
    TableColumn colBrand,
            colModel,
            colFuelType,
            colGearBox,
            colPrice,
            colPassengers,
            colSuitcases;

    @FXML
    ComboBox comboBrand,
            comboCarType,
            comboGearBox,
            comboPassengers;

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

    public void showTableInformation () {
        try {

            List<Vehicle> vehicles = (List<Vehicle>) VehicleDBHandler.readAvailableVehicles(BookingSession.getInstance().getBooking().getStartDate(), BookingSession.getInstance().getBooking().getEndDate());


        } catch (Exception x) {

            x.printStackTrace();
        }

    }

    @Override
    public void filter() {

    }

    @Override
    public void search() {

    }
}
