package view.tm;

public class BloodTM {
    private String blId;
    private String blType;
    private String blDescription;

    public BloodTM() {
    }

    public BloodTM(String blId, String blType, String blDescription) {
        this.setBlId(blId);
        this.setBlType(blType);
        this.setBlDescription(blDescription);
    }


    public String getBlId() {
        return blId;
    }

    public void setBlId(String blId) {
        this.blId = blId;
    }

    public String getBlType() {
        return blType;
    }

    public void setBlType(String blType) {
        this.blType = blType;
    }

    public String getBlDescription() {
        return blDescription;
    }

    public void setBlDescription(String blDescription) {
        this.blDescription = blDescription;
    }

    @Override
    public String toString() {
        return "BloodTM{" +
                "blId='" + blId + '\'' +
                ", blType='" + blType + '\'' +
                ", blDescription='" + blDescription + '\'' +
                '}';
    }
}
