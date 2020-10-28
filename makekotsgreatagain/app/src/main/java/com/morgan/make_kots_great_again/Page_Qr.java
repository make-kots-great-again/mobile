package com.morgan.make_kots_great_again;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Page_Qr extends AppCompatActivity {

    // VARIABLES
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;

    private String url = "https://kots-app.herokuapp.com/server/api/user/groups";

    private final String unauthorized = "Unauthorized";


    // ANDROIDS LIFE CYCLE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_qr);
        surfaceView = findViewById(R.id.cameraPreview);
        textView = findViewById(R.id.textView);

        //Setting up the
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        //Setting up the camera
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        //Setting up the SurfaceView to show what the camera sees
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder)
            {
                try
                {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        Toast.makeText(getApplicationContext(), "The application couldn't access to your camera", Toast.LENGTH_SHORT).show();
                        wait(500);
                        // ??? TODO ??? ask to user for authorization instead of going in the phone parameters
                        launch_Login_Activity();
                        return;
                    }

                    cameraSource.start(holder);
                }
                catch (IOException | InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height)
            {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder)
            {
                cameraSource.stop();
            }
        });

        //Setting up the detector for the qr code
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()
        {
            @Override
            public void release()
            {
                barcodeDetector.release();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections)
            {
                SparseArray<Barcode> codes = detections.getDetectedItems();

                if (codes.size() != 0)
                {
                    try
                    {
                        LoginRequest(codes.valueAt(0).displayValue);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    // METHODS

    /*
    * Send a request to the API.
    *
    * The request only contains a header
    *
    * @params token : the content of the QR Code whose supposed to be a token
    *
    */
    public void LoginRequest(String token)  throws Exception
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().header("Authorization", token).url(url).build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String responseBody = response.body().string();

                if(!isTokenWrong(responseBody))
                {
                    launch_page2();
                }
                else
                {
                    launch_Login_Activity();
                }
            }
        });
    }

    /*
    * Check the body of API's response
    *
    * @params token : body of the API request
    *
    * @return true : if the response is "Unauthorized"
    * @return false : if the response is something else than "Unauthorized"
    */
    public boolean isTokenWrong(String token)
    {
        return token.equals(unauthorized);
    }

    //Launch the activity "Login" when called
    public void launch_Login_Activity()
    {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    //Launch the activity "Page2" when called
    public void launch_page2()
    {
        Intent intent = new Intent(this, Page2.class);
        startActivity(intent);
        finish();
    }
}
