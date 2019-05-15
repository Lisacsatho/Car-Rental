package se.hkr.Scenes.ForgotPassword;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.Email.Email;
import se.hkr.HashUtils;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;
import se.hkr.Navigator;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ForgotPasswordController implements Initializable {

    @FXML
    private TextField txtFieldMail;

    @FXML
    private Button btnSend;

    /*private void btnSendPressed(ActionEvent ae) {

        txtFieldMail.getText();

        if(MemberDBHandler.verifyEmail(txtFieldMail))
            try (MemberDBHandler memberDBHandler = new MemberDBHandler()) {

                member.setVerificationCode(HashUtils.generateCode(member.getEmail()));
            memberDBHandler.insert(member);
            Email email = new Email(member.getEmail(), "Password reset | RentAll", "Please verify your email using the following code: " + member.getVerificationCode());
            email.send();
            Dialogue.alert("Please check your email for the reset code");
            Navigator.getInstance().goBack();
        } else{
            Dialogue.alert("Your e-mail is not registered in our system. Try again och sign up to become a member.");
        }
        catch(Exception e){
            e.printStackTrace();
            Dialogue.alert("Database connection failed, please try again later.");
        }


    }*/



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
