package se.hkr.Scenes.MemberPanel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.Booking;
import se.hkr.Model.User.Member;
import se.hkr.UserSession;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class MemberPanelController  implements Initializable {
    @FXML
    private VBox containerBookings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (BookingDBHandler bookingDBHandler = new BookingDBHandler()) {
            List<Booking> bookings = bookingDBHandler.readForMemberSimple((Member)UserSession.getInstance().getSessionObject());
            bookings.forEach((booking -> {
                containerBookings.getChildren().add(buildBooking(booking));
            }));
        } catch (Exception e) {
            Dialogue.alert("Trouble contacting server.");
            System.out.println(e.getMessage());
        }
    }

    public HBox buildBooking(Booking booking) {
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
        System.out.println(booking.getId());

        container.getChildren().addAll(id, startDate, endDate);
        container.getStyleClass().add("highlighted-button");
        container.setPadding(new Insets(2, 2, 2, 2));
        return container;
    }
}
