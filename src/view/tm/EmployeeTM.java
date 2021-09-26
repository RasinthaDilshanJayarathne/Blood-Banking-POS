package view.tm;

public class EmployeeTM {
    private String userID;
    private String empId;
    private String empName;
    private String empAddress;
    private String empCity;
    private String empType;
    private String empGender;
    private String empPhoneNo;

    public EmployeeTM() {
    }

    public EmployeeTM(String userID, String empId, String empName, String empAddress, String empCity, String empType, String empGender, String empPhoneNo) {
        this.setUserID(userID);
        this.setEmpId(empId);
        this.setEmpName(empName);
        this.setEmpAddress(empAddress);
        this.setEmpCity(empCity);
        this.setEmpType(empType);
        this.setEmpGender(empGender);
        this.setEmpPhoneNo(empPhoneNo);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpCity() {
        return empCity;
    }

    public void setEmpCity(String empCity) {
        this.empCity = empCity;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getEmpGender() {
        return empGender;
    }

    public void setEmpGender(String empGender) {
        this.empGender = empGender;
    }

    public String getEmpPhoneNo() {
        return empPhoneNo;
    }

    public void setEmpPhoneNo(String empPhoneNo) {
        this.empPhoneNo = empPhoneNo;
    }
}
