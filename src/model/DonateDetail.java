package model;

public class DonateDetail {
    private String blID;
    private String rID;
    private String name;
    private String nic;
    private String date;
    private String time;
    private int qtyOnHand;
    private int totalQty;

    public DonateDetail() {
    }

    public DonateDetail(String blID, String rID, String name, String nic, String date, String time, int qtyOnHand, int totalQty) {
        this.setBlID(blID);
        this.setrID(rID);
        this.setName(name);
        this.setNic(nic);
        this.setDate(date);
        this.setTime(time);
        this.setQtyOnHand(qtyOnHand);
        this.setTotalQty(totalQty);
    }

    public String getBlID() {
        return blID;
    }

    public void setBlID(String blID) {
        this.blID = blID;
    }

    public String getrID() {
        return rID;
    }

    public void setrID(String rID) {
        this.rID = rID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
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

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }
}
