package se.hkr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.hkr.Model.User.Member;

import java.io.IOException;
import java.util.Stack;

public class Navigator {
    private static Navigator ourInstance = new Navigator();
    private static final String PATH_TO_SCENES = "Scenes/";
    private final String MEMBER_PANEL = "MemberPanel/MemberPanelView.fxml";
    private final String EMPLOYEE_PANEL = "AddCar/AddCarView.fxml";

    private Stack<Scene> previousScenes;
    private Stage primaryStage;

    public static Navigator getInstance() {
        return ourInstance;
    }

    private Navigator() {
        previousScenes = new Stack<>();
    }

    public void navigateToPanel() {
        assert(UserSession.getInstance().isLoggedIn());
        if (UserSession.getInstance().isMember()) {
            if (!((Member) UserSession.getInstance().getLoggedInUser()).isVerified()) {
                navigateTo("VerifyEmail/VerifyEmailView.fxml");
            } else {
                navigateTo(MEMBER_PANEL);
            }
        } else if (UserSession.getInstance().isEmployee()) {
            navigateTo(EMPLOYEE_PANEL);
        }
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
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
                Scene nextScene = new Scene(root);
                String cssTheme = getClass().getResource("Assets/theme.css").toExternalForm();
                nextScene.getStylesheets().add(cssTheme);
                primaryStage.setScene(nextScene);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Figure out proper handling
        }
    }

    private void setScene(Scene scene) {
        if (primaryStage != null) {
            primaryStage.setScene(scene);
        }
    }
}
