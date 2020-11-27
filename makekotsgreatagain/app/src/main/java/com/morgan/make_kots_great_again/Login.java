package com.morgan.make_kots_great_again;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button button_sign = findViewById(R.id.button_sign);
        final Button button_scan = findViewById(R.id.button_scan);
        final TextView username = findViewById(R.id.edit_mail);
        final TextView password = findViewById(R.id.edit_password);

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiRequest apiRequest = new ApiRequest(Login.this);
                // Checks if the user is connected to internet
                if (isConnectedToInternet(Login.this)) {
                    apiRequest.login_post_request(username, password);
                }
            }
        });

        button_scan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    launch_QR_scan_activity();
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                launch_QR_scan_activity();
            }
            else {
                Toast.makeText(this, "camera permission not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     //-----------------------------------------------------------
     // Function that destroy current activity and launch "Page2"
     //-----------------------------------------------------------
     */
    protected void launch_page2(Activity activity){
        Intent intent = new Intent(activity, Page2.class);
        activity.startActivity(intent);
        activity.finish();// Kills curent activity
    }

    /**
     //-----------------------------------------------------------
     // Function that checks if user is connected to internet
     //-----------------------------------------------------------
     * @return
     */
    protected boolean isConnectedToInternet(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean result = networkInfo != null && networkInfo.isConnected();
        if (!result){
            Toast.makeText(activity, "⚠️ No internet connection !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     //----------------------------------------------------------------------
     // Function that launch "Page_QR" (without destroying previous activity
     //----------------------------------------------------------------------
     */
    private void launch_QR_scan_activity(){
        Intent intent = new Intent(this, Page_Qr.class);
        startActivity(intent);
    }
}