package se.hkr.Scenes.AddVehicle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import se.hkr.Database.VehicleDB.*;
import se.hkr.Dialogue;
import se.hkr.Model.Vehicle.*;
import se.hkr.Navigator;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddVehicleController implements Initializable {

    @FXML
    private TextArea
            textDescription;

    @FXML
    private Button
            buttonExit,
            buttonSave,
            buttonAdd,
            buttonRemove;

    @FXML
    private TextField
            textModel,
            textYear,
            textPrice;

    @FXML
    private ComboBox
            comboFuelType,
            comboBrand,
            comboGearBox,
            comboPassengers,
            comboSuitcases,
            comboCarType;

    @FXML
    private TableView <VehicleOption>
            tableViewPick,
            tableViewChoose;

    @FXML
    private MenuItem
            menuItemBack,
            menuItemCancel,
            menuItemQuit;

    @FXML
    private TableColumn
            colChooseName,
            colChooseId,
            colName,
            colIID;

    private ObservableList<VehicleOption> allVehicleOptions;
    private ObservableList<VehicleOption> chooseVehicleOptions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (FuelTypeDBHandler fuelTypeDBHandler = new FuelTypeDBHandler();
             GearBoxDBHandler gearBoxDBHandler = new GearBoxDBHandler();
             CarTypeDBHandler carTypeDBHandler = new CarTypeDBHandler();
             VehicleBrandDBHandler vehicleBrandDBHandler = new VehicleBrandDBHandler();
             VehicleOptionDBHandler vehicleOptionDBHandler = new VehicleOptionDBHandler()) {

            ObservableList<FuelType> fuelTypes = FXCollections.observableArrayList(fuelTypeDBHandler.readAll());
            comboFuelType.setItems(fuelTypes);

            ObservableList<GearBox> gearBoxes = FXCollections.observableArrayList(gearBoxDBHandler.readAll());
            comboGearBox.setItems(gearBoxes);

            ObservableList<CarType> carTypes = FXCollections.observableArrayList(carTypeDBHandler.readAll());
            comboCarType.setItems(carTypes);

            ObservableList<VehicleBrand> vehicleBrands = FXCollections.observableArrayList(vehicleBrandDBHandler.readAll());
            comboBrand.setItems(vehicleBrands);

            ObservableList<Integer> suitcases = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);
            comboSuitcases.setItems(suitcases);

            ObservableList<Integer> passengers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);
            comboPassengers.setItems(passengers);

            allVehicleOptions =FXCollections.observableArrayList(vehicleOptionDBHandler.readAll());
            chooseVehicleOptions = FXCollections.observableArrayList();

            colIID.setCellValueFactory(new PropertyValueFactory<VehicleOptionDBHandler, Integer>("id"));

            colName.setCellValueFactory(new PropertyValueFactory<VehicleOptionDBHandler, String>("name"));

            colChooseId.setCellValueFactory(new PropertyValueFactory<VehicleOptionDBHandler, Integer>("id"));

            colChooseName.setCellValueFactory(new PropertyValueFactory<VehicleOptionDBHandler, String>("name"));

            tableViewPick.setItems(allVehicleOptions);

            tableViewChoose.setItems(chooseVehicleOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonSavePressed(ActionEvent ae) {
        if (validate()) {
            try (CarDBHandler carDBHandler = new CarDBHandler();
                VehicleOptionDBHandler vehicleOptionDBHandler = new VehicleOptionDBHandler()) {
                Car car = new Car(Double.parseDouble(textPrice.getText()),
                        textDescription.getText(),
                        ((Integer) comboPassengers.getSelectionModel().getSelectedItem()).intValue(),
                        (FuelType) comboFuelType.getSelectionModel().getSelectedItem(),
                        (GearBox) comboGearBox.getSelectionModel().getSelectedItem(),
                        textModel.getText(),
                        Integer.parseInt(textYear.getText()),
                        (VehicleBrand) comboBrand.getSelectionModel().getSelectedItem(),
                        ((Integer) comboSuitcases.getSelectionModel().getSelectedItem()).intValue(),
                        (CarType) comboCarType.getSelectionModel().getSelectedItem());

                carDBHandler.insert(car);
                for (VehicleOption vehicleOption : chooseVehicleOptions) {
                    vehicleOptionDBHandler.insertVehicleRelation(car, vehicleOption);
                }
                Dialogue.inform("Car was added to the system.");

            } catch (SQLException e) {
                Dialogue.alert("There was a problem while inserting the car into the database, please try again later.");
            } catch (Exception e) {

            }
        }
    }

    private boolean validate() {
        if (comboFuelType.getSelectionModel().isEmpty()) {
            Dialogue.alert("Please select a fuel type");
            return false;
        }
        if (comboBrand.getSelectionModel().isEmpty()) {
            Dialogue.alert("Please select a brand");
            return false;
        }
        if (comboGearBox.getSelectionModel().isEmpty()) {
            Dialogue.alert("Please select a gear box");
            return false;
        }
        if (comboPassengers.getSelectionModel().isEmpty()) {
            Dialogue.alert("Please select amount of passengers");
            return false;
        }
        if (comboSuitcases.getSelectionModel().isEmpty()) {
            Dialogue.alert("Please select amount of suitcases");
            return false;
        }
        if (comboCarType.getSelectionModel().isEmpty()) {
            Dialogue.alert("Please select a car type");
            return false;
        }
        if (textModel.getText().isEmpty()) {
            Dialogue.alert("Please specify a model");
            return false;
        }
        if (textYear.getText().isEmpty()) {
            Dialogue.alert("Please specify a model year");
            return false;
        }
        if (textPrice.getText().isEmpty()) {
            Dialogue.alert("Please specify the price");
            return false;
        }
        return true;
    }

    @FXML
    private void menuItemCancelPressed(ActionEvent ae) {
        Navigator.getInstance().navigateToPanel();
    }

    @FXML
    private void menuItemBackPressed(ActionEvent ae) { Navigator.getInstance().goBack(); }

    @FXML
    private void menuItemQuitPressed(ActionEvent ae) { System.exit(0);}


    @FXML
    private void buttonAddPressed(ActionEvent ae) {
        VehicleOption vehicleOption = tableViewPick.getSelectionModel().getSelectedItem();
        if (vehicleOption != null) {
            chooseVehicleOptions.add(vehicleOption);
            allVehicleOptions.remove(vehicleOption);
        } else {
            Dialogue.alert("please choose a vehicle option");
        }
    }

    @FXML
    private void buttonRemovePressed(ActionEvent ae) {
        VehicleOption vehicleOption = tableViewChoose.getSelectionModel().getSelectedItem();
        if(vehicleOption != null){
            allVehicleOptions.add(vehicleOption);
            chooseVehicleOptions.remove(vehicleOption);
        } else {
            Dialogue.alert("No item to remove");
        }
    }
}

