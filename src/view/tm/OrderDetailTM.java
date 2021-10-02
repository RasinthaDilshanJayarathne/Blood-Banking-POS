package view.tm;

public class OrderDetailTM {
    private String orderId;
    private String hospitalId;
    private String rackId;
    private int qty;
    private String orderDate;

    public OrderDetailTM() {
    }

    public OrderDetailTM(String orderId, String hospitalId, String rackId, int qty, String orderDate) {
        this.setOrderId(orderId);
        this.setHospitalId(hospitalId);
        this.setRackId(rackId);
        this.setQty(qty);
        this.setOrderDate(orderDate);
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

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "OrderDetailTM{" +
                "orderId='" + orderId + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", rackId='" + rackId + '\'' +
                ", qty=" + qty +
                ", orderDate='" + orderDate + '\'' +
                '}';
    }
}
