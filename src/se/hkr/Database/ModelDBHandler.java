package se.hkr.Database;

import se.hkr.Model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class ModelDBHandler <T extends Model> implements Database {
    protected Statement statement;

    public ModelDBHandler() {
        try {
            connect();
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to database.", e);
        }
    }

    public abstract void insert(T model);

    public abstract void update(T model);

    public abstract void delete(T model);

    public abstract List<T> readAll();

    public abstract T readByPrimaryKey(String key);

    /*
    *   Responsible for creating objects of the subject
    *   to avoid repetition in db handlers.
    * */
    public abstract List<T> buildModels(ResultSet set);

    @Override
    public void connect() throws SQLException {
        DatabaseConnection.getInstance().connect();
        statement = DatabaseConnection.getInstance().getConnection().createStatement();
    }

    @Override
    public void close() throws Exception {
        DatabaseConnection.getInstance().close();
        statement.close();
    }
}
