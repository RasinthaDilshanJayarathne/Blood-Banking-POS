package model;

public class BloodRack {
    private String id;
    private String blId;
    private String name;
    private int qty;
    private String bloodType;
    private int storeQty;

    public BloodRack() {
    }

   /* public BloodRack(String name, int qty) {
        this.name = name;
        this.qty = qty;
    }*/

    public BloodRack(String id, String name, int qty, int storeQty) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.storeQty = storeQty;
    }

    public BloodRack(String id, String blId, String name, int qty, String bloodType, int storeQty) {
        this.setId(id);
        this.setBlId(blId);
        this.setName(name);
        this.setQty(qty);
        this.setBloodType(bloodType);
        this.setStoreQty(storeQty);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlId() {
        return blId;
    }

    public void setBlId(String blId) {
        this.blId = blId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public int getStoreQty() {
        return storeQty;
    }

    public void setStoreQty(int storeQty) {
        this.storeQty = storeQty;
    }

    @Override
    public String toString() {
        return "BloodRack{" +
                "id='" + id + '\'' +
                ", blId='" + blId + '\'' +
                ", name='" + name + '\'' +
                ", qty=" + qty +
                ", bloodType='" + bloodType + '\'' +
                ", storeQty=" + storeQty +
                '}';
    }
}