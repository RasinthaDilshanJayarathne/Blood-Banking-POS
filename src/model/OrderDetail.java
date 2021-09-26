package model;

public class OrderDetail {
    private String rackId;
    private int qty;

    public OrderDetail() {
    }

    public OrderDetail(String rackId, String orderId, int qty) {
        this.setRackId(rackId);
        this.setQty(qty);
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

    @Override
    public String toString() {
        return "OrderDetail{" +
                "rackId='" + rackId + '\'' +
                ", qty=" + qty +
                '}';
    }
}
