package se.hkr.Scenes.EmployeePanel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import se.hkr.BookingSession;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.Booking;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.User;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class EmployeePanelController implements Initializable {
    @FXML
    private TableView<Booking> tblBookingsStarting, tblBookingsEnding;

    @FXML
    private TableColumn colStartingId, colStartingMember, colStartingEndDate;

    @FXML
    private TableColumn colEndingId, colEndingMember, colEndingReturned;

    @FXML
    private Label lblWelcomeMessage;

    @FXML
    private Button btnManageEmployees, btnAddEmployee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!UserSession.getInstance().isEmployee()) {
            UserSession.getInstance().resetSession();
            Navigator.getInstance().navigateToPanel();
        } else {
            User user = UserSession.getInstance().getSessionObject();
            lblWelcomeMessage.setText(String.format("Welcome %s %s", user.getFirstName(), user.getLastName()));
            initializeTables();
            if (UserSession.getInstance().isManager()) {
                btnManageEmployees.setVisible(true);
                btnAddEmployee.setVisible(true);
            } else {
                btnManageEmployees.setVisible(false);
                btnAddEmployee.setVisible(false);
            }
        }
    }

    private void initializeTables() {
        try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
            ObservableList<Booking> startingBookings = FXCollections.observableArrayList(bookingDBHandler.readBookingsStartingToday());
            ObservableList<Booking> endingBookings = FXCollections.observableArrayList(bookingDBHandler.readBookingsEndingToday());
            colStartingId.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("id"));
            colEndingId.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("id"));
            colStartingMember.setCellValueFactory(new PropertyValueFactory<Booking, String>("member"));
            colEndingMember.setCellValueFactory(new PropertyValueFactory<Booking, String>("member"));
            colStartingEndDate.setCellValueFactory(new PropertyValueFactory<Booking, Date>("endDate"));
            colEndingReturned.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures param) {
                    if (((Booking) param.getValue()).isReturned()) {
                        return new SimpleStringProperty("YES");
                    } else {
                        return new SimpleStringProperty("NO");
                    }
                }
            });

            tblBookingsStarting.setItems(startingBookings);
            tblBookingsEnding.setItems(endingBookings);
        } catch (SQLException e) {
            Dialogue.alert("Problem communicating with database: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void buttonManageBookingsPressed() {
        Navigator.getInstance().navigateTo("ViewBookings/ViewBookingsView.fxml");
    }

    @FXML
    private void buttonManageMembersPressed() {
        Navigator.getInstance().navigateTo("ViewMembers/ViewMembersView.fxml");
    }

    @FXML
    private void buttonManageVehiclesPressed() {
        Navigator.getInstance().navigateTo("ViewVehicles/ViewVehiclesView.fxml");
    }

    @FXML
    private void buttonAddVehiclePressed() {
        Navigator.getInstance().navigateTo("AddVehicle/AddVehicleView.fxml");
    }

    @FXML
    private void buttonManageEmployeesPressed() {
        Navigator.getInstance().navigateTo("ViewEmployees/ViewEmployeesView.fxml");
    }

    @FXML
    private void buttonAddEmployeePressed() {
        Navigator.getInstance().navigateTo("AddEmployee/AddEmployeeView.fxml");
    }

    @FXML
    private void buttonInspectStartingBookingPressed() {
        Booking booking = tblBookingsStarting.getSelectionModel().getSelectedItem();
        if (booking != null) {
            BookingSession.getInstance().setSessionObject(booking);
            Navigator.getInstance().navigateTo("ViewBookings/ViewBookingsView.fxml");
        } else {
            Dialogue.alert("Please select a booking to inspect.");
        }
    }

    @FXML
    private void buttonInspectEndingBookingPressed() {
        Booking booking = tblBookingsEnding.getSelectionModel().getSelectedItem();
        if (booking != null) {
            BookingSession.getInstance().setSessionObject(booking);
            Navigator.getInstance().navigateTo("ViewBookings/ViewBookingsView.fxml");
        } else {
            Dialogue.alert("Please select a booking to inspect.");
        }
    }
}
