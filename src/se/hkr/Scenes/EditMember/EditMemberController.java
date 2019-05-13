package se.hkr.Scenes.EditMember;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.HashUtils;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EditMemberController implements Initializable {

    @FXML
    TextField txtFldFirstName,
            txtFldLastName,
            txtFldSsn,
            txtFldAddress,
            txtFldZip,
            txtFldEmail,
            txtFldPassword,
            txtFldPhone,
            txtFldCity;

    @FXML
    Button btnSave,
            btnGoBack;

    public void btnSavePressed(ActionEvent ae) {

    }

    public void btnGoBackPressed(ActionEvent ae) {
        Navigator.getInstance().navigateTo("ViewBookings/ViewBookingsView.fxml");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (UserSession.getInstance().isMember()) {
            User user = UserSession.getInstance().getSessionObject();
            Member member = (Member) UserSession.getInstance().getSessionObject();
            txtFldSsn.setText(user.getSocialSecurityNo());
            txtFldFirstName.setText(user.getFirstName());
            txtFldLastName.setText(user.getLastName());
            txtFldEmail.setText(user.getEmail());
            txtFldPhone.setText(user.getPhoneNumber());

            Dialogue.alert("Your input was incorrect. Check your information.\nSsn should be in format: YYMMDD-XXXX");
        }
        else {
            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        }
    }
}


