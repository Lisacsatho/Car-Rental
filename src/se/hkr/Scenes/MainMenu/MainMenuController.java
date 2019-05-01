package se.hkr.Scenes.MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    TextField txtFldUsername,
            txtFldPassword;

    @FXML
    Button btnSignUp,
            btnLogin,
            btnGo;

    public void btnSignUpPressed(ActionEvent ae){

        try {
            if (ae.getSource() == btnSignUp) {
                Stage stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("EditMemberView.fxml"))));
            }
        } catch (Exception x) {


        }


    }

    public void btnLoginPressed(ActionEvent ae){


    }

    public void btnGoPressed(ActionEvent ae){

    }



}
