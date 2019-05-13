package se.hkr;

import javafx.application.Application;
import javafx.stage.Stage;
import se.hkr.Email.Email;
import se.hkr.Database.Database;
import se.hkr.Database.DatabaseConnection;


public class Main extends Application {
    /*
     *   Viktigt meddelande till allmänheten: Använd Facade-klassen Dialogue för att visa Alerts numera.
     * */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Navigator.getInstance().setPrimaryStage(primaryStage);
        

//        Navigator.getInstance().navigateTo("AddCar/AddCarView.fxml");
//        Navigator.getInstance().navigateTo("ViewCars/ViewCarsView.fxml");

        
        primaryStage.show();
   //  Email email = new Email("helgefreiman@hotmail.com", "hey dickhead", "Hey you. Där sitter du i en grå soffa och ser lagom trött, sliten möjligen bakis ut? KAN du ha vart på BG igår? hmmmm... ett tips. Gå och lägg dig breve din heta BF ;););) ");
  //    email.send();

     //   Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
   //     primaryStage.show();

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
