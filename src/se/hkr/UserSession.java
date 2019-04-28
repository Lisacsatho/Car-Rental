package se.hkr;

import se.hkr.Model.User.User;

public class UserSession {
    private static UserSession ourInstance = new UserSession();
    private User loggedInUser;

    public static UserSession getInstance() {
        return ourInstance;
    }

    private UserSession() {

    }

    public void logIn(User user) {
         loggedInUser = user;
    }

    public void logOut() {
        loggedInUser = null;
    }
}
