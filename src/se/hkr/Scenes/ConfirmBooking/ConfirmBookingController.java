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
import se.hkr.Database.BookingDBHandler;
import se.hkr.Database.UserDB.UserDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Dialogue;
import se.hkr.Email.Email;
import se.hkr.Model.Booking;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;
import se.hkr.Navigator;
import se.hkr.Scenes.SessionListener;
import se.hkr.UserSession;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ConfirmBookingController implements Initializable, SessionListener<BookingSession> {
    @FXML
    private TableView<Vehicle> tblVehicles;

    @FXML
    private TableView<Pair<Vehicle, VehicleOption>> tblOptions;

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
    private ObservableList<Pair<Vehicle, VehicleOption>> vehicleOptions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BookingSession.getInstance().addListener(this);
        colVehicleBrand.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("brand"));
        colVehicleModel.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("modelName"));
        colOptionName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Pair<Vehicle, VehicleOption>) param.getValue()).getValue().getName());
            }
        });
        colOptionVehicleModel.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Pair<Vehicle, VehicleOption>) param.getValue()).getKey().toString());
            }
        });

        vehicles = FXCollections.observableArrayList(BookingSession.getInstance().getSessionObject().getVehicles());
        vehicleOptions = FXCollections.observableArrayList(BookingSession.getInstance().getSessionObject().getVehicleOptions());
        tblVehicles.setItems(vehicles);
        tblOptions.setItems(vehicleOptions);
        initializeLabels();

        if (!UserSession.getInstance().isLoggedIn()) {
            containerLogin.setVisible(true);
            btnConfirmBooking.setDisable(true);
        }
    }

    @Override
    public void update() {

    }

    private void initializeLabels() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = BookingSession.getInstance().getSessionObject().getStartDate();
        Date endDate = BookingSession.getInstance().getSessionObject().getEndDate();
        lblStartDate.setText(START_DATE_PREFIX + dateFormat.format(startDate));
        lblEndDate.setText(END_DATE_PREFIX + dateFormat.format(endDate));
        long diff = endDate.getTime() - startDate.getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        lblDays.setText(AMOUNT_OF_DAYS_PREFIX + days);
        lblTotalAmount.setText(TOTAL_PREFIX + BookingSession.getInstance().getSessionObject().getTotalPrice());
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
            if (user != null && user instanceof Member) {
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

    @FXML
    private void buttonConfirmPressed(ActionEvent event) {
        try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
            Booking booking = BookingSession.getInstance().getSessionObject();
            List<? extends Vehicle> controlList = VehicleDBHandler.readAvailableVehicles(booking.getStartDate(), booking.getEndDate());
            if (vehiclesAreAvailable()) {
                bookingDBHandler.insert(BookingSession.getInstance().getSessionObject());
                Dialogue.alert("Your booking is made! Thank you for renting from RentAll.");
                sendConfirmationMail();
                BookingSession.getInstance().resetSession();
            } else {
                Dialogue.alert("It seems some of your vehicles was booked while you were booking. Please try again.");
            }
        } catch (Exception e) {
            Dialogue.alert("Could not save booking, please try again later.");
            try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
                bookingDBHandler.delete(BookingSession.getInstance().getSessionObject());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean vehiclesAreAvailable() {
        try {
            Booking booking = BookingSession.getInstance().getSessionObject();
            List<? extends Vehicle> controlVehicles = VehicleDBHandler.readAvailableVehicles(booking.getStartDate(), booking.getEndDate());
            for (Vehicle vehicle : booking.getVehicles()) {
                boolean confirmedVehicle = false;
                for (Vehicle controlVehicle : controlVehicles) {
                    if (controlVehicle.getId() == vehicle.getId()) {
                        confirmedVehicle = true;
                        break;
                    }
                }
                if (!confirmedVehicle) {
                    return false;
                }
            }
        } catch (SQLException e) {
            Dialogue.alert("Something went wrong...");
        }
        return true;
    }

    private void sendConfirmationMail() {
        Booking booking = BookingSession.getInstance().getSessionObject();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String subject = String.format("Booking confirmation for %s %s", UserSession.getInstance().getSessionObject().getFirstName(), UserSession.getInstance().getSessionObject().getLastName());
        StringBuilder message = new StringBuilder();
        message.append("Thank you for choosing RentAll!\n");
        message.append(String.format("Your booking from %s to %s is confirmed. See booking details below.%n",
                                    simpleDateFormat.format(booking.getStartDate()),
                                    simpleDateFormat.format(booking.getEndDate())));
        message.append(String.format("%nBooking id: %d%n", booking.getId()));
        message.append("\nVehicles\n");
        for (Vehicle vehicle : booking.getVehicles()) {
            message.append(String.format("%s %s, Fuel type: %s, Gear box: %s%n", vehicle.getBrand(), vehicle.getModelName(), vehicle.getFuelType(), vehicle.getGearBox()));
        }
        message.append("\nExtra vehicle options\n");
        for (Pair<Vehicle, VehicleOption> pair : booking.getVehicleOptions()) {
            message.append(String.format("%s, for vehicle: %s %s%n", pair.getValue().getName(), pair.getKey().getBrand(), pair.getKey().getModelName()));
        }
        message.append(String.format("%nTotal: $%.2f", booking.getTotalPrice()));
        Email email = new Email(UserSession.getInstance().getSessionObject().getEmail(), subject, message.toString());
        email.send();
    }
}