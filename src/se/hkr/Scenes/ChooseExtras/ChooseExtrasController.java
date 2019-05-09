package se.hkr.Scenes.ChooseExtras;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import se.hkr.BookingSession;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseExtrasController implements ReadController, Initializable {

    @FXML
    private TableView <VehicleOption> tblOptions;

    @FXML
    private TableColumn colCar,
                        colName,
                        colPrice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCar.setCellValueFactory(new PropertyValueFactory<Car,String >("modelName"));
        colName.setCellValueFactory(new PropertyValueFactory<VehicleOption, String>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<VehicleOption, Double>("price"));

        ObservableList<VehicleOption> data = FXCollections.observableArrayList();
    }

    @Override
    public void filter() {
    }
    @Override
    public void search() {
    }
}