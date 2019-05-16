package se.hkr.Scenes.RegisterUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.Email.Email;
import se.hkr.HashUtils;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    private TextField
            txtFldFirstName,
            txtFldLastName,
            txtFldSsn,
            txtFldStreet,
            txtFldZip,
            txtFldEmail,
            txtFldPassword,
            txtFldPhone,
            txtFldState,
            txtFldDriversLicense;

    @FXML
    private Button btnJoin;

    public void registerUser() {
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
            // TODO: implement more input verification.
            if (Pattern.matches("[0-9][0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]", txtFldSsn.getText())) {
                Member member = new Member(txtFldSsn.getText(), txtFldFirstName.getText(), txtFldLastName.getText(), txtFldEmail.getText(),
                        txtFldPhone.getText(), new Address(txtFldStreet.getText(), txtFldZip.getText(), txtFldState.getText()),
                        HashUtils.hashPassword(txtFldPassword.getText()), txtFldDriversLicense.getText(), false);
                member.setVerificationCode(HashUtils.generateCode(member.getEmail()));
                memberDBHandler.insert(member);
                Email email = new Email(member.getEmail(), "Email confirmation | RentAll", "Please verify your email using the following code: " + member.getVerificationCode());
                email.send();
                Dialogue.alert("User registered! Please check your email for the confirmation email.");
                Navigator.getInstance().goBack();
            } else {
                Dialogue.alert("Your input was incorrect. Check your information.\nSsn should be in format: YYMMDD-XXXX");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Dialogue.alert("Database connection failed, please try again later.");
        }
    }

    public void goBack (ActionEvent event) {
        Navigator.getInstance().goBack();
    }

    public void joinSystem (ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnJoin) {

            Navigator.getInstance().navigateTo("MemberPanel/MemberPanelView.fxml");
        }
    }
}
