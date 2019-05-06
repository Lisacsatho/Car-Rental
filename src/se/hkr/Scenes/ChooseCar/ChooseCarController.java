package se.hkr.Scenes.ChooseCar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import se.hkr.BookingSession;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Scenes.MainMenu.MainMenuController;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.sql.SQLException;
import java.time.Period;
import java.util.Date;
import java.util.ResourceBundle;

public class ChooseCarController implements ReadController, Initializable {

    @FXML
    private TableView tblCars;

    @FXML
    private TableColumn
            colBrand,
            colModel,
            colFuelType,
            colGearBox,
            colPrice,
            colPassengers,
            colSuitcases;




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

    public void textAreaCarListUpdated(){

        Period.between()

    }

    @Override
    public void filter() {

    }

    @Override
    public void search() {

    }
}
