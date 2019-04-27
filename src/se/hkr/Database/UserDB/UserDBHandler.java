package se.hkr.Database.UserDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;

public abstract class UserDBHandler <U extends User> extends ModelDBHandler<U> {

    public static <U extends User> UserDBHandler getHandlerFor(U model) {
        if (model instanceof Member) {
            return new MemberDBHandler();
        } else {
            return new EmployeeDBHandler();
        }
    }

    public U authenticate() {
        // TODO: Add authentication code here
        return null;
    }

    @Override
    public void insert(U model) {

    }

    @Override
    public void update(U model) {

    }

    @Override
    public void delete(U model) {
        
    }
}
