package se.hkr.Database.VehicleDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Vehicle.CarType;
import se.hkr.Model.Vehicle.FuelType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarTypeDBHandler extends ModelDBHandler<CarType> {
    @Override
    public void insert(CarType model) throws SQLException {

    }

    @Override
    public void update(CarType model) throws SQLException {

    }

    @Override
    public void delete(CarType model) throws SQLException {

    }

    @Override
    public List<CarType> readAll() throws SQLException {
        List<CarType> carTypes = new ArrayList<>();
        String query = String.format("SELECT * FROM CarType");
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(query);
            carTypes = buildModels(set);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return carTypes;
    }

    @Override
    public CarType readByPrimaryKey(String key) throws SQLException {
        String read = String.format("SELECT * FROM cartype WHERE id=%s LIMIT 1", key);
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(read);
            return buildModels(set).get(0);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<CarType> buildModels(ResultSet set) throws SQLException {
        List<CarType> carTypes = new ArrayList<>();
        while (set.next()) {
            carTypes.add(new CarType(set.getInt("id"), set.getString("name")));
        }
        return carTypes;
    }

    public CarType buildModelWithColumnNames(ResultSet set, String... columnNames) throws SQLException {
        CarType carType = null;
        try {
            carType = new CarType(set.getInt(columnNames[0]), set.getString(columnNames[1]));
        } catch (SQLException e) {
            throw new SQLException("Could not build fuel type from set.", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Column names not sufficient: " + e.getMessage(), e);
        }
        return carType;
    }
}
