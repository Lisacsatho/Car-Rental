package se.hkr.Database.VehicleDB;
import se.hkr.Model.Vehicle.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDBHandler extends VehicleDBHandler<Car>{
    @Override
    public void insert(Car model) throws SQLException {
        super.insert(model);
        String insert = "INSERT INTO car VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insert)) {
            insertStatement.setInt(1, model.getId());
            insertStatement.setInt(2, model.getSuitcases());
            insertStatement.setInt(3, model.getCarType().getId());
            insertStatement.execute();
        } catch (Exception e) {
            throw new SQLException(e);
        }
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
        try (Statement statement = connection.createStatement()) {
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
        String query = "SELECT * FROM AllCars WHERE id=? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, Integer.parseInt(key));
            ResultSet set = statement.executeQuery();
            List<Car> result = buildModels(set);
            car = (!result.isEmpty()) ? result.get(0) : car;
        } catch (Exception e) {
            // TODO: Handle error
            e.printStackTrace();
        }
        return car;
    }

    @Override
    public List<Car> buildModels(ResultSet set) {
        List<Car> cars = new ArrayList<>();
        try (FuelTypeDBHandler fuelTypeDBHandler = new FuelTypeDBHandler();
             GearBoxDBHandler gearBoxDBHandler = new GearBoxDBHandler();
             CarTypeDBHandler carTypeDBHandler = new CarTypeDBHandler();
             VehicleBrandDBHandler vehicleBrandDBHandler = new VehicleBrandDBHandler()) {
            while(set.next()) { ;
                FuelType fuelType = fuelTypeDBHandler.readByPrimaryKey(Integer.toString(set.getInt("fuelType")));
                GearBox gearBox = gearBoxDBHandler.readByPrimaryKey(Integer.toString(set.getInt("gearBox")));
                CarType carType = carTypeDBHandler.readByPrimaryKey(Integer.toString(set.getInt("carType")));
                VehicleBrand vehicleBrand = vehicleBrandDBHandler.readByPrimaryKey(Integer.toString(set.getInt("brand")));

                cars.add(new Car(set.getInt("id"), set.getDouble("price"), set.getString("description"), set.getInt("passengers"), fuelType, gearBox, set.getString("modelName"), set.getInt("modelYear"), vehicleBrand, set.getInt("suitcases"), carType));
            }
        } catch (Exception e) {
            // TODO: Handle exception appropriately
            e.printStackTrace();
        }
        return cars;
    }
}
