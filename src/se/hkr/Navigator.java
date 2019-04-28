package se.hkr;

import javafx.stage.Stage;
import java.util.Stack;

public class Navigator {
    private static Navigator ourInstance = new Navigator();

    public static Navigator getInstance() {
        return ourInstance;
    }

    private Navigator() {

        Stack <String> previousScenes = new Stack<>();
        Stage primaryStage;
    }

    public void navigateTo(String scene) {
    }

    public void goBack() {
    }
}
