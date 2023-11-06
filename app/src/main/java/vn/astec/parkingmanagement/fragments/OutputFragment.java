package vn.astec.parkingmanagement.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.astec.parkingmanagement.R;
import vn.astec.parkingmanagement.activities.MainActivity;
import vn.astec.parkingmanagement.adapters.VehicleTypeAdapter;
import vn.astec.parkingmanagement.models.InputRequest;
import vn.astec.parkingmanagement.models.InputResponse;
import vn.astec.parkingmanagement.models.OutputRequest;
import vn.astec.parkingmanagement.models.OutputResponse;
import vn.astec.parkingmanagement.models.VehicleType;
import vn.astec.parkingmanagement.network.RetrofitService;

public class OutputFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ImageCapture imageCapture;
    private PreviewView mPreviewView;
    public static final int PERMISSION_REQUEST_CODE = 110;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    private boolean led = false;
    private Camera camera = null;
    private List<VehicleType> vehicleTypes;
    private RecyclerView recyclerView;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ImageView imgResult, imgFlash;
    private int previewViewWidth = 1;
    private int previewViewHeight = 1;
    private FrameLayout layoutImgResult, layoutPreviewCamera;
    private VehicleTypeAdapter vehicleTypeAdapter;
    private TextView edtTimeInput, edtTimeOutput, edtCaNumber, edtInputPlateNumbers, edtOutputPlateNumbers, edtPrice;
    private MainActivity activity;
    private RetrofitService retrofitService;
    private Button btnSubmit;
    private String imgInputBase64Str = "";
    private OutputResponse outputResponse = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_output, container, false);
        mPreviewView = view.findViewById(R.id.preview_view);
        recyclerView = view.findViewById(R.id.recycler_view);
        imgResult = view.findViewById(R.id.img_result);
        layoutImgResult = view.findViewById(R.id.layout_img_result);
        view.findViewById(R.id.btn_skip).setOnClickListener(this);
        imgFlash = view.findViewById(R.id.img_flash);
        imgFlash.setOnClickListener(this);
        layoutPreviewCamera = view.findViewById(R.id.layout_preview_camera);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        edtTimeInput = view.findViewById(R.id.edt_time_input);
        edtTimeOutput = view.findViewById(R.id.edt_time_output);
        edtCaNumber = view.findViewById(R.id.edt_ca_number);
        edtInputPlateNumbers = view.findViewById(R.id.edt_input_plate_numbers);
        edtPrice = view.findViewById(R.id.edt_price);
        edtOutputPlateNumbers = view.findViewById(R.id.edt_output_plate_numbers);

        boolean isPermissionsGranted = allPermissionsGranted();
        if (isPermissionsGranted) {
            startCamera();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        setVehicleTypeListData();

        ViewTreeObserver vto = mPreviewView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mPreviewView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mPreviewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                previewViewWidth = mPreviewView.getMeasuredWidth();
                previewViewHeight = mPreviewView.getMeasuredHeight();

            }
        });

        activity = (MainActivity) getActivity();
        activity.getSupportActionBar().setTitle(getString(R.string.nav_output));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(activity.url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
        setEnableBtnSubmit(false);
        return view;
    }

    private void setVehicleTypeListData() {
        vehicleTypes = new ArrayList<>();
        vehicleTypes.add(new VehicleType(R.drawable.ic_bicycle, false, getString(R.string.bicycle)));
        vehicleTypes.add(new VehicleType(R.drawable.ic_two_wheeler, false, getString(R.string.motorbike)));
        vehicleTypes.add(new VehicleType(R.drawable.ic_directions_car, false, getString(R.string.car)));
        vehicleTypeAdapter = new VehicleTypeAdapter(getContext(), vehicleTypes);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(vehicleTypeAdapter);
    }

    private void takePhoto(String caNo) {
        activity.showDialogLoading();
        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/Parking");
        File[] fileInFolder1 = dir.listFiles();
        if (fileInFolder1 != null) {
            for (File item : fileInFolder1) {
                try {
                    item.delete();
                } catch (Exception ex) {
                }
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "img");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Parking");
        }
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions
                .Builder(getActivity().getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();


//        File file = new File(getBatchDirectoryName(), "img" + ".jpg");
//        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();

        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                try{
                    File imgFile = new File(dir.getAbsolutePath() + "/img.jpg");
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        int offsetHeight = (int) (bitmap.getWidth() * (double) previewViewHeight / previewViewWidth);
                        int positionHeight = (bitmap.getHeight() / 2 - offsetHeight / 2);
                        Bitmap finalMyBitmap = Bitmap.createBitmap(bitmap, 0, positionHeight, bitmap.getWidth(), offsetHeight);
                        imgInputBase64Str = activity.bitmapToBase64String(finalMyBitmap);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                layoutImgResult.setVisibility(View.VISIBLE);
                                layoutPreviewCamera.setVisibility(View.GONE);
                                imgResult.setImageBitmap(finalMyBitmap);
                            }
                        });
                        OutputRequest rq = new OutputRequest();
                        rq.setCaNo(caNo);
                        rq.setOutputPlateNumberImage(imgInputBase64Str);
                        retrofitService.output(rq).enqueue(new Callback<OutputResponse>() {
                            @Override
                            public void onResponse(Call<OutputResponse> call, Response<OutputResponse> response) {
                                try{
                                    activity.dismissDialogLoading();
                                    OutputResponse res = response.body();
                                    if (res != null) {
                                        outputResponse = res;
                                        if (res.getInputted() == false) {
                                            activity.showDialogNotification(getString(R.string.notification_not_is_inputted));
                                            setEnableBtnSubmit(false);
                                            return;
                                        }
                                        String inputPlateNumbers = res.getInputPlateNumber();
                                        edtInputPlateNumbers.setText(inputPlateNumbers);
                                        String outputPlateNumbers = res.getOutputPlateNumber();
                                        edtOutputPlateNumbers.setText(outputPlateNumbers);
                                        if (verifyPlateNumbers(inputPlateNumbers, outputPlateNumbers)) {
                                            edtOutputPlateNumbers.setTextColor(ContextCompat.getColor(getContext(), R.color.color_success));
                                        } else {
                                            edtOutputPlateNumbers.setTextColor(ContextCompat.getColor(getContext(), R.color.color_failed));
                                        }
                                        if (res.getInputTime() != null && !res.getInputTime().equals("")) {
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try {
                                                Date inputDate = simpleDateFormat.parse(res.getInputTime());
                                                String inputDateStr = new SimpleDateFormat("dd/MM HH:mm", Locale.US).format(inputDate);
                                                edtTimeInput.setText(inputDateStr);
                                            } catch (ParseException e) {
                                                activity.showDialogNotification(e.getMessage());
                                                return;
                                            }
                                        }
                                        if (res.getOutputTime() != null && !res.getOutputTime().equals("")) {
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try {
                                                Date outputDate = simpleDateFormat.parse(res.getOutputTime());
                                                String outputDateStr = new SimpleDateFormat("dd/MM HH:mm", Locale.US).format(outputDate);
                                                edtTimeOutput.setText(outputDateStr);
                                            } catch (ParseException e) {
                                                activity.showDialogNotification(e.getMessage());
                                                return;
                                            }
                                        }
                                        if (res.getCardInfo() != null) {
                                            edtCaNumber.setText(res.getCardInfo().getCaNo());
                                            String ctvName = res.getCardInfo().getCvtName();
                                            int pos = -1;
                                            if (ctvName.toLowerCase().contains(getString(R.string.motorbike).toLowerCase())) {
                                                pos = 1;
                                            } else if (ctvName.toLowerCase().contains(getString(R.string.bicycle).toLowerCase())) {
                                                pos = 0;
                                            } else if (ctvName.toLowerCase().contains(getString(R.string.car).toLowerCase())) {
                                                pos = 2;
                                            }
                                            if (pos != -1) {
                                                vehicleTypes.get(pos).setCheck(true);
                                                vehicleTypeAdapter = new VehicleTypeAdapter(getContext(), vehicleTypes);
                                                vehicleTypeAdapter.notifyDataSetChanged();
                                                recyclerView.setAdapter(vehicleTypeAdapter);
                                            }
                                            if (res.getCardInfo().getCaStatus() != true) {
                                                activity.showDialogNotification(getString(R.string.notification_card_not_active));
                                                setEnableBtnSubmit(false);
                                                return;
                                            }
                                            edtPrice.setText(res.getPrice().toString());
                                            if (res.getOutputTime() != null && !res.getOutputTime().equals("")) {
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                                                try {
                                                    Date date = simpleDateFormat.parse(res.getOutputTime());
                                                    String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
                                                    Date dt = simpleDateFormat2.parse(dateStr);
                                                    if ((res.getCardInfo().getApplyDateStr() != null && !res.getCardInfo().getApplyDateStr().equals("") &&
                                                            simpleDateFormat2.parse(res.getCardInfo().getApplyDateStr()).compareTo(dt) > 0) ||
                                                            (res.getCardInfo().getExpriedDateStr() != null && !res.getCardInfo().getExpriedDateStr().equals("") &&
                                                                    simpleDateFormat2.parse(res.getCardInfo().getExpriedDateStr()).compareTo(dt) < 0)) {
                                                        activity.showDialogNotification(getString(R.string.notification_card_expired));
                                                        setEnableBtnSubmit(false);
                                                        return;
                                                    }
                                                } catch (ParseException e) {
                                                    activity.showDialogNotificationErr(e.getMessage());
                                                    return;
                                                }
                                            }
                                            if (res.getIsMonney() == 0) {
                                                activity.showDialogNotification(getString(R.string.notification_card_not_pay));
                                                setEnableBtnSubmit(false);
                                                return;
                                            }
                                        } else {
                                            edtInputPlateNumbers.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                            activity.showDialogNotification(getString(R.string.notification_card_not_register));
                                            return;
                                        }
                                        setEnableBtnSubmit(true);
                                    }
                                }catch (Exception ex){
                                    activity.showDialogNotificationErr(ex.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<OutputResponse> call, Throwable t) {
                                activity.showDialogNotificationErr(t.getMessage());
                                activity.dismissDialogLoading();
                            }
                        });
                    }
                }catch (Exception ex){
                    activity.dismissDialogLoading();
                    activity.showDialogNotificationErr(ex.getMessage());
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException error) {
                activity.showDialogNotificationErr(error.getMessage());
                activity.dismissDialogLoading();
            }
        });
    }

    public String getBatchDirectoryName() {
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/imgs";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {
        }
        return app_folder_path;
    }

    private void setEnableBtnSubmit(boolean value) {
        btnSubmit.setEnabled(value);
    }

    private void refresh() {
        try {
            edtTimeInput.setText(null);
            edtTimeOutput.setText(null);
            edtCaNumber.setText(null);
            edtInputPlateNumbers.setText(null);
            edtOutputPlateNumbers.setText(null);
            edtPrice.setText(null);
            layoutImgResult.setVisibility(View.GONE);
            layoutPreviewCamera.setVisibility(View.VISIBLE);
            setVehicleTypeListData();
            setEnableBtnSubmit(false);
            outputResponse = null;
            imgInputBase64Str = "";
        } catch (Exception ex) {
            activity.showDialogNotificationErr(ex.getMessage());
        }
    }

    public void nfcScanListener(String value) {
        refresh();
        try {
            takePhoto(value);
        } catch (Exception ex) {
            activity.showDialogNotificationErr(ex.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                }
                break;
        }
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(view.getContext());
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                }
            }
        }, ContextCompat.getMainExecutor(view.getContext()));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder()
                .build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();
        ImageCapture.Builder imageCaptureBuilder = new ImageCapture.Builder();
        imageCapture = imageCaptureBuilder
                .setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis, imageCapture);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(view.getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean verifyPlateNumbers(String plateNumber1, String plateNumber2) {
        plateNumber1 = plateNumber1.replace("-", "").replace(".", "").replace(" ", "");
        plateNumber2 = plateNumber2.replace("-", "").replace(".", "").replace(" ", "");
        return plateNumber1.equalsIgnoreCase(plateNumber2);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_skip) {
            refresh();
        } else if (id == R.id.img_flash) {
            try {
                if (camera != null) {
                    if (!led) {
                        camera.getCameraControl().enableTorch(true);
                        imgFlash.setImageResource(R.drawable.ic_flash_on);
                        led = true;
                    } else {
                        camera.getCameraControl().enableTorch(false);
                        imgFlash.setImageResource(R.drawable.ic_flash_off);
                        led = false;
                    }
                }
            } catch (Exception ex) {
                activity.showDialogNotificationErr(ex.getMessage());
            }
        } else if (id == R.id.btn_submit) {

        }
    }

}
