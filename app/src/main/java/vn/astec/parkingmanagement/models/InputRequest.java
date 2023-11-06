package vn.astec.parkingmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputRequest {

    @SerializedName("CaNo")
    @Expose
    private String caNo;
    @SerializedName("InputPlateNumberImage")
    @Expose
    private String inputPlateNumberImage;

    public String getCaNo() {
        return caNo;
    }

    public void setCaNo(String caNo) {
        this.caNo = caNo;
    }

    public String getInputPlateNumberImage() {
        return inputPlateNumberImage;
    }

    public void setInputPlateNumberImage(String inputPlateNumberImage) {
        this.inputPlateNumberImage = inputPlateNumberImage;
    }


}