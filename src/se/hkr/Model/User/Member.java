package se.hkr.Model.User;

public class Member extends User {
    private String driversLicenseNo;
    private boolean verified;
    private String verificationCode;

    public Member(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, Address address, String password, String driversLicenseNo, boolean verified) {
        super(socialSecurityNo, firstName, lastName, email, phoneNumber, address, password);
        this.driversLicenseNo = driversLicenseNo;
        this.verified = verified;
    }

    public Member(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, Address address, String driversLicenseNo, boolean verified) {
        super(socialSecurityNo, firstName, lastName, email, phoneNumber, address);
        this.driversLicenseNo = driversLicenseNo;
        this.verified = verified;
    }

    public String getDriversLicenseNo() {
        return driversLicenseNo;
    }

    public void setDriversLicenseNo(String driversLicenseNo) {
        this.driversLicenseNo = driversLicenseNo;
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

    @Override
    public boolean matches(String key) {
        if (super.matches(key)) {
            return true;
        }
        return driversLicenseNo.matches(".*"+key+".*");
    }
}
