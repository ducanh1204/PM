package vn.astec.parkingmanagement.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import vn.astec.parkingmanagement.R;

public class BaseActivity extends AppCompatActivity {
    private Dialog mProgressDialog;
    public AlertDialog dialog;
    public void showToast(String msg) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void initDialogLoading() {
        mProgressDialog = new Dialog(this, R.style.dialogNotice);
        mProgressDialog.setContentView(R.layout.dialog_progress);
    }

    public void showDialogLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
        }
    }

    public void dismissDialogLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void showDialogNotification(String message){
        dismissDialogNotification();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.notification));
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    public void showDialogNotificationErr(String message){
        dismissDialogNotification();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.error));
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    public void dismissDialogNotification() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
