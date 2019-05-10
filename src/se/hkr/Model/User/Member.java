package se.hkr.Model.User;

public class Member extends User {
    private String driverLicensNo;
    private boolean verified;
    private String verificationCode;

    public Member(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, Address address, String password, String driverLicensNo, boolean verified) {
        super(socialSecurityNo, firstName, lastName, email, phoneNumber, address, password);
        this.driverLicensNo = driverLicensNo;
        this.verified = verified;
    }

    public Member(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, Address address, String driverLicensNo, boolean verified) {
        super(socialSecurityNo, firstName, lastName, email, phoneNumber, address);
        this.driverLicensNo = driverLicensNo;
        this.verified = verified;
    }

    public String getDriverLicensNo() {
        return driverLicensNo;
    }

    public void setDriverLicensNo(String driverLicensNo) {
        this.driverLicensNo = driverLicensNo;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
