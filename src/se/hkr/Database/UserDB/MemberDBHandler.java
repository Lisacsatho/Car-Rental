package se.hkr.Database.UserDB;

import se.hkr.Model.User.Address;
import se.hkr.Model.User.Member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDBHandler extends UserDBHandler<Member> {
    @Override
    public void insert(Member model) throws SQLException {
        super.insert(model);
        String insert = String.format("INSERT INTO member (driversLicenseNumber, socialSecurityNo, verificationCode) VALUES(?, ?, ?)");
        try (PreparedStatement statement = connection.prepareStatement(insert)) {
            statement.setString(1, model.getDriverLicensNo());
            statement.setString(2, model.getSocialSecurityNo());
            statement.setString(3, model.getVerificationCode());
            statement.execute();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean authenticateUser(String email, String hashedPassword) {
        String query = "SELECT * FROM User JOIN Member ON user.socialSecurityNo = member.socialSecurityNo WHERE email=? AND password=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, hashedPassword);
            ResultSet set = statement.executeQuery();
            return !buildModels(set).isEmpty();
        } catch (Exception e) {
            return false;
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
    protected Member readByEmail(String email) throws SQLException {
        String query = "SELECT * FROM User JOIN Member ON user.socialSecurityNo=member.socialSecurityNo WHERE email=? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            List<Member> members = buildModels(set);
            if (!members.isEmpty()) {
                return members.get(0);
            }
        }
        return null;
    }

    public boolean verifyEmail(Member member, String code) throws SQLException {
        String query = "UPDATE member SET verified=1 WHERE socialSecurityNo=? AND verificationCode=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getSocialSecurityNo());
            statement.setString(2, code);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new SQLException("Email could not be verified.", e);
        }
    }

    @Override
    public List<Member> buildModels(ResultSet set) {
        List<Member> members = new ArrayList<>();
        try (AddressDBHandler addressDBHandler = new AddressDBHandler()){
            while (set.next()) {
                Address address = addressDBHandler.readByPrimaryKey(Integer.toString(set.getInt("address")));
                Member member = new Member(set.getString("socialSecurityNo"), set.getString("firstName"), set.getString("lastName"), set.getString("email"), set.getString("phoneNo"), address, set.getString("driversLicenseNumber"), set.getBoolean("verified"));
                members.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }
}
