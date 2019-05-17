package se.hkr.Scenes.BookingComplete;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Model.Booking;
import se.hkr.Model.User.Member;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BookingCompleteController implements Initializable {

    @FXML
    private TextArea txtAreaBookingDetails;

    @FXML
     private MenuItem
            menuItemBack,
            menuItemLogOut,
            menuItemQuit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try (BookingDBHandler dB = new BookingDBHandler()) {

            List<Booking> booking = dB.readForMember((Member) UserSession.getInstance().getSessionObject());
            txtAreaBookingDetails.appendText(String.valueOf(booking));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void menuItemBackPressed (ActionEvent ae) { Navigator.getInstance().goBack(); }


    @FXML
    public void menuItemLogOutPressed (ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemLogOut) {
            UserSession.getInstance().resetSession();
            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        }

    }
    @FXML
    public void menuItemQuitPressed (ActionEvent ae) { System.exit(0);}
}