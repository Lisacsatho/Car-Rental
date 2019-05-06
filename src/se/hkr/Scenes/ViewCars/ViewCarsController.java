package se.hkr.Scenes.ViewCars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import se.hkr.Database.VehicleDB.*;
import se.hkr.Model.Vehicle.*;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewCarsController implements ReadController, Initializable {
    @FXML
    private ComboBox
                    comboFuelType,
                    comboGearBox,
                    comboBrand,
                    comboCarType;

    @FXML
    private TableView tblCars;

    @FXML
    private TableColumn
                        colBrand,
                        colModel,
                        colModelYear,
                        colFuelType,
                        colGearBox,
                        colPrice,
                        colPassengers,
                        colSuitcases;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (CarDBHandler db = new CarDBHandler();
             FuelTypeDBHandler fuelTypeDBHandler = new FuelTypeDBHandler();
             GearBoxDBHandler gearBoxDBHandler = new GearBoxDBHandler();
             CarTypeDBHandler carTypeDBHandler = new CarTypeDBHandler();
             VehicleBrandDBHandler vehicleBrandDBHandler = new VehicleBrandDBHandler()) {

            ObservableList<Car> data = FXCollections.observableArrayList(db.readAll());
            colBrand.setCellValueFactory(
                    new PropertyValueFactory<Car, String>("brand")
            );
            colModel.setCellValueFactory(
                    new PropertyValueFactory<Car, String>("modelName")
            );
            colFuelType.setCellValueFactory(
                    new PropertyValueFactory<Car, String>("fuelType")
            );
            colGearBox.setCellValueFactory(
                    new PropertyValueFactory<Car, String>("gearBox")
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
            tblCars.setItems(data);

            ObservableList<FuelType> fuelTypes = FXCollections.observableArrayList(fuelTypeDBHandler.readAll());
            comboFuelType.setItems(fuelTypes);

            ObservableList<GearBox> gearBoxes = FXCollections.observableArrayList(gearBoxDBHandler.readAll());
            comboGearBox.setItems(gearBoxes);

            ObservableList<CarType> carTypes = FXCollections.observableArrayList(carTypeDBHandler.readAll());
            comboCarType.setItems(carTypes);

            ObservableList<VehicleBrand> vehicleBrands = FXCollections.observableArrayList(vehicleBrandDBHandler.readAll());
            comboBrand.setItems(vehicleBrands);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void filter() {


    }

    @Override
    public void search() {

    }
}
