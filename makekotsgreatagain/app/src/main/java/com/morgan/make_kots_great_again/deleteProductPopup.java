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
     *
     * @param activity : activity from which the popup is called
     * @param product_uid : Unique ID of the product that we want to delete
     */
    public deleteProductPopup(final Activity activity, final String product_uid)
    {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.delete_product_popup);

        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("MyPref", 0);
        current_user_token = pref.getString("token", null);

        //Setup string's of the popup ----------------------------------------------
        this.text = "Souhaitez-vous supprimer ce produit de la liste ?";
        this.yesButtonText = "OUI";
        this.noButtonText = "NON";

        //Setup up text of the popup -----------------------------------------------
        this.textView = findViewById(R.id.text);

        //Setup yes and no buttons -----------------------------------------------
        this.yesButton = findViewById(R.id.yesBtn);
        this.yesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Make a request API to delete the product and leave the popup
                deleteProductRequest(activity, product_uid);
                dismiss();
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

    /**
     * Build and show the the popup when called
     */
    public void build()
    {
        show();
        this.textView.setText(text);
        this.yesButton.setText(yesButtonText);
        this.noButton.setText(noButtonText);
    }

    /**
     * Build and send a API request in order to delete a product
     *
     * The Unique ID of the product allows to know in which shoppingList it's located
     *
     * @param activity : activity calling the popup
     * @param uidProduct : Unique ID of the product that we want to delete
     */
    public void deleteProductRequest(final Activity activity, String uidProduct)
    {
        String url = "https://kotsapp.herokuapp.com/server/api/shoppingList/removeProduct/" + uidProduct;

        Request request = new Request.Builder()
                .header("Authorization", current_user_token)
                .url(url)
                .delete()
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

                final JSONObject Jobject;

                try
                {
                    Jobject = new JSONObject(responseBody);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                Toast.makeText(activity, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
