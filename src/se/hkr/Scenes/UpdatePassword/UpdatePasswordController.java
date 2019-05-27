package se.hkr.Scenes.UpdatePassword;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.HashUtils;
import se.hkr.Model.User.User;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.sql.SQLException;

public class UpdatePasswordController {
    @FXML
    private TextField txtFldOldPassword, txtFldNewPassword, txtFldConfirmPassword;

    @FXML
    private void btnUpdatePasswordPressed() {
        User user = UserSession.getInstance().getSessionObject();
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
            String oldPassword = HashUtils.hashPassword(txtFldOldPassword.getText());
            if (memberDBHandler.authenticateUser(user.getEmail(), oldPassword)) {
                String newPassword = txtFldNewPassword.getText().trim();
                if (newPassword.equals(txtFldConfirmPassword.getText().trim())) {
                    memberDBHandler.updatePassword(user.getEmail(), HashUtils.hashPassword(newPassword));
                    Dialogue.inform("Password was updated!");
                } else {
                    Dialogue.alert("Please enter matching new passwords.");
                }
            } else {
                Dialogue.alert("Old password incorrect.");
            }
        } catch (SQLException e) {
            Dialogue.alert(e.getMessage());
        } catch (Exception e) {
            Dialogue.alert("Something went wrong, please contact customer support and mention: " + e.getMessage());
        }
    }

    @FXML
    private void btnGoBackPressed() {
        Navigator.getInstance().navigateToPanel();
    }

    @FXML
    private void btnLogOutPressed() {
        UserSession.getInstance().resetSession();
        Navigator.getInstance().navigateToPanel();
    }

    @FXML
    private void btnContactPressed() {
        Navigator.getInstance().navigateTo("CustomerService/CustomerServiceView.fxml");
    }

    @FXML
    private void btnAboutPressed() {
        Navigator.getInstance().navigateTo("CompanyInformation/CompanyInformationView.fxml");
    }
}
