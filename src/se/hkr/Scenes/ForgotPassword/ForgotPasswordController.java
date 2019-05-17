package se.hkr.Scenes.ForgotPassword;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import se.hkr.Database.UserDB.EmployeeDBHandler;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.Email.Email;
import se.hkr.HashUtils;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Navigator;
import se.hkr.Scenes.ReadController;
import se.hkr.UserSession;

import java.net.URL;
import java.security.SecureRandom;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ForgotPasswordController implements Initializable {


    @FXML
    private TextField
            txtFieldMail,
            txtFieldCode,
            txtFieldNewPassw,
            txtFieldRePassw;

    @FXML
    private Button
            btnSend,
            btnSave;

    @FXML
    private MenuItem
            menuItemBack,
            menuItemCancel,
            menuItemLogOut,
            menuItemContact,
            menuItemAbout,
            menuItemQuit;


    private int code;

    public void btnSendPressed(ActionEvent ae) {

        txtFieldMail.getText();
        try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
            if (memberDBHandler.readByEmail(txtFieldMail.getText()) != null) {
                SecureRandom random = new SecureRandom();
                code = random.nextInt();
                Email email = new Email(txtFieldMail.getText(), "Password reset | RentAll", "Please verify your email using the following code: " + code);
                email.send();
                Dialogue.alert("Please check your email for the reset code.");
            } else {
                Dialogue.alert("Your e-mail is not registered in our system. Try again or sign up to become a member.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Dialogue.alert("Database connection failed, please try again later.");
        }

    }

    @FXML
    public void btnSavePressed(ActionEvent ae) {
        if (ae.getSource() == btnSave) {

            if (txtFieldCode.getText().equals(Integer.toString(code))) {
                txtFieldNewPassw.getText();
                if (txtFieldRePassw.getText().equals(txtFieldNewPassw.getText())) {

                    String password = HashUtils.hashPassword(txtFieldRePassw.getText());

                    try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {
                        memberDBHandler.updatePassword(txtFieldMail.getText(), password);
                        Dialogue.inform("Your password was updated.");
                    } catch (Exception e) {
                        Dialogue.alert("Could not connect to database." + e.getMessage());
                    }
                } else {
                    Dialogue.alert("Please enter the received reset code and a new password in both password columns.");
                }


            }

        }

    }

    @FXML
    public void menuItemBackPressed(ActionEvent ae) {
        Navigator.getInstance().goBack();
    }

    @FXML
    private void menuItemCancelPressed(ActionEvent ae) {
        Navigator.getInstance().navigateToPanel();
    }

    @FXML
    private void menuItemQuitPressed(ActionEvent ae) {
        System.exit(0);
    }

    @FXML
    public void menuItemLogOutPressed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemLogOut) {
            UserSession.getInstance().resetSession();

            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        }
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

    }
}

