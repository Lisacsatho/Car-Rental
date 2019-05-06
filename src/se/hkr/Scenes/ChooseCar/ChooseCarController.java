package se.hkr.Scenes.ChooseCar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import se.hkr.BookingSession;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class ChooseCarController implements ReadController, Initializable {

    @FXML
    TableView tblCars;

    @FXML

    private Button book;

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
            ObservableList<Vehicle> data = FXCollections.observableArrayList(VehicleDBHandler.readAvailableVehicles(startDate, endDate));
            colBrand.setCellValueFactory(
                    new PropertyValueFactory<Car, String>("brand")
            );
            colModel.setCellValueFactory(
                    new PropertyValueFactory<Car, String>("modelName")
            );
            colFuelType.setCellValueFactory(new PropertyValueFactory<Car, String>("fuelType"));

            colGearBox.setCellValueFactory(new PropertyValueFactory<Car, String>("gearBox"));

            colPrice.setCellValueFactory(new PropertyValueFactory<Car, String>("basePrice"));

            colPassengers.setCellValueFactory(new PropertyValueFactory<Car, String>("passengers"));

            colSuitcases.setCellValueFactory(new PropertyValueFactory<Car, String>("suitcases"));

            tblCars.setItems(data);

        } catch (SQLException e) {
            // Placeholder
            System.out.println("Database interaction failed, please try again later.");
        }
    }


    public void bookPressed (ActionEvent event) {}

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

