package se.hkr.Scenes.RegisterUser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import se.hkr.Model.User.Member;

import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    TextField txtFldFirstName,
            txtFldLastName,
            txtFldSsn,
            txtFldAddress,
            txtFldZip,
            txtFldEmail,
            txtFldPassword,
            txtFldPhone,
            txtFldCity,
            txtFldDriverslicense;
    @FXML
    Button btnJoin;

    @Override

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerUser(ActionEvent ae) {

        try {

            if (Integer.parseInt(txtFldSsn.getText()) <= 13 && ae.getSource() == btnJoin) {

                //Member member = new Member(txtFldSsn.getText(), txtFldFirstName.getText(), txtFldLastName.getText(), txtFldEmail.getText(),
                //        txtFldPhone.getText(), txtFldAddress.getText(), txtFldDriverslicense.getText());
            }
        } catch (InputMismatchException x) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input error!");
            alert.setHeaderText("Your input was incorrect. Check your input.");
            alert.showAndWait();
        }
    }
}
