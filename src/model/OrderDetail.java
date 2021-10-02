package model;

public class OrderDetail {
    private String rackId;
    private String orderId;
    private int qty;
    private String orderDate;
    private String orderTime;

    public OrderDetail() {
    }



    public OrderDetail(String orderId, int qty, String orderDate) {
        this.orderId = orderId;
        this.qty = qty;
        this.orderDate = orderDate;
    }

    public OrderDetail(String rackId, int qty) {
        this.rackId = rackId;
        this.qty = qty;
    }

    public OrderDetail(String rackId, String orderId, int qty, String orderDate, String orderTime) {
        this.setRackId(rackId);
        this.setOrderId(orderId);
        this.setQty(qty);
        this.setOrderDate(orderDate);
        this.setOrderTime(orderTime);
    }

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "rackId='" + rackId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", qty=" + qty +
                ", orderDate='" + orderDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                '}';
    }
}
