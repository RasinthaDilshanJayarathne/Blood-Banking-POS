package view.tm;

public class StoreDetailTM {
    private String blId;
    private String bloodGroup;
    private String rackId;
    private String rackName;
    private int space;
    private int qty;

    public StoreDetailTM() {
    }

    public StoreDetailTM(String blId, String bloodGroup, String rackId, String rackName, int space, int qty) {
        this.setBlId(blId);
        this.setBloodGroup(bloodGroup);
        this.setRackId(rackId);
        this.setRackName(rackName);
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

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getRackName() {
        return rackName;
    }

    public void setRackName(String rackName) {
        this.rackName = rackName;
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
        return "StoreDetailTM{" +
                "blId='" + blId + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", rackId='" + rackId + '\'' +
                ", rackName='" + rackName + '\'' +
                ", space=" + space +
                ", qty=" + qty +
                '}';
    }
}