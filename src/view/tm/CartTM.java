package view.tm;

public class CartTM {
    private String hospitalId;
    private String hospitalName;
    private String blType;
    private int orderQty;
    private String date;
    private String time;

    public CartTM() {
    }

    public CartTM(String hospitalId, String hospitalName, String blType, int orderQty, String date, String time) {
        this.setHospitalId(hospitalId);
        this.setHospitalName(hospitalName);
        this.setBlType(blType);
        this.setOrderQty(orderQty);
        this.setDate(date);
        this.setTime(time);
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getBlType() {
        return blType;
    }

    public void setBlType(String blType) {
        this.blType = blType;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CartTM{" +
                "hospitalId='" + hospitalId + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", blType='" + blType + '\'' +
                ", orderQty=" + orderQty +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
