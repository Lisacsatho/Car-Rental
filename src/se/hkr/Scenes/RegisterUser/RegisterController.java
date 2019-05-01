package se.hkr.Scenes.RegisterUser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;

import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

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
    @FXML
    private Button btnJoin;

    @Override

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerUser(ActionEvent ae) {
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {

            if (txtFldSsn.getText().length() == 13 && (txtFldDriversLicense.getText().length() == 9 && ae.getSource() == btnJoin)) {
                Member member = new Member(txtFldSsn.getText(), txtFldFirstName.getText(), txtFldLastName.getText(), txtFldEmail.getText(),
                                           txtFldPhone.getText(), new Address(txtFldStreet.getText(), txtFldZip.getText(), txtFldState.getText()), txtFldPassword.getText(), txtFldDriversLicense.getText());
                memberDBHandler.insert(member);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input error!");
            alert.setHeaderText("Your input was incorrect. Check your information.");
            alert.showAndWait();
        }
    }
}
