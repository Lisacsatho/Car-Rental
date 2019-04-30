package se.hkr;

import javafx.application.Application;
import javafx.stage.Stage;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Database.UserDB.UserDBHandler;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();*/
        try (UserDBHandler db = new MemberDBHandler()) {
            db.connect();
            Member lisa = new Member("940412-4433", "Lisa", "Csatho", "lisa@icloud.com", "079-343111", new Address("Söder 2", "43453", "Stockholm"), "lisalisa", "9705894392");
            db.insert(lisa);
            System.out.println("Inserted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
