package se.hkr.Scenes.VerifyEmail;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import se.hkr.BookingSession;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.User.Member;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

public class VerifyEmailController implements Initializable {
    @FXML
    private TextField txtFldCode;

    @FXML
    private Label lblEmail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!UserSession.getInstance().isLoggedIn()) {
            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        } else {
            lblEmail.setText(UserSession.getInstance().getSessionObject().getEmail());
        }
    }
    @FXML
    private void buttonConfirmPressed(ActionEvent event) {
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
            if (memberDBHandler.verifyEmail((Member) UserSession.getInstance().getSessionObject(), txtFldCode.getText().trim())) {
                ((Member) UserSession.getInstance().getSessionObject()).setVerified(true);
                Dialogue.alert("Email verified!");
                if (BookingSession.getInstance().getSessionObject() == null) {
                    Navigator.getInstance().navigateToPanel();
                } else {
                    Navigator.getInstance().goBack();
                }
            } else {
                Dialogue.alert("Wrong code.");
            }
        } catch (Exception e) {
            Dialogue.alert("We're having problem talking to the server right now.");
        }
    }
}
