package vn.astec.parkingmanagement.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutputRequest {

@SerializedName("CaNo")
@Expose
private String caNo;
@SerializedName("OutputPlateNumberImage")
@Expose
private String outputPlateNumberImage;

public String getCaNo() {
return caNo;
}

public void setCaNo(String caNo) {
this.caNo = caNo;
}

public String getOutputPlateNumberImage() {
return outputPlateNumberImage;
}

public void setOutputPlateNumberImage(String outputPlateNumberImage) {
this.outputPlateNumberImage = outputPlateNumberImage;
}

}