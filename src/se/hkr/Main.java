package se.hkr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Database.UserDB.UserDBHandler;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;
import se.hkr.Model.Vehicle.Car;

import java.sql.SQLException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
<<<<<<< HEAD
        Parent root = FXMLLoader.load(getClass().getResource("Scenes/RegisterUser/RegisterView.fxml"));
=======
        Parent root = FXMLLoader.load(getClass().getResource("Scenes/ViewCars/ViewCarsView.fxml"));
>>>>>>> 3beea2cf64cede0f3193ce5d5b4f4c507b04e30f
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
