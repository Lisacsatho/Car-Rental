package se.hkr.Scenes.ViewBookings;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import se.hkr.BookingSession;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Scenes.ReadController;

public class ViewBookingsController implements ReadController {

    @FXML
    TextArea txtAreaBookingHistory;

    @FXML
    ComboBox comboJumpTo;


    /*private void setTxtAreaBookingHistory() {

        try {

            if (BookingDBHandler.getInstance().getBooking() != null) {

                for (int i = 0; i < BookingDBHandler.getInstance().getBooking().readForMember(); ++) {
                    if (BookingDBHandler.getInstance().getBooking().readForMember(i) != null) {
                        txtAreaBookingHistory.appendText(Booking.getInstance().getBooking().get(i).toString());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    @Override
    public void filter() {

    }

    @Override
    public void search() {

    }
}