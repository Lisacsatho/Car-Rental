package se.hkr.Model.User;

import se.hkr.Model.Model;

public abstract class User implements Model {

    private String socialSecurityNo;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Address address;
    private String password;

    public User(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, Address address, String password) {
        this.socialSecurityNo = socialSecurityNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
    }

    public User(String socialSecurityNo, String firstName, String lastName, String email, String phoneNumber, Address address) {
        this.socialSecurityNo = socialSecurityNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getSocialSecurityNo() {
        return socialSecurityNo;
    }

    public void setSocialSecurityNo(String socialSecurityNo) {
        this.socialSecurityNo = socialSecurityNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return String.format("%s, %s", lastName, firstName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
    *   Search matches on ssn, first name, last name, email, phone and address
    * */
    @Override
    public boolean matches(String key) {
        if (address.matches(key)) {
            return true;
        } else if (socialSecurityNo.matches(".*"+key+".*")) {
            return true;
        } else if (firstName.matches(".*"+key+".*") || firstName.toLowerCase().matches(".*"+key+".*")) {
            return true;
        } else if (lastName.matches(".*"+key+".*") || lastName.toLowerCase().matches(".*"+key+".*")) {
            return true;
        } else if (email.matches(".*"+key+".*") || email.toLowerCase().matches(".*"+key+".*")) {
            return true;
        } else if (phoneNumber.matches(".*"+key+".*")) {
            return true;
        } else if (String.format("%s %s", firstName, lastName).matches(".*"+key+".*") || String.format("%s %s", firstName, lastName).toLowerCase().matches(".*"+key+".*")) {
            return true;
        } else {
            return false;
        }
    }
}
