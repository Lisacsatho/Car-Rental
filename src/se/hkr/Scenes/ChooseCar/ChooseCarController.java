package se.hkr.Scenes.ChooseCar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
            colPrice,
            colBookingBrand,
            colBookingModel;
    @FXML
    private TextField carPrices;

    @FXML
    private ComboBox comboBrand,
            comboCarType,
            comboGearBox,
            comboPassengers;

    @FXML
    private Label lblGearBox,
                  lblFuelType,
                  lblPassengers,
                  lblSuitcases,
                  lblDescription,
                  lblCarName;

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

            colPrice.setCellValueFactory(new PropertyValueFactory<Car, String>("basePrice"));

            colBookingBrand.setCellValueFactory(new PropertyValueFactory<Car, String>("brand"));

            colBookingModel.setCellValueFactory(new PropertyValueFactory<Car, String>("modelName"));

            tblCars.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    showCarInformation(newValue);
                }
            });

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
            if (tblCars.getSelectionModel().getSelectedItem() != null) {
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

            if (tblCars.getSelectionModel().getSelectedItem() != null) {
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

    private void showCarInformation(Vehicle vehicle) {
        final String GEARBOX_PREFIX = "Gear box: ";
        final String FUELTYPE_PREFIX  = "Fuel type box: ";
        final String PASSENGERS_PREFIX = "Passengers: ";
        final String SUITCASES_PREFIX = "Suitcases: ";

        lblCarName.setText(String.format("%s %s", vehicle.getBrand(), vehicle.getModelName()));
        lblGearBox.setText(GEARBOX_PREFIX + vehicle.getGearBox());
        lblFuelType.setText(FUELTYPE_PREFIX + vehicle.getFuelType());
        lblPassengers.setText(PASSENGERS_PREFIX + vehicle.getPassengers());
        lblDescription.setText(vehicle.getDescription());
        if (vehicle instanceof Car) {
            lblSuitcases.setText(SUITCASES_PREFIX + ((Car) vehicle).getSuitcases());
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

    @FXML
    private void buttonCancelBookingPressed(ActionEvent event) {
        BookingSession.getInstance().resetSession();
        Navigator.getInstance().goBack();
    }

    @Override
    public void filter() {

    }

    @Override
    public void search() {

    }
}

