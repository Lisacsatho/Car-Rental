package se.hkr.Database.VehicleDB;

import se.hkr.Database.DatabaseConnection;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.CarType;
import se.hkr.Model.Vehicle.FuelType;
import se.hkr.Model.Vehicle.GearBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDBHandler extends VehicleDBHandler<Car>{

    @Override
    public void insert(Car model) {

    }

    @Override
    public void update(Car model) {

    }

    @Override
    public void delete(Car model) {

    }

    @Override
    public List<Car> readAll() {
        List<Car> cars = new ArrayList<>();
        String query = String.format("SELECT * FROM AllCars");
        try (Statement statement = databaseConnection.getConnection().createStatement()) {
            ResultSet set = statement.executeQuery(query);
            cars = buildModels(set);
        } catch (Exception e) {
            // TODO: Handle error
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public Car readByPrimaryKey(String key) {
        Car car = null;
        String query = String.format("SELECT * FROM AllCars WHERE vehicleId=%s LIMIT 1", key);
        try (Statement statement = databaseConnection.getConnection().createStatement()){
            ResultSet set = statement.executeQuery(query);
            car = buildModels(set).get(0);
        } catch (Exception e) {
            // TODO: Handle error
            e.printStackTrace();
        }
        return car;
    }



    @Override
    public List<Car> buildModels(ResultSet set) {
        List<Car> cars = new ArrayList<>();
        try {
            while(set.next()) {
                FuelType fuelType = new FuelType(set.getInt("fuelTypeId"), set.getString("fuelTypeName"));
                GearBox gearBox = new GearBox(set.getInt("gearBoxId"), set.getString("gearBoxName"));
                CarType carType = new CarType(set.getInt("carTypeId"), set.getString("carTypeName"));

                cars.add(new Car( set.getInt("vehicleId"), set.getDouble("vehiclePrice"),
                        set.getString("vehicleBrandName"), set.getString("vehicleModelName"),
                        set.getInt("vehicleModelYear"), set.getString("vehicleDescription"),
                        set.getInt("vehiclePassengers"), fuelType, gearBox,
                        set.getInt("suitcases"), carType));
            }
        } catch (SQLException e) {
            // TODO: Handle exception appropriately
            e.printStackTrace();
        }
        return cars;
    }
}
