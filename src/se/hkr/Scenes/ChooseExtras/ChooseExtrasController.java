package se.hkr.Scenes.ChooseExtras;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Pair;
import se.hkr.BookingSession;
import se.hkr.Dialogue;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;
import se.hkr.Navigator;
import se.hkr.Scenes.ReadController;
import se.hkr.Scenes.SessionListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ChooseExtrasController implements ReadController<VehicleOption>, Initializable, SessionListener<BookingSession> {

    @FXML
    private TableView <Pair<Vehicle, VehicleOption>>
            tblOptions,
            tblBookedOptions;

    @FXML
    private TableColumn
            colCar,
            colName,
            colPrice,
            colBookingName,
            colBookingCar;
    @FXML
    private TextField txtFldPrice;

    @FXML
    private MenuItem
            menuItemAbout,
            menuItemContact;

    private ObservableList<Pair<Vehicle, VehicleOption>> vehicleOptions;
    private ObservableList<Pair<Vehicle, VehicleOption>> bookedVehicleOptions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BookingSession.getInstance().addListener(this);
        txtFldPrice.setText("$" + Double.toString(BookingSession.getInstance().getSessionObject().getTotalPrice()));
        colCar.setCellValueFactory(new PropertyValueFactory<Pair<Vehicle, VehicleOption>, String>("key"));
        colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Pair<Vehicle, VehicleOption>) param.getValue()).getValue().getName());
            }
        });
        colPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleDoubleProperty(((Pair<Vehicle, VehicleOption>) param.getValue()).getValue().getPrice());
            }
        });
        colBookingCar.setCellValueFactory(new PropertyValueFactory<Pair<Vehicle, VehicleOption>, String>("key"));

        colBookingName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Pair<Vehicle, VehicleOption>) param.getValue()).getValue().getName());
            }
        });

        vehicleOptions = FXCollections.observableArrayList();
        bookedVehicleOptions = FXCollections.observableArrayList();
        BookingSession.getInstance().getSessionObject().getVehicles().forEach((vehicle -> {
            vehicle.getVehicleOptions().forEach((vehicleOption -> {
                vehicleOptions.add(new Pair<Vehicle, VehicleOption>(vehicle, vehicleOption));
            }));
        }));
        if (BookingSession.getInstance().getSessionObject().getVehicleOptions() != null) {
            initializeEarlierBookings();
        }
        tblOptions.setItems(vehicleOptions);
        tblBookedOptions.setItems(bookedVehicleOptions);
    }

    @Override
    public void update() {
        txtFldPrice.setText("$" + calculateTotalPrice());
    }

    private void initializeEarlierBookings() {
        BookingSession.getInstance().getSessionObject().getVehicleOptions().forEach((pair) -> {
            vehicleOptions.remove(pair);
            bookedVehicleOptions.add(pair);
        });
    }

    @FXML
    private void buttonChoosePressed(ActionEvent event) {
        if (tblOptions.getSelectionModel().getSelectedItem() != null) {
            Pair<Vehicle, VehicleOption> vehicleOption = tblOptions.getSelectionModel().getSelectedItem();
            vehicleOptions.remove(vehicleOption);
            bookedVehicleOptions.add(vehicleOption);
            BookingSession.getInstance().getSessionObject().setVehicleOptions(bookedVehicleOptions);
            txtFldPrice.setText("$" + calculateTotalPrice());
        } else {
            Dialogue.alert("Please choose an additional option.");
        }
    }

    @FXML
    private void buttonRemovePressed(ActionEvent event) {
        if (tblBookedOptions.getSelectionModel().getSelectedItem() != null) {
            Pair<Vehicle, VehicleOption> vehicleOption = tblBookedOptions.getSelectionModel().getSelectedItem();
            bookedVehicleOptions.remove(vehicleOption);
            vehicleOptions.add(vehicleOption);
            BookingSession.getInstance().getSessionObject().setVehicleOptions(bookedVehicleOptions);
            txtFldPrice.setText("$" + calculateTotalPrice());
        } else {
            Dialogue.alert("Please choose an addition option to remove.");
        }
    }

    private double calculateTotalPrice() {
        double startingPrice = 0.0;
        try {
            Date startDate = BookingSession.getInstance().getSessionObject().getStartDate();
            Date endDate = BookingSession.getInstance().getSessionObject().getEndDate();
            long diff = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            for (Vehicle vehicle : BookingSession.getInstance().getSessionObject().getVehicles()) {
                startingPrice += vehicle.getBasePrice() * days;
            }
            for (Pair<Vehicle, VehicleOption> vehicleOption : bookedVehicleOptions) {
                startingPrice += vehicleOption.getValue().getPrice() * days;
            }
        } catch (Exception x) {
            Dialogue.alert("Something went wrong, please try again.");
        }
        return startingPrice;
    }

    @FXML
    private void buttonProceedPressed() {
        BookingSession.getInstance().getSessionObject().setVehicleOptions(bookedVehicleOptions);
        BookingSession.getInstance().getSessionObject().setTotalPrice(calculateTotalPrice());
        BookingSession.getInstance().notifyListeners();
        Navigator.getInstance().navigateTo("ConfirmBooking/ConfirmBookingView.fxml");
    }

    @FXML
    private void buttonBackPressed() {
        BookingSession.getInstance().getSessionObject().setVehicleOptions(bookedVehicleOptions);
        BookingSession.getInstance().notifyListeners();
        Navigator.getInstance().goBack();
    }

    @FXML
    private void buttonCancelPressed() {
        BookingSession.getInstance().resetSession();
        Navigator.getInstance().navigateToPanel();
    }

    @FXML
    public void menuItemContactPressed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemContact) {

            Navigator.getInstance().navigateTo("CustomerService/CustomerServiceView.fxml");
        }
    }

    @FXML
    public void menuItemAboutPressed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemAbout) {

            Navigator.getInstance().navigateTo("CompanyInformation/CompanyInformationView.fxml");

        }
    }

    @Override
    public boolean filter(VehicleOption model) {
        return false;
    }

    @Override
    public void search() {
    }
}