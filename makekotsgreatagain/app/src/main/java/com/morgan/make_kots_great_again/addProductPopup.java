package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;

public class addProductPopup extends Dialog {

    private AutoCompleteTextView search_bar;
    private NumberPicker nb_picker;
    private RadioGroup radioGroup;
    private RadioButton groupeRadio, meRadio;
    private TextView quantity, owner;
    private Button add_btn;

    private String qty, own, search_bar_hint;
    private String current_user_name, current_user_token, current_group_id, current_list_selected;

    ArrayAdapter<String> adapter;

    private ArrayList<String> products = new ArrayList<>();
    private Map<String, Integer> codes = new HashMap<>();

    //CONSTRUCTOR
    public addProductPopup(final Activity activity){
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.add_new_product_popup);

        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("MyPref", 0);
        current_user_name = pref.getString("username", null);
        current_user_token = pref.getString("token", null);
        current_group_id = pref.getString("group_id", null);
        current_list_selected = pref.getString("list", null);

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
                if(s.length() == search_bar.getThreshold()){
                    getProductsFromPattern((s.toString()));
                    refillAdapter();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        this.quantity = findViewById(R.id.text_quantity);
        this.nb_picker = findViewById(R.id.quantity_nb);
        this.nb_picker.setMaxValue(20);
        this.nb_picker.setMinValue(1);

        this.owner = findViewById(R.id.text_owner);
        this.radioGroup = findViewById(R.id.radio_group);
        if(current_list_selected.contains("personal list")){
            this.owner.setVisibility(View.INVISIBLE);
            this.radioGroup.setVisibility(View.INVISIBLE);
        }
        this.meRadio = findViewById(R.id.btn_owner);
        this.meRadio.setChecked(true);
        this.groupeRadio = findViewById(R.id.btn_group);

        this.add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String url = "http://kotsapp.herokuapp.com/server/api/shoppingList/addProduct/" + current_group_id;

                JSONObject json = new JSONObject();
                try {
                    json.put("code", codes.get(search_bar.getText().toString()));
                    json.put("quantity", nb_picker.getValue());

                    if(groupeRadio.isChecked()){
                        json.put("groupProduct", true);
                     }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), String.valueOf(json));

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().header("Authorization", current_user_token).url(url).post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException{
                        try {
                            String responseBody = response.body().string();

                            final JSONObject Jobject = new JSONObject(responseBody);

                            Log.d("ok", responseBody.toString());

                            Log.d("msg", Jobject.getString("message"));
                            Log.d("error", Jobject.getString("error"));

                            //activity.Get_Shopping_Lists_items("https://kotsapp.herokuapp.com/server/api/shoppingList/", );

                            if(Jobject.getString("success").equals("true")){
                                //return to page2 update list
                            }

                            activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        Toast.makeText(activity, Jobject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    /*
    * Build the popup when called
    */
    public void build(){
        show();
        this.search_bar.setHint(search_bar_hint);
        this.quantity.setText(qty);
        this.owner.setText(own);
    }

    /*
    * Clear and refill the adapter of the AutoCompleteTextView
    * Called after each API request to get a list of product from the AutoCompleteTextView's pattern
    * */
    public void refillAdapter(){
        adapter.clear();

        for(int i=0; i<products.size(); i++){
            adapter.add(products.get(i));
        }
    }

    /*
    * Make an API request to get a list of product that contains the pattern
    *
    * @param patter : current text written in the AutoCompleteTextView
    * */
    public void getProductsFromPattern(String pattern){
        String url = "http://kotsapp.herokuapp.com/server/api/products/" + pattern;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().header("Authorization", current_user_token ).url(url).build();

        Log.d("ok", "send request");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                try {
                    JSONObject Jobject = new JSONObject(responseBody);
                    JSONArray Jarray = Jobject.getJSONArray("products");

                    products.clear();

                    for(int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        String product_name = object.getString("product_name");
                        String product_code = object.getString("code");

                        products.add(product_name);

                        codes.put(product_name, Integer.parseInt(product_code));
                    }

                } catch (JSONException ignored) { }
            }
        });
    }

}
