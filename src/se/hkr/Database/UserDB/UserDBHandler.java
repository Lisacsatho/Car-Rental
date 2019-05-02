package se.hkr.Database.UserDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.HashUtils;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class UserDBHandler <U extends User> extends ModelDBHandler<U> {

    public static <U extends User> UserDBHandler getHandlerFor(U model) {
        if (model instanceof Member) {
            return new MemberDBHandler();
        } else {
            return new EmployeeDBHandler();
        }
    }

    public abstract User authenticate(String email, String password);

    @Override
    public void insert(U model) throws SQLException {
        try (AddressDBHandler addressDB = new AddressDBHandler();
             Statement statement = connection.createStatement()) {
            addressDB.connect();
            addressDB.insert(model.getAddress());

            StringBuilder hashedPassword = new StringBuilder();
            for (byte b : model.getPassword()) {
                hashedPassword.append(b);
            }
            String insert = String.format("INSERT INTO user VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d)",
                                         model.getSocialSecurityNo(),
                                         model.getFirstName(),
                                         model.getLastName(),
                                         model.getPhoneNumber(),
                                         model.getEmail(),
                                         hashedPassword.toString(),
                                         model.getAddress().getId());
            statement.execute(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(U model) {

    }

    @Override
    public void delete(U model) {
        
    }
}
