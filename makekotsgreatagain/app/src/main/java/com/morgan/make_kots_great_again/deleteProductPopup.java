package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class deleteProductPopup extends Dialog
{
    private String yesButtonText, noButtonText, text;

    private TextView textView;
    private Button yesButton, noButton;

    private String current_user_token;

    public deleteProductPopup(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.delete_product_popup);

        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("MyPref", 0);
        current_user_token = pref.getString("token", null);

        this.text = "Souhaitez-vous supprimer ce produit de la liste";
        this.yesButtonText = "OUI";
        this.noButtonText = "NON";

        this.textView = findViewById(R.id.text);
        this.yesButton = findViewById(R.id.yesBtn);
        this.yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteProduct();
            }
        });

        this.noButton = findViewById(R.id.noBtn);
        this.noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
    }

    public void build()
    {
        show();
        this.textView.setText(text);
        this.yesButton.setText(yesButtonText);
        this.noButton.setText(noButtonText);
    }

    public void deleteProduct()
    {
        String url = "http://kotsapp.herokuapp.com/server/api/shoppingList/removeProduct/IDinList";
        JSONObject json = null;

        Request request = new Request.Builder()
                .header("Authorization", current_user_token)
                .post(RequestBody.create(MediaType.parse("application/json"), String.valueOf(json)))
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {/*Nothing*/}

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String responseBody = response.body().string();
            }
        });
    }
}
