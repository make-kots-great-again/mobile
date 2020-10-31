package com.morgan.make_kots_great_again;

import android.app.Activity;
import android.app.Dialog;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class addProductPopup extends Dialog {

    private NumberPicker nb_picker;
    private Spinner spinner;
    private TextView quantity, owner, find_product_bar;

    private String qty, own, product_bar_hint;

    public addProductPopup(Activity activity){
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.add_new_product_popup);

        this.qty = "Quantité :";
        this.own = "Propriétaire :";
        this.product_bar_hint = "Rechercher un produit";

        this.find_product_bar = findViewById(R.id.product_search_bar);
        this.quantity = findViewById(R.id.text_quantity);
        this.owner = findViewById(R.id.text_owner);
        this.nb_picker = findViewById(R.id.quantity_nb);
        this.spinner = findViewById(R.id.spinner);
    }

    public void build(){
        show();
        this.find_product_bar.setHint(product_bar_hint);
        this.quantity.setText(qty);
        this.owner.setText(own);
    }
}
