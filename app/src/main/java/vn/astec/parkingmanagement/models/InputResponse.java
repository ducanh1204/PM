package vn.astec.parkingmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputResponse {
    @SerializedName("cardInfo")
    @Expose
    private CardInfo cardInfo;
    @SerializedName("InputPlateNumber")
    @Expose
    private String inputPlateNumber;
    @SerializedName("IsMonney")
    @Expose
    private Integer isMonney;
    @SerializedName("InputTime")
    @Expose
    private String inputTime;

    public CardInfo getCardInfo() {
        return cardInfo;
    }


    public String getInputPlateNumber() {
        return inputPlateNumber;
    }


    public Integer getIsMonney() {
        return isMonney;
    }


    public String getInputTime() {
        return inputTime;
    }

}