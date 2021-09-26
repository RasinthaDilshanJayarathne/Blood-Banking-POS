package view.tm;

public class OrderTM {
    private String orderId;
    private String hospitalId;
    private String date;
    private String time;

    public OrderTM() {
    }

    public OrderTM(String orderId, String hospitalId, String date, String time) {
        this.setOrderId(orderId);
        this.setHospitalId(hospitalId);
        this.setDate(date);
        this.setTime(time);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
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
}
