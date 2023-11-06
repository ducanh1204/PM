package vn.astec.parkingmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IOInfo {
    @SerializedName("CA_NUMBER")
    @Expose
    private String caNumber;
    @SerializedName("CardID")
    @Expose
    private Integer cardID;
    @SerializedName("PlateNumberReg")
    @Expose
    private String plateNumberReg;
    @SerializedName("PlateNumber")
    @Expose
    private String plateNumber;
    @SerializedName("IoTimeStr")
    @Expose
    private String ioTimeStr;
    @SerializedName("VehicleImage")
    @Expose
    private String vehicleImage;
    @SerializedName("GateID")
    @Expose
    private Integer gateID;
    @SerializedName("Em_id")
    @Expose
    private Integer emId;

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public Integer getCardID() {
        return cardID;
    }

    public void setCardID(Integer cardID) {
        this.cardID = cardID;
    }

    public String getPlateNumberReg() {
        return plateNumberReg;
    }

    public void setPlateNumberReg(String plateNumberReg) {
        this.plateNumberReg = plateNumberReg;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getIoTimeStr() {
        return ioTimeStr;
    }

    public void setIoTimeStr(String ioTimeStr) {
        this.ioTimeStr = ioTimeStr;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public Integer getGateID() {
        return gateID;
    }

    public void setGateID(Integer gateID) {
        this.gateID = gateID;
    }

    public Integer getEmId() {
        return emId;
    }

    public void setEmId(Integer emId) {
        this.emId = emId;
    }
}
