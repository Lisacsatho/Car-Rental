package se.hkr.Database.VehicleDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Vehicle.FuelType;

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
        return null;
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
}
