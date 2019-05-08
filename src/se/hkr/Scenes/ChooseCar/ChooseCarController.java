package se.hkr.Scenes.ChooseCar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import se.hkr.BookingSession;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Navigator;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class ChooseCarController implements ReadController, Initializable {

    private ObservableList<Car> data;
    private ObservableList<Car> bookedCars;

    @FXML
    private TableView<Car> tblCars, tblBookedCars;


    @FXML
    private TableColumn colBrand,
            colModel,
            colFuelType,
            colGearBox,
            colPrice,
            colPassengers,
            colSuitcases,
            colBookingBrand,
            colBookingModel;
    @FXML
    private TextField carPrices;
    @FXML
    private ComboBox comboBrand,
            comboCarType,
            comboGearBox,
            comboPassengers;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Date startDate = BookingSession.getInstance().getBooking().getStartDate();
        Date endDate = BookingSession.getInstance().getBooking().getEndDate();

        try {
            data = FXCollections.observableArrayList((List<Car>) VehicleDBHandler.readAvailableVehicles(startDate, endDate));
            bookedCars = FXCollections.observableArrayList();
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

            colBookingBrand.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));

            colBookingModel.setCellValueFactory(new PropertyValueFactory<Car, String>("modelName"));

            tblCars.setItems(data);
            tblBookedCars.setItems(bookedCars);

        } catch (SQLException e) {
            // Placeholder
            System.out.println("Database interaction failed, please try again later.");
        }
    }


    public void bookPressed() {
        Car car = tblCars.getSelectionModel().getSelectedItem();

        try {
            if (!tblCars.getSelectionModel().isEmpty()) {
                data.remove(car);
                bookedCars.add(car);
                calculateTotalPrice();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void removeBookedCar() {

        Car car = tblBookedCars.getSelectionModel().getSelectedItem();

        try {

            if (!tblCars.getSelectionModel().isEmpty()) {
                bookedCars.remove(car);
                data.add(car);
                calculateTotalPrice();
            }

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void calculateTotalPrice() {

        try {
            Date startDate = BookingSession.getInstance().getBooking().getStartDate();
            Date endDate = BookingSession.getInstance().getBooking().getEndDate();
            long diff = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            double basePrices = 0.0;
            for (Car car : bookedCars) {
                basePrices += car.getBasePrice();
            }
            carPrices.setText("$" + Double.toString(basePrices * days));
        } catch (Exception x) {
            x.printStackTrace();
        }

    }

    @FXML
    private void buttonNextPressed(ActionEvent event) {
        if (!bookedCars.isEmpty()) {
            BookingSession.getInstance().getBooking().setVehicles(new ArrayList<>(bookedCars));
            Navigator.getInstance().navigateTo("ChooseExtras/ChooseExtrasView.fxml");
        } else {
            Dialogue.alert("Please choose at least one car to book.");
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public void search() {

    }
}

