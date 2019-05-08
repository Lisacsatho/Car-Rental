package se.hkr;

import javafx.application.Application;

import javafx.stage.Stage;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Model.Vehicle.Car;


public class Main extends Application {
    /*
    *   Viktigt meddelande till allmänheten: Använd Facade-klassen Dialogue för att visa Alerts numera.
    * */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Navigator.getInstance().setPrimaryStage(primaryStage);

        Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
