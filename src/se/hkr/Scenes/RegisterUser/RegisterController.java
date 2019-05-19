package se.hkr.Scenes.RegisterUser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.Email.Email;
import se.hkr.HashUtils;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;
import se.hkr.Navigator;

import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    private TextField txtFldFirstName,
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
    private void registerUser() {
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
            // TODO: implement more input verification.
            if (validateInformation()) {
                Member member = new Member(txtFldSsn.getText(), txtFldFirstName.getText(), txtFldLastName.getText(), txtFldEmail.getText(),
                        txtFldPhone.getText(), new Address(txtFldStreet.getText(), txtFldZip.getText(), txtFldState.getText()),
                        HashUtils.hashPassword(txtFldPassword.getText()), txtFldDriversLicense.getText(), false);
                member.setVerificationCode(HashUtils.generateCode(member.getEmail()));
                memberDBHandler.insert(member);
                Email email = new Email(member.getEmail(), "Email confirmation | RentAll", "Please verify your email using the following code: " + member.getVerificationCode());
                email.send();
                Dialogue.alert("User registered! Please check your email for the confirmation email.");
                Navigator.getInstance().goBack();
            }
        } catch (Exception e) {
            Dialogue.alert("Database connection failed, please try again later. " + e.getMessage());
        }
    }

    private boolean validateInformation() {
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
            if (!Pattern.matches("[0-9][0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]", txtFldSsn.getText())) {
                Dialogue.alert("Please enter correct social security no. format YYMMDD-XXXX.");
                return false;
            } else if (!Pattern.matches("[1-9][1-9][1-9] [1-9][1-9]", txtFldZip.getText())) {
                Dialogue.alert("Please enter zip code in XXX XX format.");
                return false;
            } else if (!Pattern.matches(".*[@].*[.].*", txtFldEmail.getText())) {
                Dialogue.alert("Invalid email address.");
                return false;
            } else if (memberDBHandler.readByPrimaryKey(txtFldSsn.getText()) != null) {
                Dialogue.alert("Member already exists with that social security no.");
                return false;
            } else if (memberDBHandler.readByEmail(txtFldEmail.getText()) != null) {
                Dialogue.alert("Member already exists with that email.");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Dialogue.alert(e.getMessage());
        }
        return false;
    }

    @FXML
    private void goBack () {
        Navigator.getInstance().goBack();
    }
}
