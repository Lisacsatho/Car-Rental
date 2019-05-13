package se.hkr.Scenes.ViewBookings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Pair;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.Vehicle;
import se.hkr.Model.Vehicle.VehicleOption;
import se.hkr.Scenes.ReadController;

import java.net.URL;
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
        } catch (Exception e) {
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

    private void removeVehicle(Vehicle vehicle) {
        if (currentVehicles.size() > 1) {
            currentVehicles.remove(vehicle);
            vehiclesToRemove.add(vehicle);
            for (Pair<Vehicle, VehicleOption> pair : currentVehicleOptions) {
                if (vehicle.getId() == pair.getKey().getId()) {
                    currentVehicleOptions.remove(pair);
                }
            }
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
                booking.setTotalPrice(Double.parseDouble(txtFldTotalPrice.getText()));
                bookingDBHandler.update(booking);
                for (Vehicle vehicle : vehiclesToRemove) {
                    bookingDBHandler.removeVehicleFromBooking(vehicle, booking);
                }
                for (Pair<Vehicle, VehicleOption> vehicleOption : vehicleOptionsToRemove) {
                    bookingDBHandler.removeVehicleOptionFromBooking(vehicleOption, booking);
                }
                Dialogue.inform("Booking was updated!");
            } catch (Exception e) {
                Dialogue.alert(e.getMessage());
            }
            updateBookingList();
        }
    }

    @Override
    public boolean filter (Booking model){
        return false;
    }

    @Override
    public void search() {
        String key = txtFldSearch.getText().trim();
        matchingBookings.clear();
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
                    matchingBookings.add(booking);
                }
            } else if (datePicFilterFrom.getValue() != null && datePicFilterTo.getValue() != null) {
                Date filterStartDate = extractDate(datePicFilterFrom);
                Date filterEndDate = extractDate(datePicFilterTo);
                if (booking.getStartDate().after(filterStartDate) && booking.getStartDate().before(filterEndDate)) {
                    matchingBookings.add(booking);
                }
            } else {
                matchingBookings.add(booking);
            }
        }
    }

    private void searchBykey(String key) {
        for (Booking booking : allBookings) {
            if (datePicFilterFrom.getValue() != null && datePicFilterTo == null) {
                Date filterStartDate = extractDate(datePicFilterFrom);
                if (booking.matches(key) && (booking.getStartDate().after(filterStartDate))) {
                    matchingBookings.add(booking);
                }
            } else if (datePicFilterFrom.getValue() != null && datePicFilterTo.getValue() != null) {
                Date filterStartDate = extractDate(datePicFilterFrom);
                Date filterEndDate = extractDate(datePicFilterTo);
                if (booking.matches(key) && (booking.getStartDate().after(filterStartDate) && booking.getStartDate().before(filterEndDate))) {
                    matchingBookings.add(booking);
                }
            } else {
                if (booking.matches(key)) {
                    matchingBookings.add(booking);
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