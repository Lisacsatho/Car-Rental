package se.hkr.Database.UserDB;

import se.hkr.Database.ModelDBHandler;
import se.hkr.Model.User.Address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public void update(Address model) {

    }

    @Override
    public void delete(Address model) {

    }

    @Override
    public List<Address> readAll() {
        return null;
    }

    @Override
    public Address readByPrimaryKey(String key) {
        return null;
    }

    @Override
    public List<Address> buildModels(ResultSet set) {
        return null;
    }
}
