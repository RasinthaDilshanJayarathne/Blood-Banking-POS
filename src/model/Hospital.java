package model;

public class Hospital {
    private String hospitalId;
    private String name;
    private String email;
    private String address;
    private String city;
    private String phoneNo;
    private String website;

    public Hospital() {
    }

    public Hospital(String hospitalId, String name, String email, String address, String city, String phoneNo, String website) {
        this.setHospitalId(hospitalId);
        this.setName(name);
        this.setEmail(email);
        this.setAddress(address);
        this.setCity(city);
        this.setPhoneNo(phoneNo);
        this.setWebsite(website);
    }


    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "hospitalId='" + hospitalId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
