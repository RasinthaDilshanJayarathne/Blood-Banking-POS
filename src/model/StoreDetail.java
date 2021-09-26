package model;

public class StoreDetail {
    private String blId;
    private String bloodGroup;
    private String rackName;
    private String rackId;
    private int space;
    private int qty;

    public StoreDetail() {
    }

    public StoreDetail(String blId, String bloodGroup, String rackName, String rackId, int space, int qty) {
        this.setBlId(blId);
        this.setBloodGroup(bloodGroup);
        this.setRackName(rackName);
        this.setRackId(rackId);
        this.setSpace(space);
        this.setQty(qty);
    }

    public String getBlId() {
        return blId;
    }

    public void setBlId(String blId) {
        this.blId = blId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
    }

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "StoreDetail{" +
                "blId='" + blId + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", rackName='" + rackName + '\'' +
                ", rackId='" + rackId + '\'' +
                ", space=" + space +
                ", qty=" + qty +
                '}';
    }
}
