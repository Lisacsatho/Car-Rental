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
        Navigator.getInstance().setPrimaryStage(primaryStage);
        Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
