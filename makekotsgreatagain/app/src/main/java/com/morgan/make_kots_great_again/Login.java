package com.morgan.make_kots_great_again;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

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

    TextView username, password;
    // Data needed to connect to API
    private final String ip = "172.20.0.3";
    private final int port = 8000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button button_sign = findViewById(R.id.button_sign);
        final Button button_scan = findViewById(R.id.button_scan);
        username = findViewById(R.id.edit_mail);
        password = findViewById(R.id.edit_password);

        final Context context = this;

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                NetworkChecks checkClass = new NetworkChecks();

                // If not connected to Internet
                if (!checkClass.isConnectedToInternet(context)) {
                    Toast.makeText(getBaseContext(), "⚠️ No internet connection !", Toast.LENGTH_SHORT).show();
                }

                // If connection to host cannot be established
                else if (!checkClass.isHostUp(ip)){
                    Log.d("TEST", Boolean.toString(checkClass.isHostUp(ip)));
                    Toast.makeText(getBaseContext(), "⚠️ Error connecting to Database", Toast.LENGTH_SHORT).show();
                }
                // If everything is ok, connects to API and sends data
                else {
                    login_post_request();
                }*/
                launch_page2();
            }
        });

        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch_QR_scan_activity();
            }
        });
    }
    //-----------------------------------------------------------
    // Function that destroy current activity and launch "Page2"
    //-----------------------------------------------------------
    private void launch_page2(){
        Intent intent = new Intent(this, Page2.class);
        startActivity(intent);
        finish();// Kills curent activity
    }

    //----------------------------------------------------------------------
    // Function that launch "Page_QR" (without destroying previous activity
    //----------------------------------------------------------------------
    private void launch_QR_scan_activity(){
        Intent intent = new Intent(this, Page_Qr.class);
        startActivity(intent);
    }

    //---------------------------------------------------------
    // Function that takes care of sending POST request to api
    //---------------------------------------------------------

    private void login_post_request() {

        String url = "http://" + ip + ":" + port + "/server/api/login/";
        JSONObject object = new JSONObject();
        try {
            object.put("pseudo",username.getText());
            object.put("password",password.getText());
        } catch (JSONException ignored) {}

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        launch_page2();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "⚠️ Incorrect username or password.", Toast.LENGTH_SHORT).show();
                username.setText("");
                password.setText("");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}