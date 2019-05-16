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
    private TextField txtFldFirstName,
            txtFldLastName,
            txtFldSsn,
            txtFldAddress,
            txtFldZip,
            txtFldEmail,
            txtFldPassword,
            txtFldPhone,
            txtFldCity;

    @FXML
    private Button
            btnSave,
            btnBack;

    public void btnSavePressed(ActionEvent event) {
        if (event.getSource() == btnSave) {
            try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {

                Member member = (Member) UserSession.getInstance().getSessionObject();
                member.setFirstName(txtFldFirstName.getText());
                member.setLastName(txtFldLastName.getText());
                member.setEmail((txtFldEmail.getText()));
                member.setSocialSecurityNo(txtFldSsn.getText());
                member.setPhoneNumber(txtFldPhone.getText());
                member.getAddress().setStreet(txtFldAddress.getText());
                member.getAddress().setZip(txtFldZip.getText());
                member.getAddress().setState(txtFldCity.getText());

                memberDBHandler.update(member);

            } catch (Exception e) {
                Dialogue.alert(e.getMessage());
            }

        }
    }


    public void btnGoBackPressed(ActionEvent event) {
        if (event.getSource() == btnBack) {
            Navigator.getInstance().navigateTo("MemberPanel/MemberPanelView.fxml");
        }
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
