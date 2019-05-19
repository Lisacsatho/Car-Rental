package se.hkr.Scenes.MemberPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se.hkr.BookingSession;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.Booking;
import se.hkr.Model.User.Member;
import se.hkr.Navigator;
import se.hkr.UserSession;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MemberPanelController implements Initializable {
    @FXML
    private VBox containerBookings;
    @FXML
    private Label lblWelcomeMessage,
            lblSsn,
            lblStreet,
            lblZip,
            lblCity,
            lblPhoneNumber,
            lbleMail;
    @FXML
    private Button btnEditMember, btnGo;
    @FXML
    private MenuItem menuItemHelp, menuItemLogOut;
    @FXML
    private DatePicker datePickerStart, datePickerEnd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Member member = null;
        if (UserSession.getInstance().isMember()) {
            member = (Member)UserSession.getInstance().getSessionObject();
        } else {
            Navigator.getInstance().navigateToPanel();
        }

        try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
            List<Booking> bookings = bookingDBHandler.readForMemberSimple(member);
            bookings.forEach((booking -> {
                containerBookings.getChildren().add(buildBookingGUI(booking));
            }));
            lblWelcomeMessage.setText("Welcome " + UserSession.getInstance().getSessionObject().getFirstName() + " " + UserSession.getInstance().getSessionObject().getLastName() + "!");
            lblSsn.setText(UserSession.getInstance().getSessionObject().getSocialSecurityNo());
            lblStreet.setText(UserSession.getInstance().getSessionObject().getAddress().getStreet());
            lblZip.setText(UserSession.getInstance().getSessionObject().getAddress().getZip());
            lblCity.setText(UserSession.getInstance().getSessionObject().getAddress().getState());
            lblPhoneNumber.setText(UserSession.getInstance().getSessionObject().getPhoneNumber());
            lbleMail.setText(UserSession.getInstance().getSessionObject().getEmail());
        } catch (Exception e) {
            Dialogue.alert("Trouble contacting server.");
            System.out.println(e.getMessage());
        }
    }

    private HBox buildBookingGUI(Booking booking) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Insets insets = new Insets(10, 5, 10, 5);
        Label startDate = new Label(simpleDateFormat.format(booking.getStartDate()));
        startDate.setPadding(insets);
        Label endDate = new Label(simpleDateFormat.format(booking.getEndDate()));
        endDate.setPadding(insets);
        Label id = new Label("Booking id: " + booking.getId());
        id.setPadding(insets);

        container.getChildren().addAll(id, startDate, endDate);
        container.getStyleClass().add("highlighted-button");
        container.setPadding(new Insets(2, 2, 2, 2));
        return container;
    }

    public void logOutOption(ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemLogOut) {
            UserSession.getInstance().resetSession();

            Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        }
    }

    public void helpMenu(ActionEvent actionEvent) {

        if (actionEvent.getSource() == menuItemHelp) {

            Navigator.getInstance().navigateTo("CustomerService/CustomerServiceView.fxml");
        }
    }

    public void editMemberInformation(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnEditMember) {
            Navigator.getInstance().navigateTo("EditMember/EditMemberView.fxml");
        }
    }

    public void pressedGo(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnGo) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = format.parse(datePickerStart.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                Date endDate = format.parse(datePickerEnd.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                Date today = new Date();
                if (startDate.after(today) && endDate.after(startDate)) {
                    BookingSession.getInstance().resetSession();
                    BookingSession.getInstance().getSessionObject().setStartDate(startDate);
                    BookingSession.getInstance().getSessionObject().setEndDate(endDate);

                    Navigator.getInstance().navigateTo("ChooseCar/ChooseCarView.fxml");
                } else {
                    Dialogue.alert("Please select valid start and ending dates.");
                }
            } catch (Exception x) {
                x.printStackTrace();
                Dialogue.alert("Choose both starting date and returning date.");
            }

        }
    }
}
