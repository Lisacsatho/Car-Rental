package se.hkr.Database.UserDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.User.Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressDBHandler extends ModelDBHandler<Address> {
    @Override
    public void insert(Address model) {
        String insert = String.format("INSERT INTO address (zip, street, state) VALUES('%s', '%s', '%s')",
                                    model.getZip(),
                                    model.getStreet(),
                                    model.getState());
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
            String getId = "SELECT LAST_INSERT_ID() AS id";
            ResultSet set = statement.executeQuery(getId);
            set.next();
            model.setId(set.getInt("id"));
        } catch (SQLException e) {
            // TODO: Proper handling
            e.printStackTrace();
        }
    }

    @Override
    public void update(Address model) throws SQLException {
        String query = "UPDATE address SET street=?, state=?, zip=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, model.getStreet());
            statement.setString(2, model.getState());
            statement.setString(3, model.getZip());
            statement.setInt(4, model.getId());
            if (statement.executeUpdate() != 1) {
                throw new SQLException("No address found with id: " + model.getId());
            }
        } catch (Exception e) {
            throw new SQLException("Cannot update address: " + model.getId(), e);
        }
    }

    @Override
    public void delete(Address model) {

    }

    @Override
    public List<Address> readAll() {
        return null;
    }

    @Override
    public Address readByPrimaryKey(String key) throws SQLException {
        String query = "SELECT * FROM address WHERE id=?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(key));
            List<Address> result = buildModels(statement.executeQuery());
            if (!result.isEmpty()) {
                return result.get(0);
            }
        } catch (Exception e) {
            throw new SQLException("Could not read address by primary key: " + key, e);
        }
        return null;
    }

    @Override
    public List<Address> buildModels(ResultSet set) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        try {
            while (set.next()) {
                Address address = new Address(set.getInt("id"), set.getString("street"), set.getString("zip"), set.getString("state"));
                addresses.add(address);
            }
        } catch (SQLException e) {
            throw new SQLException("Could not build addresses: " + e.getMessage(), e);
        }
        return addresses;
    }
}
