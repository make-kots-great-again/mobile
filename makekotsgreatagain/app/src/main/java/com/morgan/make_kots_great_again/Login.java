package com.morgan.make_kots_great_again;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final Button button_sign = findViewById(R.id.button_sign);
        final Button button_scan = findViewById(R.id.button_scan);

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getBaseContext(), "Logging in...", Toast.LENGTH_SHORT);
                toast.show();
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
}