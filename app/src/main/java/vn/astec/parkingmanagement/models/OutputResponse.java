package vn.astec.parkingmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutputResponse {
    @SerializedName("cardInfo")
    @Expose
    private CardInfo cardInfo;
    @SerializedName("OutputPlateNumber")
    @Expose
    private String outputPlateNumber;
    @SerializedName("InputPlateNumber")
    @Expose
    private String inputPlateNumber;
    @SerializedName("IsMonney")
    @Expose
    private Integer isMonney;
    @SerializedName("InputTime")
    @Expose
    private String inputTime;
    @SerializedName("OutputTime")
    @Expose
    private String outputTime;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("DAY_NUM")
    @Expose
    private Integer dayNum;
    @SerializedName("DAY_PAY")
    @Expose
    private Integer dayPay;
    @SerializedName("NIGHT_NUM")
    @Expose
    private Integer nightNum;
    @SerializedName("NIGHT_PAY")
    @Expose
    private Integer nightPay;
    @SerializedName("IsInputted")
    @Expose
    private Boolean isInputted;

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public String getOutputPlateNumber() {
        return outputPlateNumber;
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

    public String getOutputTime() {
        return outputTime;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public Integer getDayPay() {
        return dayPay;
    }

    public Integer getNightNum() {
        return nightNum;
    }

    public Integer getNightPay() {
        return nightPay;
    }

    public Boolean getInputted() {
        return isInputted;
    }
}