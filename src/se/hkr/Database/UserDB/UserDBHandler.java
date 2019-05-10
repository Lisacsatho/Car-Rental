package se.hkr.Database.UserDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.HashUtils;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;

import java.sql.PreparedStatement;
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

    public static User authenticate(String email, String password) throws SQLException {
        String hashedPassword = HashUtils.hashPassword(password);
        try (MemberDBHandler memberDBHandler = new MemberDBHandler();
             EmployeeDBHandler employeeDBHandler = new EmployeeDBHandler()) {
            if (memberDBHandler.authenticateUser(email, hashedPassword)) {
                return memberDBHandler.readByEmail(email);
            } else if (employeeDBHandler.authenticateUser(email, hashedPassword)) {
                return employeeDBHandler.readByEmail(email);
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return null;
    }

    protected abstract boolean authenticateUser(String email, String hashedPassword);

    protected abstract U readByEmail(String email) throws SQLException;

    @Override
    public void insert(U model) throws SQLException {
        try (AddressDBHandler addressDB = new AddressDBHandler();
             Statement statement = connection.createStatement()) {
            addressDB.connect();
            addressDB.insert(model.getAddress());

            String insert = String.format("INSERT INTO user VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d)",
                                         model.getSocialSecurityNo(),
                                         model.getFirstName(),
                                         model.getLastName(),
                                         model.getPhoneNumber(),
                                         model.getEmail(),
                                         model.getPassword(),
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
    public void delete(U model) throws SQLException {
        String delete = "DELETE FROM user WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(delete)) {
            statement.setString(1, model.getSocialSecurityNo());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Could not delete user.", e);
        }
    }
}
