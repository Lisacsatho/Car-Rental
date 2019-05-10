package se.hkr;

import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;

public class UserSession {
    private static UserSession ourInstance = new UserSession();
    private User loggedInUser;

    public static UserSession getInstance() {
        return ourInstance;
    }

    private UserSession() {

    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void logIn(User user) {
         loggedInUser = user;
    }

    public void logOut() {
        loggedInUser = null;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public boolean isMember() {
        if (isLoggedIn()) {
            return loggedInUser instanceof Member;
        }
        return false;
    }

    public boolean isEmployee() {
        if (isLoggedIn()) {
            return loggedInUser instanceof Employee;
        }
        return false;
    }
}
