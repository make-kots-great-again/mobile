package com.morgan.make_kots_great_again;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    TextView username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button button_sign = findViewById(R.id.button_sign);
        final Button button_scan = findViewById(R.id.button_scan);
        username = findViewById(R.id.edit_mail);
        password = findViewById(R.id.edit_password);

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String details ="";
                details += "username : " + username.getText() + "\n";
                details += "password : " + password.getText();
                Toast toast = Toast.makeText(getBaseContext(), details, Toast.LENGTH_SHORT);
                toast.show();*/
                postData();

            }
        });

        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getBaseContext(), "Launching QR Scan activity...", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
    //-----------------------
    // Post Request For JSONObject
    public void postData() {

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("pseudo",username.getText());
            object.put("password",password.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://172.18.0.3:8000/server/api/login/", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getBaseContext(), "String Response : " + response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            String success = response.getString("success");
                            String txt = "String Response : " + success +"\n";
                            txt += "Welcome " + username.getText().toString();
                            Toast.makeText(getBaseContext(), txt, Toast.LENGTH_SHORT).show();
                            Log.d("JSON", String.valueOf(response));
                            String Error = response.getString("httpStatus");
                            if (Error.equals("") || Error.equals(null)) {

                            } else if (Error.equals("OK")) {
                                JSONObject body = response.getJSONObject("body");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(getBaseContext(), "T'es qui toi !?", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
    //-----------------------
}