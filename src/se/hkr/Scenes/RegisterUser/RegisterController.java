package se.hkr.Scenes.RegisterUser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.HashUtils;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerUser() {
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {

            if (Pattern.matches("[0-9][0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]", txtFldSsn.getText())) {
                Member member = new Member(txtFldSsn.getText(), txtFldFirstName.getText(), txtFldLastName.getText(), txtFldEmail.getText(),
                        txtFldPhone.getText(), new Address(txtFldStreet.getText(), txtFldZip.getText(), txtFldState.getText()), HashUtils.hashPassword(txtFldPassword.getText()), txtFldDriversLicense.getText());
                memberDBHandler.insert(member);
                System.out.println("Member inserted!");
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Input error!");
                alert.setHeaderText("Your input was incorrect. Check your information.\nSsn should be in format: YYMMDD-XXXX");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
