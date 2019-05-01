package se.hkr.Database.VehicleDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.Vehicle.FuelType;
import se.hkr.Model.Vehicle.GearBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GearBoxDBHandler extends ModelDBHandler<GearBox> {
    @Override
    public void insert(GearBox model) throws SQLException {

    }

    @Override
    public void update(GearBox model) throws SQLException {

    }

    @Override
    public void delete(GearBox model) throws SQLException {

    }

    @Override
    public List<GearBox> readAll() throws SQLException {
        List<GearBox> gearBoxes = new ArrayList<>();
        String query = String.format("SELECT * FROM GearBox");
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(query);
            gearBoxes = buildModels(set);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gearBoxes;
    }

    @Override
    public GearBox readByPrimaryKey(String key) throws SQLException {
        String read = String.format("SELECT * FROM gearbox WHERE id=%s", key);
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(read);
            return buildModels(set).get(0);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<GearBox> buildModels(ResultSet set) throws SQLException {
        List<GearBox> gearBoxes = new ArrayList<>();
        while (set.next()) {
            gearBoxes.add(new GearBox(set.getInt("id"), set.getString("name")));
        }
        return gearBoxes;
    }
}
