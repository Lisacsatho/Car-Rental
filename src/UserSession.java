import se.hkr.Model.User.User;

public class UserSession {
    private static UserSession ourInstance = new UserSession();

    public static UserSession getInstance() {
        return ourInstance;
    }

    private UserSession() {

        User loggedInUser;
    }

    public void logIn(User user) {
    }

    public void logOut() {
    }
}
