package se.hkr.Database.UserDB;

import se.hkr.Model.User.Member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MemberDBHandler extends UserDBHandler<Member> {
    @Override
    public void insert(Member model) throws SQLException {
        super.insert(model);
        String insert = String.format("INSERT INTO member VALUES('%s', '%s')",
                                    model.getDriverLicensNo(),
                                    model.getSocialSecurityNo());
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void update(Member model) {
        System.out.println("Updated member!");
    }

    @Override
    public void delete(Member model) {
        System.out.println("Deleted member!");
    }

    @Override
    public List<Member> readAll() {
        return null;
    }

    @Override
    public Member readByPrimaryKey(String key) {
        return null;
    }

    @Override
    public List<Member> buildModels(ResultSet set) {
        return null;
    }
}
