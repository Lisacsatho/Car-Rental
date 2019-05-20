package se.hkr.Database.UserDB;

import se.hkr.HashUtils;
import se.hkr.Model.User.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDBHandler extends UserDBHandler<Employee> {
    @Override
    public void insert(Employee model) {
        System.out.println("Inserted employee!");
    }

    @Override
    public boolean authenticateUser(String email, String hashedPassword) {
        String query = "SELECT * FROM User JOIN Employee ON user.socialSecurityNo = employee.socialSecurityNo WHERE email=? AND password=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, hashedPassword.toString());
            ResultSet set = statement.executeQuery();
            return !buildModels(set).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void update(Employee model) throws SQLException {
        super.update(model);
        String query = "UPDATE employee SET salary=?, role=? WHERE socialSecurityNo=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, model.getSalary());
            if (model instanceof Manager) {
                statement.setString(2, "MANAGER");
            } else {
                statement.setString(2, "EMPLOYEE");
            }
            statement.setString(3, model.getSocialSecurityNo());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Could not update employee table.", e);
        }
    }

    @Override
    public void delete(Employee model) {
        System.out.println("Deleted employee!");
    }

    @Override
    public List<Employee> readAll() throws SQLException {
        String query = "SELECT * FROM employee JOIN user ON user.socialSecurityNo=employee.socialSecurityNo";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
             return buildModels(statement.executeQuery());
        } catch (Exception e) {
            throw new SQLException("Problem reading employees: " + e.getMessage(), e);
        }
    }

    @Override
    public Employee readByPrimaryKey(String key) {
        return null;
    }

    @Override
    public Employee readByEmail(String email) throws SQLException {
        String query = "SELECT * FROM User JOIN Employee ON user.socialSecurityNo=employee.socialSecurityNo WHERE email=? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            List<Employee> employees = buildModels(set);
            if (!employees.isEmpty()) {
                return employees.get(0);
            }
        }
        return null;
    }

    @Override
    public List<Employee> buildModels(ResultSet set) {
        List<Employee> employees = new ArrayList<>();
        try (AddressDBHandler addressDBHandler = new AddressDBHandler()){
            while (set.next()) {
                Address address = addressDBHandler.readByPrimaryKey(Integer.toString(set.getInt("address")));
                if (set.getString("role").equals("Manager")) {
                    Manager manager = new Manager(set.getString("socialSecurityNo"), set.getString("firstName"), set.getString("lastName"), set.getString("email"), set.getString("phoneNo"), address, set.getDouble("salary"));
                    employees.add(manager);
                } else {
                    Employee employee = new Employee(set.getString("socialSecurityNo"), set.getString("firstName"), set.getString("lastName"), set.getString("email"), set.getString("phoneNo"), address, set.getDouble("salary"));
                    employees.add(employee);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }
}
