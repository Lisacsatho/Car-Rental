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
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class ChooseCarController implements ReadController<Vehicle>, Initializable {

    private ObservableList<Vehicle> data;
    private ObservableList<Vehicle> bookedVehicles;

    @FXML
    private TableView<Vehicle> tblAvailableVehicles,
                               tblBookedVehicles;

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
            data = FXCollections.observableArrayList(VehicleDBHandler.readAvailableVehicles(startDate, endDate));
            System.out.println(data);
            bookedVehicles = FXCollections.observableArrayList();

            colBrand.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("brand"));
            colModel.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("modelName"));
            colPrice.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("basePrice"));
            colBookingBrand.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("brand"));
            colBookingModel.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("modelName"));

            tblAvailableVehicles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    showCarInformation(newValue);
                }
            });

            tblAvailableVehicles.setItems(data);
            tblBookedVehicles.setItems(bookedVehicles);
        } catch (SQLException e) {
            // Placeholder
            System.out.println("Database interaction failed, please try again later.");
        }
    }

    public void bookPressed() {
        Vehicle vehicle = tblAvailableVehicles.getSelectionModel().getSelectedItem();
        try {
            if (tblAvailableVehicles.getSelectionModel().getSelectedItem() != null) {
                data.remove(vehicle);
                bookedVehicles.add(vehicle);
                BookingSession.getInstance().getBooking().setVehicles(bookedVehicles);
                carPrices.setText("$" + calculateTotalPrice());
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void removeBookedCar() {
        try {
            if (tblBookedVehicles.getSelectionModel().getSelectedItem() != null) {
                Vehicle vehicle = tblBookedVehicles.getSelectionModel().getSelectedItem();
                bookedVehicles.remove(vehicle);
                data.add(vehicle);
                BookingSession.getInstance().getBooking().setVehicles(bookedVehicles);
                carPrices.setText("$" + calculateTotalPrice());
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public double calculateTotalPrice() {
        try {
            Date startDate = BookingSession.getInstance().getBooking().getStartDate();
            Date endDate = BookingSession.getInstance().getBooking().getEndDate();
            long diff = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            double basePrices = 0.0;
            for (Vehicle vehicle : bookedVehicles) {
                basePrices += vehicle.getBasePrice();
            }
            return basePrices * days;
        } catch (Exception x) {
            Dialogue.alert("Something went wrong, please try again.");
        }
        return 0.0;
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
        if (!bookedVehicles.isEmpty()) {
            BookingSession.getInstance().getBooking().setVehicles(bookedVehicles);
            BookingSession.getInstance().getBooking().setTotalPrice(calculateTotalPrice());
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
    public boolean filter(Vehicle model) {
        return false;
    }

    @Override
    public void search() {

    }
}

