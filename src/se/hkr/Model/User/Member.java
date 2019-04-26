package se.hkr.Model.User;

public class Member extends User {

    private String driverLicensNo;
    private int age;


    public Member(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, String address, String driverLicensNo) {
        super(socialSecurityNo, firstName, lastName, email, phoneNumber, address);
        this.driverLicensNo = driverLicensNo;
    }

    public String getDriverLicensNo() {
        return driverLicensNo;
    }

    public void setDriverLicensNo(String driverLicensNo) {
        this.driverLicensNo = driverLicensNo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
