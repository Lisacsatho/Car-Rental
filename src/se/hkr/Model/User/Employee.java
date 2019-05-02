package se.hkr.Model.User;

public class Employee extends User {
    private double salary;

    public Employee(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, Address address, byte[] password, double salary) {
        super(socialSecurityNo, firstName, lastName, email, phoneNumber, address, password);
        this.salary = salary;
    }

    public Employee(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, Address address, double salary) {
        super(socialSecurityNo, firstName, lastName, email, phoneNumber, address);
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary > 0) {
            this.salary = salary;
        } else {
            salary = 0;
        }
    }
}