package se.hkr;

import javafx.scene.control.Alert;

public class Dialogue {
    public static void alert(String prompt) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setHeaderText(prompt);
        alert.showAndWait();
    }
}
