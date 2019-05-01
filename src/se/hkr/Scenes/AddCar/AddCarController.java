package se.hkr.Scenes.AddCar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import se.hkr.Database.VehicleDB.CarTypeDBHandler;
import se.hkr.Database.VehicleDB.FuelTypeDBHandler;
import se.hkr.Database.VehicleDB.GearBoxDBHandler;
import se.hkr.Database.VehicleDB.VehicleBrandDBHandler;
import se.hkr.Model.Vehicle.CarType;
import se.hkr.Model.Vehicle.FuelType;
import se.hkr.Model.Vehicle.GearBox;
import se.hkr.Model.Vehicle.VehicleBrand;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCarController implements Initializable {

    @FXML
    private Button buttonExit, buttonSave;

    @FXML
    private TextField textModel, textYear, textPrice;

    @FXML
    private ComboBox comboFuelType,
            comboBrand,
            comboGearBox,
            comboPassengers,
            comboSuitcases,
            comboCarType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (FuelTypeDBHandler fuelTypeDBHandler = new FuelTypeDBHandler();
             GearBoxDBHandler gearBoxDBHandler = new GearBoxDBHandler();
             CarTypeDBHandler carTypeDBHandler = new CarTypeDBHandler();
             VehicleBrandDBHandler vehicleBrandDBHandler = new VehicleBrandDBHandler()) {

            ObservableList<FuelType> fuelTypes = FXCollections.observableArrayList(fuelTypeDBHandler.readAll());
            comboFuelType.setItems(fuelTypes);

            ObservableList<GearBox> gearBoxes = FXCollections.observableArrayList(gearBoxDBHandler.readAll());
            comboGearBox.setItems(gearBoxes);

            ObservableList<CarType> carTypes = FXCollections.observableArrayList(carTypeDBHandler.readAll());
            comboCarType.setItems(carTypes);

            ObservableList<VehicleBrand> vehicleBrands = FXCollections.observableArrayList(vehicleBrandDBHandler.readAll());
            comboBrand.setItems(vehicleBrands);

            ObservableList<Integer> suitcases= FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);
            comboSuitcases.setItems(suitcases);

            ObservableList<Integer> passengers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);
            comboPassengers.setItems(passengers);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buttonAddPressed (ActionEvent e) {

    }


}


