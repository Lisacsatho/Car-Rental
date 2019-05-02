package se.hkr.Database.UserDB;

import se.hkr.HashUtils;
import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;
import se.hkr.Model.User.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    public Member authenticate(String email, String password) {
        byte[] hash = HashUtils.hash(password);
        StringBuilder hashedPassword = new StringBuilder();
        for (byte b : hash) {
            hashedPassword.append(b);
        }
        String query = "SELECT * FROM User JOIN Member ON user.socialSecurityNo = member.socialSecurityNo WHERE email=? AND password=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, hashedPassword.toString());
            ResultSet set = statement.executeQuery();
            return buildModels(set).get(0);
        } catch (Exception e) {
            return null;
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
        List<Member> members = new ArrayList<>();
        try (AddressDBHandler addressDBHandler = new AddressDBHandler()){
            while (set.next()) {
                Address address = addressDBHandler.readByPrimaryKey(Integer.toString(set.getInt("address")));
                Member member = new Member(set.getString("socialSecurityNo"), set.getString("firstName"), set.getString("lastName"), set.getString("email"), set.getString("phoneNo"), address, set.getString("driversLicenseNumber"));
                members.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }
}
