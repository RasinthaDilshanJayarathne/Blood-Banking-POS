package model;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private String hospitalId;
    private String date;
    private String time;
    private ArrayList<OrderDetail>orderDetails;

    public Order() {
    }

    public Order(String orderId, String hospitalId, String date, String time, ArrayList<OrderDetail> orderDetails) {
        this.setOrderId(orderId);
        this.setHospitalId(hospitalId);
        this.setDate(date);
        this.setTime(time);
        this.setOrderDetails(orderDetails);
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

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
