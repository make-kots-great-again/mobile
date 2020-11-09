package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class addProductPopup extends Dialog {

    private AutoCompleteTextView search_bar;
    private NumberPicker nb_picker;
    private Spinner spinner;
    private TextView quantity, owner;
    private Button add_btn;

    private String qty, own, search_bar_hint;
    private String current_user_name, current_user_token;

    ArrayAdapter<String> adapter;

    private ArrayList<String> products = new ArrayList<>();

    public addProductPopup(Activity activity){
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.add_new_product_popup);

        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("MyPref", 0);
        current_user_name = pref.getString("username", null);
        current_user_token = pref.getString("token", null);

        //getProductFromPattern(("O"));

        this.qty = "Quantité :";
        this.own = "Propriétaire :";
        this.search_bar_hint = "Rechercher un produit";

        //Setup search bar
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, products);
        search_bar = (AutoCompleteTextView)findViewById(R.id.product_search_bar);
        search_bar.setThreshold(3);
        search_bar.setAdapter(adapter);
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == search_bar.getThreshold()){
                    getProductFromPattern((s.toString()));
                    refillAdapter();
                }
            }
        });

        this.quantity = findViewById(R.id.text_quantity);
        this.nb_picker = findViewById(R.id.quantity_nb);
        this.nb_picker.setMaxValue(20);
        this.nb_picker.setMinValue(1);

        this.owner = findViewById(R.id.text_owner);
        this.spinner = findViewById(R.id.spinner);

        this.add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String product_name = search_bar.getText().toString();
                int product_quantity = nb_picker.getValue();

                String url = "http://localhost:8000/server/api/qqch";

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().header("Authorization", current_user_token ).url(url).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //update list view
                    }
                });
            }
        });
    }


    public void build(){
        show();
        this.search_bar.setHint(search_bar_hint);
        this.quantity.setText(qty);
        this.owner.setText(own);
    }

    public void refillAdapter(){
        for(int i=0; i<products.size(); i++){
            adapter.add(products.get(i));
        }
    }

    public void getProductFromPattern(String pattern){
        String url = "http://kotsapp.herokuapp.com/server/api/products/" + pattern;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().header("Authorization", current_user_token ).url(url).build();

        Log.d("ok", "send request");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("ok", "response received");

                String responseBody = response.body().string();
                try {
                    JSONObject Jobject = new JSONObject(responseBody);
                    JSONArray Jarray = Jobject.getJSONArray("products");

                    products.clear();

                    for(int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        String product_name = object.getString("product_name");

                        products.add(product_name);
                    }

                } catch (JSONException ignored) { }
            }
        });
    }

}
