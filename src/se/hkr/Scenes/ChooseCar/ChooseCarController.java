package se.hkr.Scenes.ChooseCar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import se.hkr.BookingSession;
import se.hkr.ComboBoxButtonCell;
import se.hkr.Database.VehicleDB.CarTypeDBHandler;
import se.hkr.Database.VehicleDB.GearBoxDBHandler;
import se.hkr.Database.VehicleDB.VehicleBrandDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.Vehicle.*;
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
    @FXML
    private Button btnResetFilter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Date startDate = BookingSession.getInstance().getBooking().getStartDate();
        Date endDate = BookingSession.getInstance().getBooking().getEndDate();

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

        showComboData();
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
            Dialogue.alert("Something went wrong. Check the information.");
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
            Dialogue.alert("Please check your information. Something went wrong.");
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

            comboGearBox.setButtonCell(new ComboBoxButtonCell("Gear box"));
            comboBrand.setButtonCell(new ComboBoxButtonCell("Brand"));
            comboPassengers.setButtonCell(new ComboBoxButtonCell("Passengers"));
            comboCarType.setButtonCell(new ComboBoxButtonCell("Vehicle type"));


        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @Override
    public boolean filter(Vehicle model) {
        if (!comboGearBox.getSelectionModel().isEmpty()) {
            if (model.getGearBox().getId() != ((GearBox) comboGearBox.getSelectionModel().getSelectedItem()).getId()) {
                return false;
            }
        }
        if (!comboPassengers.getSelectionModel().isEmpty()) {
            if (model.getPassengers() != ((Integer) comboPassengers.getSelectionModel().getSelectedItem())) {
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

    public void resetFilter(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnResetFilter)
            comboGearBox.getSelectionModel().clearSelection();
            comboBrand.getSelectionModel().clearSelection();
            comboPassengers.getSelectionModel().clearSelection();
            comboCarType.getSelectionModel().clearSelection();
    }
    @Override
    public void search() {

    }
}

