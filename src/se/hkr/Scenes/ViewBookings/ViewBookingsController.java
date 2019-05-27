package se.hkr.Scenes.ViewBookings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Pair;
import se.hkr.BookingSession;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;
import se.hkr.Navigator;
import se.hkr.Scenes.ReadController;
import se.hkr.UserSession;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ViewBookingsController implements ReadController<Booking>, Initializable {
    private ObservableList<Booking> allBookings;
    private ObservableList<Booking> matchingBookings;

    @FXML
    private TableView<Booking> tblBookings;
    @FXML
    private TableColumn colBookingId,
                        colBookingMember,
                        colBookingStartDate,
                        colBookingEndDate,
                        colBookingTotalPrice;

    @FXML
    private TableView<Vehicle> tblVehicles;
    @FXML
    private TableColumn colVehicleModel, colVehicleBrand, colVehiclePriceFrom;
    private ObservableList<Vehicle> currentVehicles;
    private ObservableList<Vehicle> vehiclesToRemove;

    @FXML
    private TableView<Pair<Vehicle, VehicleOption>> tblVehicleOptions;
    @FXML
    private TableColumn colOptionVehicle, colOptionName;
    private ObservableList<Pair<Vehicle, VehicleOption>> currentVehicleOptions;
    private ObservableList<Pair<Vehicle, VehicleOption>> vehicleOptionsToRemove;

    @FXML
    private TextField txtFldSearch;

    @FXML
    private DatePicker datePicFilterFrom, datePicFilterTo;

    @FXML
    private DatePicker datePicStartDate, datePicEndDate;

    @FXML
    private TextField txtFldTotalPrice;

    @FXML
    private Label lblLateReturn;

    @FXML
    private CheckBox checkBoxReturned, checkBoxShowReturned;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colBookingId.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("id"));
        colBookingMember.setCellValueFactory(new PropertyValueFactory<Booking, String>("member"));
        colBookingStartDate.setCellValueFactory(new PropertyValueFactory<Booking, Date>("startDate"));
        colBookingEndDate.setCellValueFactory(new PropertyValueFactory<Booking, Date>("endDate"));
        colBookingTotalPrice.setCellValueFactory(new PropertyValueFactory<Booking, Double>("totalPrice"));

        updateBookingList();
        tblBookings.setItems(matchingBookings);

        colVehicleModel.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("modelName"));
        colVehicleBrand.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("brand"));
        colVehiclePriceFrom.setCellValueFactory(new PropertyValueFactory<Vehicle, Double>("basePrice"));

        colOptionVehicle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Pair<Vehicle, VehicleOption>) param.getValue()).getKey().getModelName());
            }
        });
        colOptionName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Pair<Vehicle, VehicleOption>) param.getValue()).getValue().getName());
            }
        });

        currentVehicleOptions = FXCollections.observableArrayList();
        vehicleOptionsToRemove = FXCollections.observableArrayList();
        currentVehicles = FXCollections.observableArrayList();
        vehiclesToRemove = FXCollections.observableArrayList();

        tblBookings.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            displayBooking(newValue);
        });
        tblVehicles.setItems(currentVehicles);
        tblVehicleOptions.setItems(currentVehicleOptions);

        Booking booking = BookingSession.getInstance().getSessionObject();
        if (booking != null) {
            try {
                checkBoxShowReturned.setSelected(booking.isReturned());
                search();
                tblBookings.getSelectionModel().select(booking);
                BookingSession.getInstance().resetSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBookingList() {
        try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
            allBookings = FXCollections.observableArrayList(bookingDBHandler.readAllSimple());
            if (matchingBookings == null) {
                matchingBookings = FXCollections.observableArrayList(allBookings);
            } else {
                matchingBookings.clear();
                matchingBookings.addAll(allBookings);
            }
            search();
        } catch (Exception e) {
            e.printStackTrace();
            Dialogue.alert("Could not connect to database.");
        }
    }

    private void displayBooking(Booking booking) {
        try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
            Booking extendedBooking = bookingDBHandler.readByPrimaryKey(Integer.toString(booking.getId()));
            vehicleOptionsToRemove.clear();
            vehiclesToRemove.clear();

            currentVehicles.clear();
            currentVehicles.addAll(extendedBooking.getVehicles());
            currentVehicleOptions.clear();
            currentVehicleOptions.addAll(extendedBooking.getVehicleOptions());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            datePicStartDate.setValue(LocalDate.parse(simpleDateFormat.format(booking.getStartDate())));
            datePicEndDate.setValue(LocalDate.parse(simpleDateFormat.format(booking.getEndDate())));

            if (!booking.isReturned()) {
                checkBoxReturned.setDisable(false);
                checkBoxReturned.setSelected(false);
                long daysLate = calculateLateBooking(booking);
                if (daysLate > 0) {
                    lblLateReturn.textFillProperty().setValue(Paint.valueOf("#c0392b"));
                    lblLateReturn.setText(String.format("Booking is late %d days.", daysLate));
                } else if (daysLate == 0) {
                    lblLateReturn.textFillProperty().setValue(Paint.valueOf("#E8AB20"));
                    lblLateReturn.setText("Return is expected today");
                } else {
                    lblLateReturn.textFillProperty().setValue(Paint.valueOf("#27ae60"));
                    lblLateReturn.setText(String.format("There are %d days left until return date.", Math.abs(daysLate)));
                }
            } else {
                lblLateReturn.textFillProperty().setValue(Paint.valueOf("#27ae60"));
                lblLateReturn.setText("Booking has been returned.");
                checkBoxReturned.setSelected(true);
                checkBoxReturned.setDisable(true);
            }
            txtFldTotalPrice.setText(String.format("%.2f", booking.getTotalPrice()));
        } catch (Exception e) {
            resetDisplay();
        }
    }

    @FXML
    private void btnRemoveVehiclePressed() {
        if (tblVehicles.getSelectionModel().getSelectedItem() != null) {
            Vehicle vehicle = tblVehicles.getSelectionModel().getSelectedItem();
            if (Dialogue.alertOk("Are you sure you want to delete: " + vehicle.getModelName() + "?\nAll vehicle options will also be removed.")) {
                removeVehicle(vehicle);
            }
        } else {
            Dialogue.alert("Please choose a vehicle to delete.");
        }
    }

    private long calculateLateBooking(Booking booking) {
        long days = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date endDate = simpleDateFormat.parse(simpleDateFormat.format(booking.getEndDate()));
            long diff = today.getTime() - endDate.getTime();
            days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            Dialogue.alert("Could not parse date: " + e.getMessage());
        }
        return days;
    }

    private void removeVehicle(Vehicle vehicle) {
        if (currentVehicles.size() > 1) {
            currentVehicles.remove(vehicle);
            vehiclesToRemove.add(vehicle);
            List<Pair<Vehicle, VehicleOption>> temporaryVehicleOptionsToRemove = new ArrayList<>();
            for (Pair<Vehicle, VehicleOption> pair : currentVehicleOptions) {
                if (vehicle.getId() == pair.getKey().getId()) {
                    temporaryVehicleOptionsToRemove.add(pair);
                }
            }
            currentVehicleOptions.removeAll(temporaryVehicleOptionsToRemove);
            // Update the price to match the changes made
            txtFldTotalPrice.setText(Double.toString(calculatePriceForCurrentBooking()));
        } else {
            Dialogue.alert("Bookings must have at least one vehicle associated with it.");
        }
    }

    @FXML
    private void btnRemoveVehicleOptionPressed() {
        Pair<Vehicle, VehicleOption> vehicleOption = tblVehicleOptions.getSelectionModel().getSelectedItem();
        if (vehicleOption != null) {
            if (Dialogue.alertOk(String.format("Are you sure you want to delete: %s from vehicle %s?", vehicleOption.getValue().getName(), vehicleOption.getKey().getModelName()))) {
                removeVehicleOption(vehicleOption);
            }
        }
    }

    private void removeVehicleOption(Pair<Vehicle, VehicleOption> vehicleOption) {
        currentVehicleOptions.remove(vehicleOption);
        vehicleOptionsToRemove.add(vehicleOption);
        // Update the price to match the changes made
        txtFldTotalPrice.setText(Double.toString(calculatePriceForCurrentBooking()));
    }

    private void resetDisplay() {
        vehiclesToRemove.clear();
        vehicleOptionsToRemove.clear();
        currentVehicleOptions.clear();
        currentVehicles.clear();
        datePicStartDate.setValue(null);
        datePicEndDate.setValue(null);
        txtFldTotalPrice.clear();
        lblLateReturn.setText("");
    }

    private double calculatePriceForCurrentBooking() {
        Booking booking = tblBookings.getSelectionModel().getSelectedItem();
        double price = 0.0;
        if (booking != null) {
            Date startDate = booking.getStartDate();
            Date endDate = booking.getEndDate();
            long diff = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            for (Vehicle vehicle : currentVehicles) {
                price += vehicle.getBasePrice() * days;
            }
            for (Pair<Vehicle, VehicleOption> pair : currentVehicleOptions) {
                price += pair.getValue().getPrice() * days;
            }
        }
        return price;
    }

    @FXML
    private void btnSaveBookingPressed() {
        Booking booking = tblBookings.getSelectionModel().getSelectedItem();
        if (booking != null) {
            try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
                Booking extendedBooking = bookingDBHandler.readByPrimaryKey(Integer.toString(booking.getId()));
                double price = Double.parseDouble(txtFldTotalPrice.getText());
                if (checkBoxReturned.isSelected() && calculateLateBooking(extendedBooking) > 0) {
                    double penaltyFine = 0.0;
                    for (Vehicle vehicle : extendedBooking.getVehicles()) {
                        penaltyFine += vehicle.getBasePrice();
                    }
                    for (Pair<Vehicle, VehicleOption> pair : extendedBooking.getVehicleOptions()) {
                        penaltyFine += pair.getValue().getPrice();
                    }
                    penaltyFine *= calculateLateBooking(extendedBooking);
                    price += penaltyFine;
                    Dialogue.inform("The customer recieved a $" + penaltyFine + " penalty fine.");
                }
                extendedBooking.setTotalPrice(price);
                extendedBooking.setReturned(checkBoxReturned.isSelected());
                bookingDBHandler.update(extendedBooking);
                for (Vehicle vehicle : vehiclesToRemove) {
                    bookingDBHandler.removeVehicleFromBooking(vehicle, extendedBooking);
                }
                for (Pair<Vehicle, VehicleOption> vehicleOption : vehicleOptionsToRemove) {
                    bookingDBHandler.removeVehicleOptionFromBooking(vehicleOption, extendedBooking);
                }
                Dialogue.inform("Booking was updated!");
            } catch (Exception e) {
                e.printStackTrace();
                Dialogue.alert(e.getMessage());
            }
            updateBookingList();
        }
    }

    @FXML
    private void btnRemoveBookingPressed() {
        Booking booking = tblBookings.getSelectionModel().getSelectedItem();
        if (booking != null) {
            if (Dialogue.alertOk(String.format("Are you sure you want to delete booking id: %d for member: %s?", booking.getId(), booking.getMember()))) {
                removeBooking(booking);
                updateBookingList();
            }
        }
    }

    private void removeBooking(Booking booking) {
        try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
            bookingDBHandler.delete(booking);
            Dialogue.alert("Booking was deleted successfully.");
        } catch (Exception e) {
            Dialogue.alert("Something went wrong when deleting booking: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        Navigator.getInstance().goBack();
    }

    @FXML
    private void buttonLogOutPressed() {
        UserSession.getInstance().resetSession();
        Navigator.getInstance().navigateToPanel();
    }

    @Override
    public boolean filter (Booking model){
        return false;
    }

    @FXML
    private void btnResetDatesPressed() {
        datePicFilterTo.setValue(null);
        datePicFilterFrom.setValue(null);
    }

    @Override
    public void search() {
        String key = txtFldSearch.getText().trim();
        matchingBookings.clear();
        if (datePicFilterFrom.getValue() == null && datePicFilterTo.getValue() != null) {
            datePicEndDate.setValue(null);
            Dialogue.inform("Use the 'from' date to filter by one date.");
        }
        if (key.equals("")) {
            searchOnEmptyKey();
        } else {
            searchBykey(key);
        }
    }

    private void searchOnEmptyKey() {
        for (Booking booking : allBookings) {
            if (datePicFilterFrom.getValue() != null && datePicFilterTo.getValue() == null) {
                Date filterStartDate = extractDate(datePicFilterFrom);
                if (booking.getStartDate().after(filterStartDate)) {
                    if (checkBoxShowReturned.isSelected()) {
                        matchingBookings.add(booking);
                    } else if (!checkBoxShowReturned.isSelected() && !booking.isReturned()) {
                        matchingBookings.add(booking);
                    }
                }
            } else if (datePicFilterFrom.getValue() != null && datePicFilterTo.getValue() != null) {
                Date filterStartDate = extractDate(datePicFilterFrom);
                Date filterEndDate = extractDate(datePicFilterTo);
                if (booking.getStartDate().after(filterStartDate) && booking.getStartDate().before(filterEndDate)) {
                    if (checkBoxShowReturned.isSelected()) {
                        matchingBookings.add(booking);
                    } else if (!checkBoxShowReturned.isSelected() && !booking.isReturned()) {
                        matchingBookings.add(booking);
                    }
                }
            } else {
                if (checkBoxShowReturned.isSelected()) {
                    matchingBookings.add(booking);
                } else if (!checkBoxShowReturned.isSelected() && !booking.isReturned()) {
                    matchingBookings.add(booking);
                }
            }
        }
    }

    private void searchBykey(String key) {
        for (Booking booking : allBookings) {
            if (datePicFilterFrom.getValue() != null && datePicFilterTo == null) {
                Date filterStartDate = extractDate(datePicFilterFrom);
                if (booking.matches(key) && (booking.getStartDate().after(filterStartDate))) {
                    if (checkBoxShowReturned.isSelected()) {
                        matchingBookings.add(booking);
                    } else if (!checkBoxShowReturned.isSelected() && !booking.isReturned()) {
                        matchingBookings.add(booking);
                    }
                }
            } else if (datePicFilterFrom.getValue() != null && datePicFilterTo.getValue() != null) {
                Date filterStartDate = extractDate(datePicFilterFrom);
                Date filterEndDate = extractDate(datePicFilterTo);
                if (booking.matches(key) && (booking.getStartDate().after(filterStartDate) && booking.getStartDate().before(filterEndDate))) {
                    if (checkBoxShowReturned.isSelected()) {
                        matchingBookings.add(booking);
                    } else if (!checkBoxShowReturned.isSelected() && !booking.isReturned()) {
                        matchingBookings.add(booking);
                    }
                }
            } else {
                if (booking.matches(key)) {
                    if (checkBoxShowReturned.isSelected()) {
                        matchingBookings.add(booking);
                    } else if (!checkBoxShowReturned.isSelected() && !booking.isReturned()) {
                        matchingBookings.add(booking);
                    }
                }
            }
        }
    }

    private Date extractDate(DatePicker datePicker) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(datePicker.getValue().format(dateFormatter));
        } catch (Exception e) {
            Dialogue.alert("Enter valid date values.");
        }
        return new Date();
    }
}