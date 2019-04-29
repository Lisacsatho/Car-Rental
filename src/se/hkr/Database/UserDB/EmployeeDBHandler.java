package se.hkr.Database.UserDB;

import se.hkr.Model.User.Employee;

import java.sql.ResultSet;
import java.util.List;

public class EmployeeDBHandler extends UserDBHandler<Employee> {
    @Override
    public void insert(Employee model) {
        System.out.println("Inserted employee!");
    }

    @Override
    public void update(Employee model) {
        System.out.println("Updated employee!");
    }

    @Override
    public void delete(Employee model) {
        System.out.println("Deleted employee!");
    }

    @Override
    public List<Employee> readAll() {
        return null;
    }

    @Override
    public Employee readByPrimaryKey(String key) {
        return null;
    }

    @Override
    public List<Employee> buildModels(ResultSet set) {
        return null;
    }
}
