package com.morgan.make_kots_great_again;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

public class Page_Qr extends AppCompatActivity {

    private Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    private int REQUEST_ID_IMAGE_CAPTURE = 100;

    // METHODES DE CYCLE DE VIE ANDROID
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_qr);

        // Lauching camera application
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);

        //Stop activity when leave camera application
        finish();
    }

}
