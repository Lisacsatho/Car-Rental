package se.hkr.Scenes.EditMember;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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

    Button btnSave;


    public void btnSavePressed(ActionEvent ae){


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}
