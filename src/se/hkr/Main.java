package se.hkr;

import javafx.application.Application;
import javafx.stage.Stage;
import se.hkr.Database.VehicleDB.CarDBHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();*/
        try (CarDBHandler db = new CarDBHandler()) {
            db.connect();
            System.out.println(db.readByPrimaryKey("1").getModel());
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
