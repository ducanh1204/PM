package vn.astec.parkingmanagement.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.astec.parkingmanagement.R;
import vn.astec.parkingmanagement.fragments.HomeFragment;
import vn.astec.parkingmanagement.fragments.InputFragment;
import vn.astec.parkingmanagement.fragments.OutputFragment;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_INPUT = 1;
    private static final int FRAGMENT_OUTPUT = 2;
    private int currentNavMenu = 1;
    private long backPressedTime;
    private Fragment currentFragment;
    private NfcAdapter nfcAdapter;
    public String url = "http://192.168.10.111/";
    public int emId;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        Intent intent = getIntent();
        emId = intent.getIntExtra("EM_ID",0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//            getSupportActionBar().setCustomView(R.layout.home_activity_action_bar);
//        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new InputFragment());
        navigationView.getMenu().findItem(R.id.nav_input).setChecked(true);
        initDialogLoading();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (currentNavMenu != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                currentNavMenu = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_input) {
            if (currentNavMenu != FRAGMENT_INPUT) {
                replaceFragment(new InputFragment());
                currentNavMenu = FRAGMENT_INPUT;
            }
        } else if (id == R.id.nav_output) {
            if (currentNavMenu != FRAGMENT_OUTPUT) {
                replaceFragment(new OutputFragment());
                currentNavMenu = FRAGMENT_OUTPUT;
            }
        } else if (id == R.id.nav_log_out) {
            nextActivity(LoginActivity.class);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                view.clearFocus();
            } else {

                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    return;
                } else {
                    showToast(getResources().getString(R.string.toast_backpress));
                }
                backPressedTime = System.currentTimeMillis();
            }

        }
    }

    private void replaceFragment(Fragment fragment) {
        currentFragment = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    public void disableNfcForegroundDispatch() {
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            try {
                nfcAdapter.disableForegroundDispatch(this);
            } catch (IllegalStateException ex) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        disableNfcForegroundDispatch();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableNfcForegroundDispatch();
    }

    public void enableNfcForegroundDispatch() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {

            Intent intent = new Intent(this,
                    this.getClass()).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, intent, PendingIntent.FLAG_MUTABLE);
            try {
                nfcAdapter.enableForegroundDispatch(
                        this, pendingIntent, null, new String[][]{
                                new String[]{NfcA.class.getName()}});
            } catch (IllegalStateException ex) {
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            dismissDialogNotification();
            if (currentNavMenu == FRAGMENT_INPUT) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                String dec10 = byte2FmtString(tag.getId(), 2);
                InputFragment inputFragment = (InputFragment) currentFragment;
                inputFragment.nfcScanListener(dec10);
            } else if (currentNavMenu == FRAGMENT_OUTPUT) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                String dec10 = byte2FmtString(tag.getId(), 2);
                OutputFragment inputFragment = (OutputFragment) currentFragment;
                inputFragment.nfcScanListener(dec10);
            }
        }
    }

    private String byte2FmtString(byte[] bytes, int fmt) {
        switch (fmt) {
            case 2:
                byte[] revBytes = bytes.clone();
                reverseByteArrayInPlace(revBytes);
                return hexToDec10(byteArrToHex(revBytes));
            case 1:
                return hexToDec10(byteArrToHex(bytes));
            default:
                return hexToDec10(byteArrToHex(bytes));
        }
    }

    private void reverseByteArrayInPlace(byte[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            byte temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }

    private String byteArrToHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder();
        if (bytes != null) {
            for (Byte b : bytes) {
                ret.append(String.format("%02X", b.intValue() & 0xFF));
            }
        }
        return ret.toString();
    }

    private String hexToDec10(String hex) {
        if (!(hex != null && hex.length() % 2 == 0
                && hex.matches("[0-9A-Fa-f]+"))) {
            return null;
        }
        String ret;
        if (hex == null || hex.isEmpty()) {
            ret = "0";
        } else if (hex.length() <= 14) {
            ret = Long.toString(Long.parseLong(hex, 16));
        } else {
            BigInteger bigInteger = new BigInteger(hex, 16);
            ret = bigInteger.toString();
        }
        return ret;
    }

    public String bitmapToBase64String(Bitmap bmp) {
        byte[] imgBytes = getBytesFromBitmap(bmp);
        String base64 = Base64.encodeToString(imgBytes, Base64.NO_WRAP);
        return base64;
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] result = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}