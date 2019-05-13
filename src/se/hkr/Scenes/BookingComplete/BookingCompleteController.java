package se.hkr.Scenes.BookingComplete;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import se.hkr.Navigator;

import java.net.URL;
import java.util.ResourceBundle;

public class BookingCompleteController implements Initializable {

    @FXML
    private TextArea txtAreaBookingDetails;

    @FXML
    private Button btnGoMainMenu, btnGoMyPages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void btnGoMainMenuPressed(ActionEvent ae){
        Navigator.getInstance().navigateTo("MainMenu/MainMenuViewView.fxml");

    }

    public void btnGoMyPagesPressed(ActionEvent ae){

    }
}
