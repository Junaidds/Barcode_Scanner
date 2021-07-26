package com.android.barcodescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class PermissionActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "My_pref";
    private static String KEY_PopUp_CHECK = "PopUp_CHECK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        pref = getApplicationContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        // sessionManager = new SessionManager(this);

        Button grantBtn = findViewById(R.id.grantBtn);
        grantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getPopUpCheck()){
                    show_alert_permission();
                }else{
                    permissionCheckingMultiple();
                }
            }
        });

    }

    public boolean getPopUpCheck() {
        return pref.getBoolean(KEY_PopUp_CHECK,false);
    }

    public void setPopUpCheck(boolean status) {
        editor.putBoolean(KEY_PopUp_CHECK, status);
        editor.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(permissionGivenCheck()){
            startActivity(new Intent(PermissionActivity.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            finish();
        }
    }

    private boolean permissionGivenCheck(){

        int fineloc = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION");
        int corseloc = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION");
        int write_storage = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE");
        int read_storage = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE");
        int camera_permission = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA");

        return ((fineloc == PackageManager.PERMISSION_GRANTED)&&
                (corseloc == PackageManager.PERMISSION_GRANTED)&&
                (write_storage == PackageManager.PERMISSION_GRANTED)&&
                (read_storage == PackageManager.PERMISSION_GRANTED)&&
                (camera_permission == PackageManager.PERMISSION_GRANTED));
    }

    public boolean permissionCheckingMultiple() {
        /*int SMSPermission = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_SMS");
        int CallLogsPermission = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_CALL_LOG");*/
        int fineloc = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION");
        int corseloc = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION");
        int write_storage = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE");
        int read_storage = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_EXTERNAL_STORAGE");
        int camera_permission = ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.CAMERA");

        /*(SMSPermission == PackageManager.PERMISSION_GRANTED) &&
                (CallLogsPermission == PackageManager.PERMISSION_GRANTED)&&*/

        if ((fineloc == PackageManager.PERMISSION_GRANTED)&&
                (corseloc == PackageManager.PERMISSION_GRANTED)&&
                (write_storage == PackageManager.PERMISSION_GRANTED)&&
                (read_storage == PackageManager.PERMISSION_GRANTED)&&
                (camera_permission == PackageManager.PERMISSION_GRANTED)) {
//                permission granted
            setPopUpCheck(false);
            return true;
        } else {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                "android.permission.READ_SMS", "android.permission.READ_CALL_LOG",
                requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION" }, 1010);
                sessionManager.setPopUpCheck(true);
            }*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION",
                        "android.permission.ACCESS_FINE_LOCATION","android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1010);
                setPopUpCheck(true);
            }

            return false;
        }
    }

    public void permissionCheckingMultiple2(String permission) {
        int locPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
        if (locPermission == PackageManager.PERMISSION_GRANTED) {
//                permission granted
            setPopUpCheck(false);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{permission}, 1010);
                setPopUpCheck(true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 1010) {
            //permissionCheckingMultiple();
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        setPopUpCheck(false);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(permissions[i])) {
                                permissionCheckingMultiple2(permissions[i]);
                            }
                            setPopUpCheck(true);
                        }
                    }
                }
            }
        }
    }

    public void show_alert_permission() {
        AlertDialog alertDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert).create();
        } else {
            alertDialog = new AlertDialog.Builder(this).create();
        }

        alertDialog.setTitle("Permission Alert");
        alertDialog.setMessage("Please give permission first");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Go to Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 1010);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                        finish();
                    }
                });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                permissionCheckingMultiple();
        }
    }

}
