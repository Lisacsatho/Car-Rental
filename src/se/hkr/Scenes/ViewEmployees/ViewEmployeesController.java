package se.hkr.Scenes.ViewEmployees;

import se.hkr.Database.UserDB.EmployeeDBHandler;
import se.hkr.Model.User.Employee;
import se.hkr.Model.User.Member;
import se.hkr.Scenes.ReadController;
import se.hkr.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewEmployeesController implements ReadController<Employee> {


    public void initialize(URL location, ResourceBundle resources) {

        try (EmployeeDBHandler dB = new EmployeeDBHandler()) {

            List<Employee> employees = dB.readForEmployee((Employee) UserSession.getInstance().getLoggedInUser());
            txtAreaBookingHistory.appendText(String.valueOf(bookings));

        } catch (Exception e) {
            e.printStackTrace();
        }


    @Override
    public boolean filter(Employee model) {
        return false;
    }

    @Override
    public void search() {

    }
}
