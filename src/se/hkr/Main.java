package se.hkr;

import javafx.application.Application;
import javafx.stage.Stage;
import se.hkr.Database.DatabaseConnection;


public class Main extends Application {
    /*
     *   Viktigt meddelande till allmänheten: Använd Facade-klassen Dialogue för att visa Alerts numera.
     * */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Navigator.getInstance().setPrimaryStage(primaryStage);
        Navigator.getInstance().navigateTo("ForgotPassword/ForgotPasswordView.fxml");
        primaryStage.show();
    }

    public static void main(String[] args) {
        DatabaseConnection.getInstance();
        launch(args);
        try {
            DatabaseConnection.getInstance().close();
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
