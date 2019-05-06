package se.hkr;

import javafx.application.Application;

import javafx.stage.Stage;


public class Main extends Application {

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
