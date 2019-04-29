package se.hkr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class Navigator {
    private static Navigator ourInstance = new Navigator();
    private static final String PATH_TO_SCENES = "Scenes/";

    private Stack<Scene> previousScenes;
    private Stage primaryStage;

    public static Navigator getInstance() {
        return ourInstance;
    }

    private Navigator() {
        previousScenes = new Stack<>();
    }

    public void navigateTo(String scene) {
        previousScenes.push(primaryStage.getScene());
        setScene(scene);
    }

    public void goBack() {
        setScene(previousScenes.pop());
    }

    private void setScene(String scene) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(PATH_TO_SCENES + scene));
            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
            }
        } catch (IOException e) {
            // TODO: Figure out proper handling
        }
    }

    private void setScene(Scene scene) {
        if (primaryStage != null) {
            primaryStage.setScene(scene);
        }
    }
}
