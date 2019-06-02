package se.hkr.Scenes.AddEmployee;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import se.hkr.Database.UserDB.EmployeeDBHandler;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Database.UserDB.UserDBHandler;
import se.hkr.Dialogue;
import se.hkr.HashUtils;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Manager;
import se.hkr.Model.User.User;
import se.hkr.Navigator;

import java.util.regex.Pattern;

public class AddEmployeeController {

    @FXML
    private TextField txtFldSocialSecurityNo,
                      txtFldEmail,
                      txtFldFirstName,
                      txtFldLastName,
                      txtFldPassword,
                      txtFldConfirmPassword,
                      txtFldPhoneNo,
                      txtFldStreet,
                      txtFldZip,
                      txtFldState,
                      txtFldSalary;
    @FXML
    private CheckBox checkBoxIsManager;

    @FXML
    private void buttonAddEmployeePressed() {
        if (validate()) {
            try (EmployeeDBHandler employeeDBHandler = new EmployeeDBHandler()) {
                String ssn = txtFldSocialSecurityNo.getText();
                String email = txtFldEmail.getText();
                String firstName = txtFldFirstName.getText();
                String lastName = txtFldLastName.getText();
                String password = HashUtils.hashPassword(txtFldPassword.getText());
                String phoneNo = txtFldPhoneNo.getText();
                String street = txtFldStreet.getText();
                String zip = txtFldZip.getText();
                String state = txtFldState.getText();
                double salary = Double.parseDouble(txtFldSalary.getText());
                Address address = new Address(street, zip, state);
                if (checkBoxIsManager.isSelected()) {
                    Manager manager = new Manager(ssn, firstName, lastName, email, phoneNo, address, password, salary);
                    employeeDBHandler.insert(manager);
                } else {
                    Employee employee = new Employee(ssn, firstName, lastName, email, phoneNo, address, password, salary);
                    employeeDBHandler.insert(employee);
                }
                Dialogue.inform("Employee was added to the database.");
                Navigator.getInstance().navigateToPanel();
            } catch (Exception e) {
                e.printStackTrace();
                Dialogue.alert(e.getMessage());
            }
        }
    }

    private boolean validate() {
        try (EmployeeDBHandler employeeDBHandler = new EmployeeDBHandler()) {
            if (!Pattern.matches("[0-9][0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]", txtFldSocialSecurityNo.getText())) {
                Dialogue.alert("Please enter correct social security no. format YYMMDD-XXXX.");
                return false;
            } else if (!Pattern.matches("[1-9][1-9][1-9] [1-9][1-9]", txtFldZip.getText())) {
                Dialogue.alert("Please enter zip code in XXX XX format.");
                return false;
            } else if (!Pattern.matches(".*[@].*[.].*", txtFldEmail.getText())) {
                Dialogue.alert("Invalid email address.");
                return false;
            } else if (employeeDBHandler.userExists(txtFldSocialSecurityNo.getText())) {
                Dialogue.alert("User already exists with that social security no.");
                return false;
            } else if (UserDBHandler.readAbstractByEmail(txtFldEmail.getText()) != null) {
                Dialogue.alert("Usewr already exists with that email.");
                return false;
            } else if (!txtFldPassword.getText().equals(txtFldConfirmPassword.getText())) {
                Dialogue.alert("Passwords did not match.");
                return false;
            } else {
                try {
                    Double.parseDouble(txtFldSalary.getText());
                } catch (NumberFormatException e) {
                    Dialogue.alert("Enter a valid salary.");
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            Dialogue.alert(e.getMessage());
        }
        return false;
    }
}
