package se.hkr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.hkr.Database.UserDB.UserDBHandler;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();*/
        User rasmus = new Member("971217-6156", "Rasmus", "Nilsson", "info@rasmusnilsson.se", "0793498413", "Storgatan 7A", "0384405");
        User tobias = new Employee("921234-6156", "Tobias", "Andersson", "anderslover@hotmail.com", "0793498413", "Storgatan 7A", 2500.00);
        try (UserDBHandler db = UserDBHandler.getHandlerFor(rasmus)) {
            db.insert(rasmus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
