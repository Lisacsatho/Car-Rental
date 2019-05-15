package se.hkr.Database.VehicleDB;
import se.hkr.Model.Booking;
import se.hkr.Model.Vehicle.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarDBHandler extends VehicleDBHandler<Car>{
    @Override
    public List<? extends Vehicle> readAvailable(Date startDate, Date endDate) throws SQLException {
        List<Car> cars;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String subQuery = "SELECT vehicleId FROM bookinghasvehicle JOIN booking ON booking.id = bookinghasvehicle.bookingId WHERE (startDate BETWEEN ? AND ?) OR (endDate BETWEEN ? AND ?) OR (startDate <= ?) AND (endDate >= ?)";
        String readAvailable = String.format("SELECT * FROM AllCars WHERE id NOT IN (%s) AND readyForRent=1 AND inInventory=1", subQuery);
        try (PreparedStatement statement = connection.prepareStatement(readAvailable)) {
            statement.setString(1, format.format(startDate));
            statement.setString(2, format.format(endDate));
            statement.setString(3, format.format(startDate));
            statement.setString(4, format.format(endDate));
            statement.setString(5, format.format(startDate));
            statement.setString(6, format.format(endDate));
            cars = buildModels(statement.executeQuery());
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return cars;
    }

    @Override
    public List<Car> readForBookingSpecific(Booking booking) throws SQLException {
        String query = "SELECT AllCars.* FROM AllCars JOIN bookinghasvehicle ON id=bookinghasvehicle.vehicleId WHERE bookinghasvehicle.bookingId=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getId());
            ResultSet set = statement.executeQuery();
            return buildModels(set);
        } catch (Exception e) {
            throw new SQLException("Could not get cars for booking.", e);
        }
    }

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
    public void update(Car model) throws SQLException {
        try {
            super.update(model);
            // do eventual update here, nothing to do for now though
        } catch (Exception e) {
            throw new SQLException("Could not update car table", e);
        }
    }

    @Override
    public void inactivate(Car model) throws SQLException {
        super.inactivate(model);
    }

    @Override
    public void delete(Car model) {

    }

    @Override
    public List<Car> readAll() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM AllCars WHERE inInventory=1";
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(query);
            cars = buildModels(set);
        } catch (Exception e) {
            // TODO: Handle error
            e.printStackTrace();
        }
        return cars;
    }

    public List<Car> readAllIncludingInactive() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM AllCars";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            cars.addAll(buildModels(statement.executeQuery()));
        } catch (Exception e) {
            throw new SQLException("Could not fetch cars", e);
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
            if (!result.isEmpty()) {
                car = result.get(0);
            }
        } catch (Exception e) {
            // TODO: Handle error
            e.printStackTrace();
        }
        return car;
    }

    @Override
    public List<Car> buildModels(ResultSet set) {
        List<Car> cars = new ArrayList<>();
        try (VehicleOptionDBHandler vehicleOptionDBHandler = new VehicleOptionDBHandler()) {
            while(set.next()) {
                FuelType fuelType = FuelTypeDBHandler.buildModelWithColumnNames(set, "fuelTypeId", "fuelTypeName");
                GearBox gearBox = GearBoxDBHandler.buildModelWithColumnNames(set, "gearBoxId", "gearBoxName");
                CarType carType = CarTypeDBHandler.buildModelWithColumnNames(set, "carTypeId", "carTypeName");
                VehicleBrand vehicleBrand = VehicleBrandDBHandler.buildModelWithColumnNames(set, "brandId", "brandName");

                Car car = new Car(set.getInt("id"), set.getDouble("price"), set.getString("description"), set.getInt("passengers"), fuelType, gearBox, set.getString("modelName"), set.getInt("modelYear"), vehicleBrand, set.getInt("suitcases"), carType, set.getBoolean("readyForRent"));
                List<VehicleOption> vehicleOptions = vehicleOptionDBHandler.readForVehicle(car);
                car.setVehicleOptions(vehicleOptions);
                cars.add(car);
            }
        } catch (Exception e) {
            // TODO: Handle exception appropriately
            e.printStackTrace();
        }
        return cars;
    }

    public static Car buildWithColumnNames(ResultSet set) {
        Car car = null;
        try (VehicleOptionDBHandler vehicleOptionDBHandler = new VehicleOptionDBHandler()) {
            FuelType fuelType = FuelTypeDBHandler.buildModelWithColumnNames(set, "fuelTypeId", "fuelTypeName");
            GearBox gearBox = GearBoxDBHandler.buildModelWithColumnNames(set, "gearBoxId", "gearBoxName");
            CarType carType = CarTypeDBHandler.buildModelWithColumnNames(set, "carTypeId", "carTypeName");
            VehicleBrand vehicleBrand = VehicleBrandDBHandler.buildModelWithColumnNames(set, "brandId", "brandName");

            car = new Car(set.getInt("id"), set.getDouble("price"), set.getString("description"), set.getInt("passengers"), fuelType, gearBox, set.getString("modelName"), set.getInt("modelYear"), vehicleBrand, set.getInt("suitcases"), carType, set.getBoolean("readyForRent"));
            List<VehicleOption> vehicleOptions = vehicleOptionDBHandler.readForVehicle(car);
            car.setVehicleOptions(vehicleOptions);
        } catch (Exception e) {
            // TODO: Handle exception appropriately
            e.printStackTrace();
        }
        return car;
    }
}
