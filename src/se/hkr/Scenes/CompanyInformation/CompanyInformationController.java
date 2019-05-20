package se.hkr.Scenes.CompanyInformation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

public class CompanyInformationController implements Initializable {

    @FXML
    private Label lblAreaInfo;
    @FXML
    private MenuItem menuItemBack, menuItemLogOut, menuItemContact;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblAreaInfo.setText("This is a student project developed by\n four students at Kristianstad Univsersity.\n\nLisa Johansson\nHåkan Reinholdsson\nTobias Andersson\nRasmus Nilsson\n\nContact us at info@rasmusnilsson.se");

    }

    public void goBack (ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemBack) {
            Navigator.getInstance().goBack();
        }
    }

    public void logOut (ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemLogOut) {
            UserSession.getInstance().resetSession();
            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        }
    }

    public void contactUs (ActionEvent actionEvent) {
        if (actionEvent.getSource() == menuItemContact) {
            Navigator.getInstance().navigateTo("CustomerService/CustomerServiceView.fxml");
        }
    }
}
