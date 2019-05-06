package se.hkr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.hkr.Database.UserDB.EmployeeDBHandler;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Database.UserDB.UserDBHandler;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.User;

import java.nio.charset.StandardCharsets;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Navigator.getInstance().setPrimaryStage(primaryStage);
        Navigator.getInstance().navigateTo("RegisterUser/RegisterView.fxml");

        primaryStage.show();
        System.out.println(HashUtils.hashPassword("dogs"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
