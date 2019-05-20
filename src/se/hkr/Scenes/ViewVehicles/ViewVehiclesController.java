package se.hkr.Scenes.ViewVehicles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import se.hkr.ComboBoxButtonCell;
import se.hkr.Database.VehicleDB.*;
import se.hkr.Dialogue;
import se.hkr.Model.Vehicle.*;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewVehiclesController implements ReadController<Vehicle>, Initializable {
    @FXML
    private ComboBox comboFuelType,
                     comboGearBox,
                     comboBrand,
                     comboCarType;

    @FXML
    private TableView<Vehicle> tblVehicles;

    @FXML
    private TableColumn colBrand,
                        colModel,
                        colModelYear,
                        colPrice,
                        colPassengers,
                        colSuitcases;

    @FXML
    private TextField txtFldSearch;

    @FXML
    private Label lblVehicleName,
                  lblFuelType,
                  lblGearBox;
    @FXML
    private TextField txtFldPriceFrom;

    @FXML
    private CheckBox checkBoxShowInactive,
                     checkBoxShowInspected,
                     checkBoxReadyRent;

    private ObservableList<Vehicle> matchingVehicles;
    private List<Vehicle> allVehicles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (FuelTypeDBHandler fuelTypeDBHandler = new FuelTypeDBHandler();
             GearBoxDBHandler gearBoxDBHandler = new GearBoxDBHandler();
             CarTypeDBHandler carTypeDBHandler = new CarTypeDBHandler();
             VehicleBrandDBHandler vehicleBrandDBHandler = new VehicleBrandDBHandler()) {

            updateList();

            colBrand.setCellValueFactory(
                    new PropertyValueFactory<Car, String>("brand")
            );
            colModel.setCellValueFactory(
                    new PropertyValueFactory<Car, String>("modelName")
            );
            colModelYear.setCellValueFactory(
                    new PropertyValueFactory<Car, Integer>("modelYear")
            );
            colPassengers.setCellValueFactory(
                    new PropertyValueFactory<Car, Integer>("passengers")
            );
            colPrice.setCellValueFactory(
                    new PropertyValueFactory<Car, Double>("basePrice")
            );
            colSuitcases.setCellValueFactory(
                    new PropertyValueFactory<Car, Integer>("suitcases")
            );

            ObservableList<FuelType> fuelTypes = FXCollections.observableArrayList(fuelTypeDBHandler.readAll());
            comboFuelType.setItems(fuelTypes);
            comboFuelType.setButtonCell(new ComboBoxButtonCell("Fuel type"));

            ObservableList<GearBox> gearBoxes = FXCollections.observableArrayList(gearBoxDBHandler.readAll());
            comboGearBox.setItems(gearBoxes);
            comboGearBox.setButtonCell(new ComboBoxButtonCell("Gear box"));

            ObservableList<CarType> carTypes = FXCollections.observableArrayList(carTypeDBHandler.readAll());
            comboCarType.setItems(carTypes);
            comboCarType.setButtonCell(new ComboBoxButtonCell("Car type"));

            ObservableList<VehicleBrand> vehicleBrands = FXCollections.observableArrayList(vehicleBrandDBHandler.readAll());
            comboBrand.setItems(vehicleBrands);
            comboBrand.setButtonCell(new ComboBoxButtonCell("Brand"));

            FilteredList<Vehicle> filteredData = new FilteredList<>(matchingVehicles, c -> true);
            comboBrand.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(car -> filter(car));
            });
            comboCarType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(car -> filter(car));
            });
            comboFuelType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(car -> filter(car));
            });
            comboGearBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(car -> filter(car));
            });

            SortedList<Vehicle> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblVehicles.comparatorProperty());
            tblVehicles.setItems(sortedData);

            tblVehicles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> displayVehicle(newValue));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateList() {
        try {
            if (checkBoxShowInspected.isSelected()) {
                allVehicles = FXCollections.observableArrayList(VehicleDBHandler.readAbstractAllIncludingInactive());
            } else {
                allVehicles = FXCollections.observableArrayList(VehicleDBHandler.readAbstractAll());}

                if (checkBoxShowInactive.isSelected()) {
                    allVehicles = FXCollections.observableArrayList(VehicleDBHandler.readAbstractAllIncludingInactive());
                } else {
                    allVehicles = FXCollections.observableArrayList(VehicleDBHandler.readAbstractAll());
                }
                if (matchingVehicles == null) {
                    matchingVehicles = FXCollections.observableArrayList();
                    matchingVehicles.addAll(allVehicles);
                } else {
                    matchingVehicles.clear();
                    matchingVehicles.addAll(allVehicles);
                }

            } catch(SQLException e){
                Dialogue.alert(e.getMessage());
            }
        }

    private void resetDisplay() {
        tblVehicles.getSelectionModel().clearSelection();
        checkBoxReadyRent.setSelected(false);
        lblVehicleName.setText("Vehicle name");
        lblFuelType.setText("Fuel type: ");
        lblGearBox.setText("Gear box: ");
        txtFldPriceFrom.clear();
    }

    private void displayVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            checkBoxReadyRent.setSelected(vehicle.isReadyForRent());
            lblVehicleName.setText(String.format("%s %s, %d", vehicle.getBrand().getName(), vehicle.getModelName(), vehicle.getModelYear()));
            lblFuelType.setText(String.format("Fuel type: %s", vehicle.getFuelType().getName()));
            lblGearBox.setText(String.format("Gear box: %s", vehicle.getGearBox().getName()));
            txtFldPriceFrom.setText(Double.toString(vehicle.getBasePrice()));
        }
    }

    @FXML
    private void buttonSavePressed() {
        Vehicle vehicle = tblVehicles.getSelectionModel().getSelectedItem();
        if (vehicle != null) {
            try (VehicleDBHandler vehicleDBHandler = VehicleDBHandler.getHandlerFor(vehicle)) {
                vehicle.setBasePrice(Double.parseDouble(txtFldPriceFrom.getText()));
                vehicle.setReadyForRent(checkBoxReadyRent.isSelected());
                vehicleDBHandler.update(vehicle);
                Dialogue.inform("Vehicle was updated!");
                resetDisplay();
                updateList();
            } catch (SQLException e) {
                Dialogue.alert(e.getMessage());
            } catch (NumberFormatException e) {
                Dialogue.alert("Enter a valid value for total price.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    private void buttonInactivatePressed() {
        Vehicle vehicle = tblVehicles.getSelectionModel().getSelectedItem();
        if (vehicle != null) {
            if (Dialogue.alertOk("Are you sure you want to inactivate " + vehicle.getModelName() + "?")) {
                try (VehicleDBHandler vehicleDBHandler = VehicleDBHandler.getHandlerFor(vehicle)) {
                    vehicleDBHandler.inactivate(vehicle);
                    Dialogue.inform("Vehicle has been inactivated.");
                    resetDisplay();
                    updateList();
                } catch (SQLException e) {
                    Dialogue.alert(e.getMessage());
                } catch (Exception e) {
                    Dialogue.alert(e.getMessage());
                }
            }
        } else {
            Dialogue.alert("Please choose a vehicle to inactivate.");
        }
    }

    @FXML
    private void btnInspectNeedPressed(){
        Vehicle vehicle = tblVehicles.getSelectionModel().getSelectedItem();
        if (vehicle != null) {
            if (Dialogue.alertOk("Vehicle needed for inspection is " + vehicle.getModelName() + "?")) {
                try (VehicleDBHandler vehicleDBHandler = VehicleDBHandler.getHandlerFor(vehicle)) {
                    vehicleDBHandler.inactivate(vehicle);
                    Dialogue.inform("Vehicle has been sent to inspection.");
                    resetDisplay();
                    updateList();
                } catch (SQLException e) {
                    Dialogue.alert(e.getMessage());
                } catch (Exception e) {
                    Dialogue.alert(e.getMessage());
                }
            }
        } else {
            Dialogue.alert("Please choose a vehicle to send to inspection.");
        }
    }

    @FXML
    private void btnResetFilterPressed() {
        comboBrand.getSelectionModel().clearSelection();
        comboCarType.getSelectionModel().clearSelection();
        comboFuelType.getSelectionModel().clearSelection();
        comboGearBox.getSelectionModel().clearSelection();
        search();
    }

    @Override
    public boolean filter(Vehicle model) {
        if (!comboGearBox.getSelectionModel().isEmpty()) {
            if (model.getGearBox().getId() != ((GearBox)comboGearBox.getSelectionModel().getSelectedItem()).getId()) {
                return false;
            }
        }
        if (!comboFuelType.getSelectionModel().isEmpty()) {
            if (model.getFuelType().getId() != ((FuelType) comboFuelType.getSelectionModel().getSelectedItem()).getId()) {
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
        resetDisplay();
        String key = txtFldSearch.getText();
        matchingVehicles.clear();
        if (key.equals("")) {
            matchingVehicles.addAll(allVehicles);
        } else {
            for (Vehicle vehicle : allVehicles) {
                if (vehicle.matches(key)) {
                    matchingVehicles.add(vehicle);
                }
            }
        }
    }
}
