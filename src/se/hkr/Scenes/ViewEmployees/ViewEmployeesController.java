package se.hkr.Scenes.ViewEmployees;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import se.hkr.Database.UserDB.EmployeeDBHandler;
import se.hkr.Database.UserDB.MemberDBHandler;
import se.hkr.Dialogue;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Scenes.ReadController;
import se.hkr.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ViewEmployeesController implements ReadController<Employee>, Initializable {

    private ObservableList<Employee> allEmployees;
    private ObservableList<Employee> matchingEmployees;

    @FXML
    Button btnSearch,
            btnSaveEmployee;

    @FXML
    TableColumn colSocialSecurityNo,
            colName,
            colRole;

    @FXML
    Label lblSocialSecurityNo;

    @FXML
    TextField txtFldSearch,
            txtFldFirstName,
            txtFldLastName,
            txtFldPhoneNo,
            txtFldDriversLicenseNo,
            txtFldEmail,
            txtFldAddress,
            txtFldCity,
            txtFldZip,
            txtFldSalary;

    @FXML
    TableView<Employee> tblEmployees;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colSocialSecurityNo.setCellValueFactory(new PropertyValueFactory<Member, String>("socialSecurityNo"));
        colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {

            @Override
            public ObservableValue call(TableColumn.CellDataFeatures param) {
                return new SimpleStringProperty(((Employee) param.getValue()).getFullName());
            }
        });
        colRole.setCellValueFactory(new PropertyValueFactory<Employee, String>("role"));

        updateEmployeeList();
        tblEmployees.setItems(matchingEmployees);

        tblEmployees.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            displayEmployee(newValue);
        });
    }

    private void updateEmployeeList() {
        try (EmployeeDBHandler employeeDBHandler = new EmployeeDBHandler()) {
            allEmployees = FXCollections.observableArrayList(employeeDBHandler.readAll());
            if (matchingEmployees == null) {
                matchingEmployees = FXCollections.observableArrayList();
                matchingEmployees.addAll(allEmployees);
            } else {
                matchingEmployees.clear();
                matchingEmployees.addAll(allEmployees);
            }
        } catch (Exception e) {
            Dialogue.alert("Could not connect to database." + e.getMessage());
        }
    }

    private void displayEmployee(Employee employee) {

        try {
            lblSocialSecurityNo.setText(employee.getSocialSecurityNo());
            txtFldFirstName.setText(employee.getFirstName());
            txtFldLastName.setText(employee.getLastName());
            txtFldEmail.setText(employee.getEmail());
            txtFldPhoneNo.setText(employee.getPhoneNumber());
            txtFldCity.setText(employee.getAddress().getState());
            txtFldAddress.setText(employee.getAddress().getStreet());
            txtFldZip.setText(employee.getAddress().getZip());
            txtFldSalary.setText(String.valueOf(employee.getSalary()));

        } catch (NullPointerException e) {
            resetDisplay();
        }

    }

    private void updateEmployee(Employee employee) {
        if (!Pattern.matches("[1-9][1-9][1-9] [1-9][1-9]", txtFldZip.getText())) {
            Dialogue.alert("Zip code invalid, please use format XXX XX.");
        } else if (!Pattern.matches(".*[@].*[.].*", txtFldEmail.getText())) {
            Dialogue.alert("Email invalid, must include a @ and a dot.");
        } else {
            employee.setFirstName(txtFldFirstName.getText());
            employee.setLastName(txtFldLastName.getText());
            employee.setPhoneNumber(txtFldPhoneNo.getText());
            employee.setEmail(txtFldEmail.getText());
            employee.getAddress().setState(txtFldCity.getText());
            employee.getAddress().setStreet(txtFldAddress.getText());
            employee.getAddress().setZip(txtFldZip.getText());

            try (EmployeeDBHandler employeeDBHandler = new EmployeeDBHandler()) {
                employeeDBHandler.update(employee);
            } catch (Exception e) {
                Dialogue.alert("Something went wrong when updating member in database." + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnSaveEmployeePressed() {
        if (tblEmployees.getSelectionModel().getSelectedItem() != null) {
            if (Dialogue.alertOk("Are you sure?")) {
                updateEmployee(tblEmployees.getSelectionModel().getSelectedItem());
                updateEmployeeList();
                resetDisplay();
                Dialogue.inform("Employee was updated.");
            }
        } else {
            Dialogue.alert("Please choose an employee to update.");
        }
    }

    private void resetDisplay() {
        lblSocialSecurityNo.setText("Social security no.");
        txtFldFirstName.clear();
        txtFldLastName.clear();
        txtFldEmail.clear();
        txtFldPhoneNo.clear();
        txtFldDriversLicenseNo.clear();
        txtFldAddress.clear();
        txtFldCity.clear();
        txtFldZip.clear();
    }

    @Override
    public boolean filter(Employee model) {
        return false;
    }


    @Override
    public void search() {
        String key = txtFldSearch.getText().trim();
        matchingEmployees.clear();
        if (key.equals("")) {
            matchingEmployees.addAll(allEmployees);
        } else {
            for (Employee employee : allEmployees) {
                if (employee.matches(key)) {
                    matchingEmployees.add(employee);
                }
            }
        }
    }
}