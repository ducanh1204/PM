package vn.astec.parkingmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitInputRequest {
    @SerializedName("CaNo")
    @Expose
    private String caNo;

    @SerializedName("CaId")
    @Expose
    private Integer caId;

    @SerializedName("PlateNumberReg")
    @Expose
    private String plateNumberReg;

    @SerializedName("PlateNumber")
    @Expose
    private String plateNumber;

    @SerializedName("IoTimeStr")
    @Expose
    private String ioTimeStr;

    @SerializedName("VehicleImageByte")
    @Expose
    private String vehicleImageByte;

    @SerializedName("GateID")
    @Expose
    private Integer gateID;

    @SerializedName("EmId")
    @Expose
    private Integer emId;

    public String getCaNo() {
        return caNo;
    }

    public void setCaNo(String caNo) {
        this.caNo = caNo;
    }

    public Integer getCaId() {
        return caId;
    }

    public void setCaId(Integer caId) {
        this.caId = caId;
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

    public String getVehicleImageByte() {
        return vehicleImageByte;
    }

    public void setVehicleImageByte(String vehicleImageByte) {
        this.vehicleImageByte = vehicleImageByte;
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
