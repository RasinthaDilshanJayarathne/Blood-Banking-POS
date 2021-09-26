package model;

public class Donor {
    private String nic;
    private String userID;
    private String name;
    private String address;
    private String city;
    private String type;
    private String blID;
    private String gender;
    private String phoneNo;
    private String email;

    public Donor() {
    }

    public Donor(String nic, String userID, String name, String address, String city, String type, String blID, String gender, String phoneNo, String email) {
        this.setNic(nic);
        this.setUserID(userID);
        this.setName(name);
        this.setAddress(address);
        this.setCity(city);
        this.setType(type);
        this.setBlID(blID);
        this.setGender(gender);
        this.setPhoneNo(phoneNo);
        this.setEmail(email);
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBlID() {
        return blID;
    }

    public void setBlID(String blID) {
        this.blID = blID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
