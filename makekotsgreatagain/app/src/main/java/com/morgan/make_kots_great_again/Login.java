package com.morgan.make_kots_great_again;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
                Login_Post_request(username, password, "172.18.0.3:8000");

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

    //---------------------------------------------------------
    // Function that takes care of sending POST request to api
    //---------------------------------------------------------
    public void Login_Post_request(final TextView username, final TextView password, final String ip_port) {

        String url = "http://" + ip_port + "/server/api/login/";
        JSONObject object = new JSONObject();
        try {
            object.put("pseudo",username.getText());
            object.put("password",password.getText());
        } catch (JSONException ignored) {}

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { //Requete Fonctionne
                        String txt = "Welcome " + username.getText().toString();
                        Toast.makeText(getBaseContext(), txt, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { //Requete ERREUR
                Toast.makeText(getBaseContext(), "T'es qui toi !?", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}