package se.hkr.Scenes.ConfirmBooking;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.Pair;
import se.hkr.BookingSession;
import se.hkr.Database.UserDB.UserDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ConfirmBookingController implements Initializable {
    @FXML
    private TableView<Vehicle> tblVehicles;

    @FXML
    private TableView<Pair<String, VehicleOption>> tblOptions;

    @FXML
    private TableColumn colVehicleModel,
                        colVehicleBrand,
                        colOptionName,
                        colOptionVehicleModel;

    @FXML
    private AnchorPane containerLogin;

    @FXML
    private Button btnConfirmBooking;
    @FXML
    private TextField txtFldEmail,
                      txtFldPassword;

    @FXML
    private Label lblStartDate,
                  lblEndDate,
                  lblDays,
                  lblTotalAmount;
    private final String START_DATE_PREFIX = "Start date: ";
    private final String END_DATE_PREFIX = "End date: ";
    private final String AMOUNT_OF_DAYS_PREFIX = "Days: ";
    private final String TOTAL_PREFIX = "Total amount: $";

    private ObservableList<Vehicle> vehicles;
    private ObservableList<Pair<String, VehicleOption>> vehicleOptions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colVehicleBrand.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("brand"));
        colVehicleModel.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("modelName"));
        colOptionName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Pair<String, VehicleOption>) param.getValue()).getValue().getName());
            }
        });
        colOptionVehicleModel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Pair<String, VehicleOption>) param.getValue()).getKey());
            }
        });

        vehicles = FXCollections.observableArrayList(BookingSession.getInstance().getBooking().getVehicles());
        vehicleOptions = FXCollections.observableArrayList(BookingSession.getInstance().getBooking().getVehicleOptions());
        tblVehicles.setItems(vehicles);
        tblOptions.setItems(vehicleOptions);
        initializeLabels();

        if (!UserSession.getInstance().isLoggedIn()) {
            containerLogin.setVisible(true);
            btnConfirmBooking.setDisable(true);
        }
    }

    private void initializeLabels() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = BookingSession.getInstance().getBooking().getStartDate();
        Date endDate = BookingSession.getInstance().getBooking().getEndDate();
        lblStartDate.setText(START_DATE_PREFIX + dateFormat.format(startDate));
        lblEndDate.setText(END_DATE_PREFIX + dateFormat.format(endDate));
        long diff = endDate.getTime() - startDate.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        lblDays.setText(AMOUNT_OF_DAYS_PREFIX + days);
        lblTotalAmount.setText(TOTAL_PREFIX + BookingSession.getInstance().getBooking().getTotalPrice());
    }

    @FXML
    private void goBack(ActionEvent event) {
        Navigator.getInstance().goBack();
    }

    @FXML
    private void cancelBooking(ActionEvent event) {
        BookingSession.getInstance().resetSession();
        Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
    }

    @FXML
    private void buttonSignUpPressed(ActionEvent event) {
        Navigator.getInstance().navigateTo("RegisterUser/RegisterView.fxml");
    }

    @FXML
    private void buttonLogInPressed(ActionEvent event) {
        String email = txtFldEmail.getText();
        String password = txtFldPassword.getText();
        try {
            User user = UserDBHandler.authenticate(email, password);
            if (user != null) {
                UserSession.getInstance().logIn(user);
                if (((Member) user).isVerified()) {
                    btnConfirmBooking.setDisable(false);
                    containerLogin.setVisible(false);
                } else {
                    Navigator.getInstance().navigateTo("VerifyEmail/VerifyEmailView.fxml");
                }
            } else {
                Dialogue.alert("No user found");
            }
        } catch (SQLException e) {
            Dialogue.alert("No user found");
        }
    }
}