package se.hkr.Scenes.ChooseCar;

import javafx.fxml.Initializable;
import se.hkr.BookingSession;
import se.hkr.Database.VehicleDB.CarDBHandler;
import se.hkr.Database.VehicleDB.VehicleDBHandler;
import se.hkr.Scenes.ReadController;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseCarController implements ReadController, Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        try(CarDBHandler carDBHandler = new CarDBHandler()){
            System.out.println(carDBHandler.readAvailableVehicles(BookingSession.getInstance().getBooking().getStartDate(),
                    BookingSession.getInstance().getBooking().getEndDate()));

        } catch (Exception e){
          e.printStackTrace();
        }
    }

    @Override
    public void filter() {

    }

    @Override
    public void search() {

    }
}
