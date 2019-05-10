package se.hkr.Database.VehicleDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Vehicle.Car;
import se.hkr.Model.Vehicle.FuelType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FuelTypeDBHandler extends ModelDBHandler<FuelType> {
    @Override
    public void insert(FuelType model) throws SQLException {
    }

    @Override
    public void update(FuelType model) throws SQLException {

    }

    @Override
    public void delete(FuelType model) throws SQLException {

    }

    @Override
    public List<FuelType> readAll() {
        List<FuelType> fuelType = new ArrayList<>();
        String query = String.format("SELECT * FROM FuelType");
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(query);
            fuelType = buildModels(set);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fuelType;
    }
    @Override
    public FuelType readByPrimaryKey(String key) throws SQLException {
        String read = String.format("SELECT * FROM fueltype WHERE id=%s LIMIT 1", key);
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(read);
            return buildModels(set).get(0);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<FuelType> buildModels(ResultSet set) throws SQLException {
        List<FuelType> fuelTypes = new ArrayList<>();
        while (set.next()) {
            fuelTypes.add(new FuelType(set.getInt("id"), set.getString("name")));
        }
        return fuelTypes;

    }

    public FuelType buildModelWithColumnNames(ResultSet set, String... columnNames) throws SQLException {
        FuelType fuelType = null;
        try {
            fuelType = new FuelType(set.getInt(columnNames[0]), set.getString(columnNames[1]));
        } catch (SQLException e) {
            throw new SQLException("Could not build fuel type from set.", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Column names not sufficient: " + e.getMessage(), e);
        }
        return fuelType;
    }
}
