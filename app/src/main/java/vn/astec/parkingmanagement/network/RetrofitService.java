package vn.astec.parkingmanagement.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.astec.parkingmanagement.models.InputRequest;
import vn.astec.parkingmanagement.models.InputResponse;
import vn.astec.parkingmanagement.models.LoginRequest;
import vn.astec.parkingmanagement.models.OutputRequest;
import vn.astec.parkingmanagement.models.OutputResponse;
import vn.astec.parkingmanagement.models.SubmitInputRequest;

public interface RetrofitService {
    @POST("Astec.OCR.WebApi//api/mobileparkingmanagement/input")
    Call<InputResponse> input(@Body InputRequest rq);


    @POST("/api/mobileparkingmanagement/login")
    Call<Integer> login(@Body LoginRequest rq);


    @POST("Astec.OCR.WebApi//api/mobileparkingmanagement/submitinput")
    Call<Integer> submitInput(@Body SubmitInputRequest rq);

    @POST("Astec.OCR.WebApi//api/mobileparkingmanagement/output")
    Call<OutputResponse> output(@Body OutputRequest rq);
}
