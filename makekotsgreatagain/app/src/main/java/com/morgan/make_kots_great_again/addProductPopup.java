package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class addProductPopup extends Dialog {

    private AutoCompleteTextView search_bar;
    private NumberPicker nb_picker;
    private RadioGroup radioGroup;
    private RadioButton groupeRadio, meRadio;
    private TextView quantity, owner;
    private Button add_btn;
    private EditText input_note;

    private String qty, own, search_bar_hint;

    ArrayAdapter<String> adapter;

    private ArrayList<Product> products2 = new ArrayList<>();
    private ArrayList<String> products = new ArrayList<>();
    private Map<String, Integer> codes = new HashMap<>();

    //CONSTRUCTOR
    public addProductPopup(Activity activity, List current_list) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.add_new_product_popup);

        this.qty = "Quantité :";
        this.own = "Propriétaire :";
        this.search_bar_hint = "Rechercher un produit";

        ApiRequest apiRequest = new ApiRequest(activity);

        //Setup search bar ----------------------------------------------------------------------
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, products);
        search_bar = findViewById(R.id.product_search_bar);
        search_bar.setThreshold(3);
        search_bar.setAdapter(adapter);
        search_bar.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == search_bar.getThreshold()) {
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

        if(current_list.is_list_personal()) {
            this.owner.setVisibility(View.INVISIBLE);
            this.radioGroup.setVisibility(View.INVISIBLE);
        }

        this.meRadio = findViewById(R.id.btn_owner);
        this.meRadio.setChecked(true);
        this.groupeRadio = findViewById(R.id.btn_group);
        // Setup UI zone for product note
        this.input_note = findViewById(R.id.edit_note);

        //Setup Add Button ---------------------------------------------------------------------
        this.add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_note.getText().length() >= 55){
                    Toast.makeText(activity, "⚠️ La note pour ce produit est trop longue !", Toast.LENGTH_SHORT).show();
                }
                else {
                    apiRequest.addProductToList(activity, makeJson(), current_list);

                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException ignored) { }

                    dismiss();
                }
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
            if (input_note.getText().length() == 0){
                json.put("productNote", "null");
            }
            else {
                json.put("productNote", input_note.getText());
            }
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
