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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    public addProductPopup(final Activity activity)
    {
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

        ApiRequest apiRequest = new ApiRequest(activity);

        //Setup search bar ----------------------------------------------------------------------
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, products);
        search_bar = (AutoCompleteTextView)findViewById(R.id.product_search_bar);
        search_bar.setThreshold(3);
        search_bar.setAdapter(adapter);
        search_bar.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() == search_bar.getThreshold())
                {
                    apiRequest.getProductsFromPattern(products, codes, s.toString());
                    refillAdapter();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Setup UI zone for quantity selection -------------------------------------------------
        this.quantity = findViewById(R.id.text_quantity);
        this.nb_picker = findViewById(R.id.quantity_nb);
        this.nb_picker.setMaxValue(20);
        this.nb_picker.setMinValue(1);

        // Setup UI zone for owner selection ---------------------------------------------------
        this.owner = findViewById(R.id.text_owner);
        this.radioGroup = findViewById(R.id.radio_group);
        if(current_list_selected.contains("personal list"))
        {
            this.owner.setVisibility(View.INVISIBLE);
            this.radioGroup.setVisibility(View.INVISIBLE);
        }
        this.meRadio = findViewById(R.id.btn_owner);
        this.meRadio.setChecked(true);
        this.groupeRadio = findViewById(R.id.btn_group);

        //Setup Add Button ---------------------------------------------------------------------
        this.add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                apiRequest.addProductToList(activity, makeJson(), current_group_id);
                dismiss();
            }
        });
    }

    /*
    * Build the popup when called
    */
    public void build()
    {
        show();
        this.search_bar.setHint(search_bar_hint);
        this.quantity.setText(qty);
        this.owner.setText(own);
    }

    /*
    * Clear and refill the adapter of the AutoCompleteTextView
    * Called after each API request to get a list of product from the AutoCompleteTextView's pattern
    * */
    public void refillAdapter()
    {
        adapter.clear();

        for(int i=0; i<products.size(); i++){
            adapter.add(products.get(i));
        }
    }

    /*
    * Setup the json object to be put in the body of the API Request in order to add a product to the list
    */
    public JSONObject makeJson()
    {
        JSONObject json = new JSONObject();

        try
        {
            json.put("code", codes.get(search_bar.getText().toString()));
            json.put("quantity", nb_picker.getValue());

            if(groupeRadio.isChecked())
            {
                json.put("groupProduct", true);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return json;
    }
}
