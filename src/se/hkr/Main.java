package se.hkr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Model.Vehicle.Car;

import java.text.SimpleDateFormat;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();*/
        try (CarDBHandler db = new CarDBHandler()) {
            System.out.println(db.readByPrimaryKey("1").getModel());
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
