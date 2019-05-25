package se.hkr.Scenes.EditMember;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import se.hkr.*;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;

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
            txtFldPhone,
            txtFldCity;

    @FXML
    private Button
            btnSave;

    @FXML
    private MenuItem
            menuItemBack,
            menuItemCancel,
            menuItemLogOut,
            menuItemContact,
            menuItemAbout,
            menuItemQuit;

    public void btnSavePressed(ActionEvent event) {
        if (event.getSource() == btnSave) {
            if (validateInformation()) {
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
        Dialogue.alertOk("Information updated!");
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
            } else if (!Pattern.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]",
                    txtFldPhone.getText())) {
                Dialogue.alert("Please enter your phone number. Starting with 00 followed by country number. " +
                        "No letters or symbols." + "");
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
    public void menuItemBackPressed(ActionEvent ae) {
        Navigator.getInstance().goBack();
    }

    @FXML
    private void menuItemQuitPressed(ActionEvent ae) {
        System.exit(0);
    }

    @FXML
    private void menuItemCancelPressed(ActionEvent ae) {
        Navigator.getInstance().navigateToPanel();
    }

    @FXML
    public void menuItemLogOutPressed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemLogOut) {
            UserSession.getInstance().resetSession();

            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        }
    }

    public void btnGoBackPressed(ActionEvent event) {
        Navigator.getInstance().navigateTo("MemberPanel/MemberPanelView.fxml");
    }

    @FXML
    public void menuItemContactPressed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemContact) {

            Navigator.getInstance().navigateTo("CustomerService/CustomerServiceView.fxml");
        }
    }

    @FXML
    public void menuItemAboutPressed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemAbout) {

            Navigator.getInstance().navigateTo("CustomerService/CustomerServiceView.fxml");
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
            txtFldAddress.setText(user.getAddress().getStreet());
            txtFldZip.setText(user.getAddress().getZip());
            txtFldCity.setText(user.getAddress().getState());
        } else {
            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        }
    }
}
