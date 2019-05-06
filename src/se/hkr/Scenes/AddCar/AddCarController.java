package se.hkr.Scenes.AddCar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import se.hkr.Database.VehicleDB.*;
import se.hkr.Model.Vehicle.*;
import se.hkr.Navigator;
import java.io.CharArrayReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCarController implements Initializable {

    @FXML
    private TextArea
            textDescription;

    @FXML
    private Button
            buttonExit,
            buttonSave;

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
    @FXML
    private void buttonAddPressed (ActionEvent ae) {
        if(validate()) {
           try (CarDBHandler carDBHandler = new CarDBHandler()) {
               Car car = new Car(Double.parseDouble(textPrice.getText()),textDescription.getText(),((Integer) comboPassengers.getSelectionModel().getSelectedItem()).intValue(),
                                (FuelType) comboFuelType.getSelectionModel().getSelectedItem(), (GearBox) comboGearBox.getSelectionModel().getSelectedItem(), textModel.getText(),
                                Integer.parseInt(textYear.getText()),(VehicleBrand) comboBrand.getSelectionModel().getSelectedItem(), ((Integer)comboSuitcases.getSelectionModel().getSelectedItem()).intValue(),
                                (CarType)comboCarType.getSelectionModel().getSelectedItem());

               carDBHandler.insert(car);
               alert("Car was added to the system.");
               Navigator.getInstance().navigateToPanel();

           } catch (SQLException e) {
                alert("There was a problem while inserting the car into the database, please try again later.");
           } catch (Exception e) {

           }
        }
    }
    private boolean validate () {
        if(comboFuelType.getSelectionModel().isEmpty()) {
            alert("Please select a fuel type");
            return false;
        }
        if(comboBrand.getSelectionModel().isEmpty()) {
            alert("Please select a brand");
            return false;
        }
        if (comboGearBox.getSelectionModel().isEmpty()) {
            alert("Please select a gear box");
            return false;
        }
        if(comboPassengers.getSelectionModel().isEmpty()) {
            alert("Please select amount of passengers");
            return false;
        }
        if(comboSuitcases.getSelectionModel().isEmpty()) {
            alert("Please select amount of suitcases");
            return false;
        }
        if(comboCarType.getSelectionModel().isEmpty()) {
            alert("Please select a car type");
            return false;
        }
        if(textModel.getText().isEmpty()){
            alert("Please specify a model");
            return false;
        }
        if (textYear.getText().isEmpty()){
            alert("Please specify a model year");
            return false;
        }
        if (textPrice.getText().isEmpty()) {
            alert("Please specify the price");
            return false;
        }
        return true;
    }

    @FXML
    private void buttonExitPressed(ActionEvent ae) {
        Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
    }

    private void alert(String prompt) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setHeaderText(prompt);
        alert.showAndWait();
    }
}

