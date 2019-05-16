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

    public boolean userExists(String socialSecurityNo) throws SQLException {
        String query = "SELECT * FROM user WHERE socialSecurityNo=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, socialSecurityNo);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                return true;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return false;
    }

    public void updatePassword(String key, String password) throws SQLException {
        String query = "UPDATE user SET password=? WHERE email=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, password);
            statement.setString(2, key);
            if (statement.executeUpdate() != 1) {
                throw new SQLException("No user found with that email.");
            }
        } catch (Exception e) {

        }
    }

    protected abstract boolean authenticateUser(String email, String hashedPassword);

    public abstract U readByEmail(String email) throws SQLException;

    @Override
    public void insert(U model) throws SQLException {
        try (AddressDBHandler addressDB = new AddressDBHandler();
             Statement statement = connection.createStatement()) {
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
    public void update(U model) throws SQLException {
        String query = "UPDATE user SET firstName=?, lastName=?, phoneNo=?, email=? WHERE socialSecurityNo=?";
        try (AddressDBHandler addressDBHandler = new AddressDBHandler();
            PreparedStatement statement = connection.prepareStatement(query)) {
            addressDBHandler.update(model.getAddress());
            statement.setString(1, model.getFirstName());
            statement.setString(2, model.getLastName());
            statement.setString(3, model.getPhoneNumber());
            statement.setString(4, model.getEmail());
            statement.setString(5, model.getSocialSecurityNo());
            if (statement.executeUpdate() != 1) {
                throw new SQLException("Cannot update user: " + model.getSocialSecurityNo());
            }
        } catch (Exception e) {
            throw new SQLException("Cannot update user: " + model.getSocialSecurityNo(), e);
        }
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
