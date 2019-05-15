package se.hkr;

import javafx.application.Application;
import javafx.stage.Stage;
import se.hkr.Database.BookingDBHandler;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Email.Email;
import se.hkr.Database.Database;
import se.hkr.Database.DatabaseConnection;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.Car;

import java.util.List;


public class Main extends Application {
    /*
     *   Viktigt meddelande till allmänheten: Använd Facade-klassen Dialogue för att visa Alerts numera.
     * */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Navigator.getInstance().setPrimaryStage(primaryStage);
        
//        Navigator.getInstance().navigateTo("AddCar/AddCarView.fxml");
//        Navigator.getInstance().navigateTo("ViewCars/ViewCarsView.fxml");

        Navigator.getInstance().navigateTo("ViewCars/ViewCarsView.fxml");

  // primaryStage.show();
   //  Email email = new Email("helgefreiman@hotmail.com", "hey dickhead", "Hey you. Där sitter du i en grå soffa och ser lagom trött, sliten möjligen bakis ut? KAN du ha vart på BG igår? hmmmm... ett tips. Gå och lägg dig breve din heta BF ;););) ");
  //    email.send();

     //   Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
   //     primaryStage.show();

        Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        //Navigator.getInstance().navigateTo("ViewBookings/ViewBookingsView.fxml");

        //Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        Navigator.getInstance().navigateTo("ViewMembers/ViewMembersView.fxml");
        primaryStage.show();

    }

    public static void main(String[] args) {
        DatabaseConnection.getInstance();
        launch(args);
        try {
            DatabaseConnection.getInstance().close();
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
