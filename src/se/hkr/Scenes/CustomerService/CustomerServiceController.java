package se.hkr.Scenes.CustomerService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import se.hkr.Email.Email;
import se.hkr.Navigator;
import se.hkr.UserSession;


public class CustomerServiceController {


    @FXML
    private TextField txtFldName, txtFldEmailAddress;
    @FXML
    private Button btnSend;
    @FXML
    private Label lblContactSupport;
    @FXML
    private TextArea txtAreaDescription;
    @FXML
    private MenuItem menuItemGoBack, menuItemLogOut, menuItemAbout;

    public void issueMessage(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnSend) {

            txtFldName.getText();
            txtFldEmailAddress.getText();
            txtFldEmailAddress.getText();

            Email email = new Email("rentall.hkr@gmail.com", txtFldEmailAddress.getText(), txtAreaDescription.getText());
            email.send();
            lblContactSupport.setText("Email sent. We will contact you!");
        }
    }

    public void menuItemGoBack (ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemGoBack) {
            Navigator.getInstance().goBack();
        }
    }

    public void logOut (ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemLogOut) {
            UserSession.getInstance().resetSession();
            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        }
    }
    public void contactUs () {lblContactSupport.setText("Describe your issue below and press send.");}

    public void aboutUs (ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemAbout) {

            Navigator.getInstance().navigateTo("CompanyInformation/CompanyInformationView.fxml");
        }
    }


}


