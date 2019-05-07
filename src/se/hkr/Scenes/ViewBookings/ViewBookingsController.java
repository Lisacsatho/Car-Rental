package se.hkr.Scenes.ViewBookings;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import se.hkr.Scenes.ReadController;

public class ViewBookingsController implements ReadController {

    @FXML
    TextArea txtAreaBookingHistory;

    @FXML
    ComboBox comboJumpTo;


    private void setTxtAreaBookingHistory(){

        /*if (BookingDBHandler.getInstance().getBookingList() != null) {

            for (int i = 0; i < BookingDBHandler.getInstance().getBookingList().size(); i++) {
                if (BookingDBHandler.getInstance().getBookingList().get(i) != null) {
                    txtAreaBookingHistory.appendText(BookingDBHandler.getInstance().getBookingList().get(i).toString());
                }*/
            }


    @Override
    public void filter() {

    }

    @Override
    public void search() {

    }
}
