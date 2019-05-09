package se.hkr.Scenes.ViewCars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

public class ViewCarsController implements ReadController<Vehicle>, Initializable {
    @FXML
    private ComboBox comboFuelType,
                     comboGearBox,
                     comboBrand,
                     comboCarType;

    @FXML
    private TableView tblCars;

    @FXML
    private TableColumn colBrand,
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

            ObservableList<FuelType> fuelTypes = FXCollections.observableArrayList(fuelTypeDBHandler.readAll());
            comboFuelType.setItems(fuelTypes);

            ObservableList<GearBox> gearBoxes = FXCollections.observableArrayList(gearBoxDBHandler.readAll());
            comboGearBox.setItems(gearBoxes);

            ObservableList<CarType> carTypes = FXCollections.observableArrayList(carTypeDBHandler.readAll());
            comboCarType.setItems(carTypes);

            ObservableList<VehicleBrand> vehicleBrands = FXCollections.observableArrayList(vehicleBrandDBHandler.readAll());
            comboBrand.setItems(vehicleBrands);

            FilteredList<Car> filteredData = new FilteredList<>(data, c -> true);
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

            SortedList<Car> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblCars.comparatorProperty());
            tblCars.setItems(sortedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }
}
