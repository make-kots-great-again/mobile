package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class deleteProductPopup extends Dialog
{
    private TextView textView;
    private Button yesButton, noButton;
    private String current_user_token;
    private String yesButtonText, noButtonText, text;

    /**
     * Constructor of the popup
     *  @param activity : activity from which the popup is called
     * @param product_uid : Unique ID of the product that we want to delete
     */
    public deleteProductPopup(Activity activity, Product current_product) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.delete_product_popup);

        ApiRequest apiRequest = new ApiRequest(activity);
        current_user_token = apiRequest.token;

        //Setup string's of the popup
        this.text = "Souhaitez-vous supprimer ce produit de la liste ?";
        this.yesButtonText = "OUI";
        this.noButtonText = "NON";

        //Setup up text of the popup
        this.textView = findViewById(R.id.text);

        //Setup yes and no buttons
        this.yesButton = findViewById(R.id.yesBtn);
        this.yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make an API request to delete the product and leave the popup
                apiRequest.deleteProductRequest(activity, current_product);
                dismiss();
            }
        });

        this.noButton = findViewById(R.id.noBtn);
        this.noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * Build and show the the popup when called
     */
    public void build() {
        show();
        this.textView.setText(text);
        this.yesButton.setText(yesButtonText);
        this.noButton.setText(noButtonText);
    }
}
