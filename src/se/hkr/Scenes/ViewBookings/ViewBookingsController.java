package se.hkr.Scenes.ViewBookings;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import se.hkr.BookingSession;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Model.User.Member;
import se.hkr.Scenes.ReadController;
import se.hkr.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewBookingsController implements ReadController<Booking> {

    @FXML
    private TextArea txtAreaBookingHistory;

    @FXML
    private ComboBox comboJumpTo;


    public void initialize(URL location, ResourceBundle resources) {

        try (BookingDBHandler dB = new BookingDBHandler()) {

            List<Booking> bookings = dB.readForMember((Member) UserSession.getInstance().getLoggedInUser());
            txtAreaBookingHistory.appendText(String.valueOf(bookings));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean filter (Booking model){
        return false;
    }

    @Override
    public void search () {

    }
}