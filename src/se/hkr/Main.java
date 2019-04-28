package se.hkr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;

import java.text.SimpleDateFormat;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();*/
        try (VehicleDBHandler db = new CarDBHandler()){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            db.readAvailableVehicles(df.parse("2009-04-22"), df.parse("2010-04-26"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
