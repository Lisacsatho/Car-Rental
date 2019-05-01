package se.hkr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource("Scenes/MainMenu/MainMenuView.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));

        Navigator.getInstance().setPrimaryStage(primaryStage);
        Navigator.getInstance().navigateTo("MainMenu/MainMenuView.fxml");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
