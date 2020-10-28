package com.morgan.make_kots_great_again;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    private static final int MY_CAMERA_REQUEST_CODE = 100;


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
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try
                {
                    //check if camera permissions are already granted
                    if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    {
                        cameraSource.start(holder);
                    }
                    else
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }
                }
                catch (IOException e)
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                restart_Activity();
            } else {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                launch_Login_Activity();
            }
        }
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
    public void LoginRequest(String token) throws Exception
    {
        OkHttpClient client = new OkHttpClient();
        String url = "https://kotsapp.herokuapp.com/server/api/user/groups";

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
        String unauthorized = "Unauthorized";
        return token.equals(unauthorized);
    }

    /*
    * Restart the current activity
    *
    * Only called when the user grant the camera permission to the application
    *
     */
    public void restart_Activity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
