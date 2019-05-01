package se.hkr.Scenes.MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import se.hkr.BookingSession;
import se.hkr.Navigator;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class MainMenuController {

    @FXML
    TextField txtFldUsername,
            txtFldPassword;

    @FXML
    Button btnSignUp,
            btnLogin,
            btnGo;

    @FXML
    DatePicker datePicStart,
            datePicReturn;


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

        if (ae.getSource()==btnGo){



            try {

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = format.parse(datePicStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                Date endDate = format.parse(datePicReturn.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                BookingSession.getInstance().resetSession();
                BookingSession.getInstance().getBooking().setStartDate(startDate);
                BookingSession.getInstance().getBooking().setEndDate(endDate);



                Navigator.getInstance().navigateTo("ChooseCar/ChooseCarView.fxml");
            }
            catch (Exception x){
                x.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input error!");
                alert.setHeaderText("Choose both starting date and returning date.");
                alert.showAndWait();
            }
        }

        }



    }


