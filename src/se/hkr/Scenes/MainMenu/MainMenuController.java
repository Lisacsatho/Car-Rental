package se.hkr.Scenes.MainMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import se.hkr.BookingSession;
import se.hkr.Database.UserDB.UserDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.User.User;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MainMenuController {

    @FXML
    private TextField txtFldUsername,
                      txtFldPassword;

    @FXML
    private Button btnSignUp,
                   btnLogin,
                   btnGo;

    @FXML
    private DatePicker datePicStart,
                       datePicReturn;

    public void btnSignUpPressed(ActionEvent ae) {
        Navigator.getInstance().navigateTo("RegisterUser/RegisterView.fxml");
    }

    public void btnLoginPressed(ActionEvent ae) {
        String email = txtFldUsername.getText();
        String password = txtFldPassword.getText();
        try {
            User user = UserDBHandler.authenticate(email, password);
            if (user != null) {
                UserSession.getInstance().logIn(user);
                Navigator.getInstance().navigateToPanel();
            } else {
                Dialogue.alert("No user found");
            }
        } catch (SQLException e) {
            Dialogue.alert("No user found");
        }
    }

    public void btnGoPressed(ActionEvent ae) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = format.parse(datePicStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            Date endDate = format.parse(datePicReturn.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            Date today = new Date();
            if (startDate.after(today) && endDate.after(startDate)) {
                BookingSession.getInstance().resetSession();
                BookingSession.getInstance().getBooking().setStartDate(startDate);
                BookingSession.getInstance().getBooking().setEndDate(endDate);

                Navigator.getInstance().navigateTo("ChooseCar/ChooseCarView.fxml");
            } else {
                Dialogue.alert("Please select valid start and ending dates.");
            }
        } catch (Exception x) {
            Dialogue.alert("Choose both starting date and returning date.");
        }
    }
}


