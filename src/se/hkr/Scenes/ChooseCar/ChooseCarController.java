package se.hkr.Scenes.ChooseCar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import se.hkr.BookingSession;
import se.hkr.Database.VehicleDB.CarTypeDBHandler;
import se.hkr.Database.VehicleDB.GearBoxDBHandler;
import se.hkr.Database.VehicleDB.VehicleBrandDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.*;
import se.hkr.Navigator;
import se.hkr.Scenes.ReadController;
import se.hkr.Scenes.SessionListener;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class ChooseCarController implements ReadController<Vehicle>, Initializable, SessionListener<BookingSession> {

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
    private TextField txtFldTotalPrice;

    @FXML
    private ComboBox
            comboGearBox,
            comboBrand,
            comboPassengers,
            comboCarType;

    @FXML
    private Label lblGearBox,
            lblFuelType,
            lblPassengers,
            lblSuitcases,
            lblDescription,
            lblCarName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date startDate = BookingSession.getInstance().getSessionObject().getStartDate();
        Date endDate = BookingSession.getInstance().getSessionObject().getEndDate();
        BookingSession.getInstance().addListener(this);

        try {
            data = FXCollections.observableArrayList(VehicleDBHandler.readAvailableVehicles(startDate, endDate));
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

            tblBookedVehicles.setItems(bookedVehicles);
        } catch (SQLException e) {
            // Placeholder
            System.out.println("Database interaction failed, please try again later.");
        }
        txtFldTotalPrice.setText("$" + calculateTotalPrice());
        showComboData();
    }

    @Override
    public void update() {
        txtFldTotalPrice.setText("$" + calculateTotalPrice());
    }

    public void bookPressed() {
        Vehicle vehicle = tblAvailableVehicles.getSelectionModel().getSelectedItem();
        try {
            if (tblAvailableVehicles.getSelectionModel().getSelectedItem() != null) {
                data.remove(vehicle);
                bookedVehicles.add(vehicle);
                BookingSession.getInstance().getSessionObject().setVehicles(bookedVehicles);
                txtFldTotalPrice.setText("$" + calculateTotalPrice());
            }
        } catch (Exception x) {
            Dialogue.alert("Something went wrong. Check the information.");
        }
    }

    public void removeBookedCar() {
        try {
            if (tblBookedVehicles.getSelectionModel().getSelectedItem() != null) {
                Vehicle vehicle = tblBookedVehicles.getSelectionModel().getSelectedItem();
                bookedVehicles.remove(vehicle);
                data.add(vehicle);
                BookingSession.getInstance().getSessionObject().setVehicles(bookedVehicles);
                List<Pair<Vehicle, VehicleOption>> vehicleOptionsToRemove = new ArrayList<>();
                for (Pair<Vehicle, VehicleOption> pair : BookingSession.getInstance().getSessionObject().getVehicleOptions()) {
                    if (pair.getKey().getId() == vehicle.getId()) {
                        vehicleOptionsToRemove.add(pair);
                    }
                }
                BookingSession.getInstance().getSessionObject().getVehicleOptions().removeAll(vehicleOptionsToRemove);
                txtFldTotalPrice.setText("$" + calculateTotalPrice());
            }
        } catch (Exception x) {
            Dialogue.alert("Please check your information. Something went wrong.");
        }
    }

    private double calculateTotalPrice() {
        double startingPrice = 0.0;
        try {
            Date startDate = BookingSession.getInstance().getSessionObject().getStartDate();
            Date endDate = BookingSession.getInstance().getSessionObject().getEndDate();
            long diff = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Booking booking = BookingSession.getInstance().getSessionObject();
            if (booking != null && booking.getVehicles() != null) {
                for (Vehicle vehicle : booking.getVehicles()) {
                    startingPrice += vehicle.getBasePrice() * days;
                }
                if (booking.getVehicleOptions() != null) {
                    for (Pair<Vehicle, VehicleOption> vehicleOption : booking.getVehicleOptions()) {
                        startingPrice += vehicleOption.getValue().getPrice() * days;
                    }
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
            Dialogue.alert("Something went wrong, please try again.");
        }
        System.out.println("Total price: " + startingPrice);
        return startingPrice;
    }

    private void showCarInformation(Vehicle vehicle) {
        final String GEARBOX_PREFIX = "Gear box: ";
        final String FUELTYPE_PREFIX = "Fuel type box: ";
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
            BookingSession.getInstance().getSessionObject().setVehicles(bookedVehicles);
            BookingSession.getInstance().getSessionObject().setTotalPrice(calculateTotalPrice());
            BookingSession.getInstance().notifyListeners();
            Navigator.getInstance().navigateTo("ChooseExtras/ChooseExtrasView.fxml");
        } else {
            Dialogue.alert("Please choose at least one car to book.");
        }
    }

    @FXML
    private void buttonCancelBookingPressed(ActionEvent event) {
        BookingSession.getInstance().resetSession();
        Navigator.getInstance().navigateToPanel();
    }


    public void showComboData() {

        try (GearBoxDBHandler gearBoxDBHandler = new GearBoxDBHandler(); VehicleBrandDBHandler vehicleBrandDBHandler = new VehicleBrandDBHandler(); CarTypeDBHandler carTypeDBHandler = new CarTypeDBHandler()) {

            ObservableList<GearBox> gearBoxes = FXCollections.observableArrayList(gearBoxDBHandler.readAll());
            comboGearBox.setItems(gearBoxes);
            ObservableList<VehicleBrand> vehicleBrands = FXCollections.observableArrayList(vehicleBrandDBHandler.readAll());
            comboBrand.setItems(vehicleBrands);
            ObservableList<Integer> passengers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7);
            comboPassengers.setItems(passengers);
            ObservableList<CarType> carTypes = FXCollections.observableArrayList(carTypeDBHandler.readAll());
            comboCarType.setItems(carTypes);

            FilteredList<Vehicle> vehicles = new FilteredList<>(data, c -> true);
            comboGearBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> vehicles.setPredicate(vehicle -> filter(vehicle)));

            comboBrand.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> vehicles.setPredicate(vehicle -> filter(vehicle)));

            comboPassengers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> vehicles.setPredicate(vehicle -> filter(vehicle)));

            comboCarType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> vehicles.setPredicate(vehicle -> filter(vehicle)));

            SortedList<Vehicle> sortedData = new SortedList(vehicles);
            sortedData.comparatorProperty().bind(tblAvailableVehicles.comparatorProperty());
            tblAvailableVehicles.setItems(sortedData);

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @Override
    public boolean filter(Vehicle model) {
        if (!comboGearBox.getSelectionModel().isEmpty()) {
            if (model.getGearBox().getId() != ((GearBox)comboGearBox.getSelectionModel().getSelectedItem()).getId()) {
                return false;
            }
        }
        if (!comboPassengers.getSelectionModel().isEmpty()) {
            if (model.getPassengers() != ((Integer)comboPassengers.getSelectionModel().getSelectedItem())) {
                return false;
            }
        }
        if (!comboCarType.getSelectionModel().isEmpty()) {
            if (model instanceof Car) {
                if (((Car) model).getCarType().getId() != ((CarType) comboCarType.getSelectionModel().getSelectedItem()).getId()) {
                    return false;
                }
            }
        }
        if (!comboBrand.getSelectionModel().isEmpty()) {
            if (model.getBrand().getId() != ((VehicleBrand) comboBrand.getSelectionModel().getSelectedItem()).getId()) {
                return false;
            }
        }
        return true;

    }

    @Override
    public void search() {

    }
}

