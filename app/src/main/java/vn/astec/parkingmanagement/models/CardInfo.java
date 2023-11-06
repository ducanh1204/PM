package vn.astec.parkingmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardInfo {
    @SerializedName("CA_ID")
    @Expose
    private Integer caId;
    @SerializedName("CA_NUMBER")
    @Expose
    private String caNumber;
    @SerializedName("CA_NO")
    @Expose
    private String caNo;
    @SerializedName("CA_STATUS")
    @Expose
    private Boolean caStatus;
    @SerializedName("APPLY_DATE")
    @Expose
    private String applyDate;
    @SerializedName("EXPRIED_DATE")
    @Expose
    private String expriedDate;
    @SerializedName("PLATE_NUMBER")
    @Expose
    private String plateNumber;
    @SerializedName("CVT_ID")
    @Expose
    private Integer cvtId;
    @SerializedName("TYPEID")
    @Expose
    private Integer typeid;
    @SerializedName("VEHICLE_TYPE")
    @Expose
    private String vehicleType;
    @SerializedName("VEHICLE_MODEL")
    @Expose
    private String vehicleModel;
    @SerializedName("VEHICLE_COLOR")
    @Expose
    private String vehicleColor;
    @SerializedName("VEHICLE_IMAGE")
    @Expose
    private String vehicleImage;
    @SerializedName("EM_ID")
    @Expose
    private Integer emId;
    @SerializedName("EM_NAME")
    @Expose
    private String emName;
    @SerializedName("EM_STATUS")
    @Expose
    private Boolean emStatus;
    @SerializedName("DEP_NAME")
    @Expose
    private String depName;
    @SerializedName("TYPENAME")
    @Expose
    private String typename;
    @SerializedName("TYPE_CHECK")
    @Expose
    private String typeCheck;
    @SerializedName("CVT_NAME")
    @Expose
    private String cvtName;
    @SerializedName("APPLY_DATE_STR")
    @Expose
    private String applyDateStr;
    @SerializedName("EXPRIED_DATE_STR")
    @Expose
    private String expriedDateStr;

    public Integer getCaId() {
        return caId;
    }

    public void setCaId(Integer caId) {
        this.caId = caId;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getCaNo() {
        return caNo;
    }

    public void setCaNo(String caNo) {
        this.caNo = caNo;
    }

    public Boolean getCaStatus() {
        return caStatus;
    }

    public void setCaStatus(Boolean caStatus) {
        this.caStatus = caStatus;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getExpriedDate() {
        return expriedDate;
    }

    public void setExpriedDate(String expriedDate) {
        this.expriedDate = expriedDate;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Integer getCvtId() {
        return cvtId;
    }

    public void setCvtId(Integer cvtId) {
        this.cvtId = cvtId;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public Integer getEmId() {
        return emId;
    }

    public void setEmId(Integer emId) {
        this.emId = emId;
    }

    public String getEmName() {
        return emName;
    }

    public void setEmName(String emName) {
        this.emName = emName;
    }

    public Boolean getEmStatus() {
        return emStatus;
    }

    public void setEmStatus(Boolean emStatus) {
        this.emStatus = emStatus;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTypeCheck() {
        return typeCheck;
    }

    public void setTypeCheck(String typeCheck) {
        this.typeCheck = typeCheck;
    }

    public String getCvtName() {
        return cvtName;
    }

    public void setCvtName(String cvtName) {
        this.cvtName = cvtName;
    }

    public String getApplyDateStr() {
        return applyDateStr;
    }

    public void setApplyDateStr(String applyDateStr) {
        this.applyDateStr = applyDateStr;
    }

    public String getExpriedDateStr() {
        return expriedDateStr;
    }

    public void setExpriedDateStr(String expriedDateStr) {
        this.expriedDateStr = expriedDateStr;
    }
}