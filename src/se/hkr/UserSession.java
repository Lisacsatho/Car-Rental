package se.hkr;

import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;

public class UserSession extends Session<User> {
    private static UserSession ourInstance = new UserSession();

    public static UserSession getInstance() {
        return ourInstance;
    }

    private UserSession() {

    }

    @Override
    public void resetSession() {
        sessionObject = null;
    }

    public void logIn(User user) {
         sessionObject = user;
    }

    public boolean isLoggedIn() {
        return sessionObject != null;
    }

    public boolean isMember() {
        if (isLoggedIn()) {
            return sessionObject instanceof Member;
        }
        return false;
    }

    public boolean isEmployee() {
        if (isLoggedIn()) {
            return sessionObject instanceof Employee;
        }
        return false;
    }
}
