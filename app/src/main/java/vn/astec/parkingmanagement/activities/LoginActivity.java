package vn.astec.parkingmanagement.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.astec.parkingmanagement.R;
import vn.astec.parkingmanagement.models.LoginRequest;
import vn.astec.parkingmanagement.network.RetrofitService;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private long backPressedTime;
    private TextInputEditText edtUsername, edtPassword;
    public String url = "http://192.168.10.100:3333/";
    private OkHttpClient okHttpClient;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
        initDialogLoading();
    }

    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (username.equals("") || password.equals("")) {
            showToast(getString(R.string.warning_login_require));
            return;
        }
        showDialogLoading();
        LoginRequest rq = new LoginRequest();
        rq.setUsername(username);
        rq.setPassword(password);
        retrofitService.login(rq).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() != null) {
                    int emId = response.body();
                    if (emId > 0) {
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("EM_ID",emId);
                        startActivity(intent);
                        finish();
                    }else{
                        showToast(getString(R.string.login_failed));
                    }
                } else {
                    showToast(getString(R.string.login_failed));
                }
                dismissDialogLoading();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                showDialogNotification(t.getMessage());
                dismissDialogLoading();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            login();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            showToast(getResources().getString(R.string.toast_backpress));
        }
        backPressedTime = System.currentTimeMillis();
    }
}