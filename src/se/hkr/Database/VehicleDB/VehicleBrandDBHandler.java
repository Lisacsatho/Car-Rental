package se.hkr.Database.VehicleDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Vehicle.FuelType;
import se.hkr.Model.Vehicle.VehicleBrand;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VehicleBrandDBHandler extends ModelDBHandler<VehicleBrand> {
    @Override
    public void insert(VehicleBrand model) throws SQLException {

    }

    @Override
    public void update(VehicleBrand model) throws SQLException {

    }

    @Override
    public void delete(VehicleBrand model) throws SQLException {

    }

    @Override
    public List<VehicleBrand> readAll() throws SQLException {
        List<VehicleBrand> vehicleBrands = new ArrayList<>();
        String query = String.format("SELECT * FROM VehicleBrand");
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(query);
            vehicleBrands = buildModels(set);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vehicleBrands;
    }

    @Override
    public VehicleBrand readByPrimaryKey(String key) throws SQLException {
        String read = "SELECT * FROM vehiclebrand WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(read)) {
            statement.setInt(1, Integer.parseInt(key));
            ResultSet set = statement.executeQuery();
            return buildModels(set).get(0);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<VehicleBrand> buildModels(ResultSet set) throws SQLException {
        List<VehicleBrand> vehicleBrands = new ArrayList<>();
        while (set.next()) {
            vehicleBrands.add(new VehicleBrand(set.getInt("id"), set.getString("name")));
        }
        return vehicleBrands;
    }
}
