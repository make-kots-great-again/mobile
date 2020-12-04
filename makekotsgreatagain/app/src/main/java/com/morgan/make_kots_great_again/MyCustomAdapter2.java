package com.morgan.make_kots_great_again;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyCustomAdapter2 extends BaseAdapter implements ListAdapter {

    private static Activity activity;
    private ArrayList<Product> products;
    private String current_list_selected;

    public MyCustomAdapter2(ArrayList<Product> products, Activity activity) {
        this.products = products;
        this.activity = activity;
        SharedPreferences pref = activity.getSharedPreferences("MyPref", 0);
        current_list_selected = pref.getString("list", null);
        Collections.sort(products, Comparator.comparing(Product::getProduct_owner));
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int pos) {
        return products.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_format2, null);
        }

        //Checkbox
        CheckBox checkBox = view.findViewById(R.id.product_checkbox);

        //Product NAME
        final TextView listItemText = view.findViewById(R.id.product_name);
        listItemText.setText(cutLongText(products.get(position).getProduct_name()));

        //Product BRAND
        final TextView brand = view.findViewById(R.id.product_brand);
        brand.setText(cutLongText(products.get(position).getProduct_brand()));

        //Product OWNER
        TextView listItemOwnerText = view.findViewById(R.id.product_owner);
        String current_text = products.get(position).getProduct_owner();

        listItemOwnerText.setText(current_text);
        if (current_text.equals("GROUP")){
            listItemOwnerText.setTextColor(Color.parseColor("#3700B3"));
        }
        else if (current_text.equals("Me")){
            listItemOwnerText.setTextColor(Color.parseColor("#ff00ff"));
        }

        //Product QUANTITY
        final TextView quantity = view.findViewById(R.id.product_quantity);
        quantity.setText(Integer.toString(products.get(position).getProduct_quantity()));

        //Constraint layout
        final ConstraintLayout constraintLayout = view.findViewById(R.id.constraint_layout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
                if (checkBox.isChecked()){
                    listItemText.setPaintFlags(listItemText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    brand.setPaintFlags(brand.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    listItemText.setPaintFlags(listItemText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    brand.setPaintFlags(brand.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                String snackbar_text = products.get(position).getProduct_note();
                if (!snackbar_text.equals("null")){
                    Snackbar snackbar = Snackbar.make(constraintLayout, snackbar_text, Snackbar.LENGTH_SHORT);

                    View view = snackbar.getView();
                    TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
                    tv.setTextColor(Color.MAGENTA);

                    snackbar.show();
                }
            }
        });

        return view;
    }

    private static String cutLongText(String string){
        if (string.length() >=22){
            StringBuilder str = new StringBuilder(string);
            str.replace(22, string.length(), "...");
            return str.toString();
        }
        else { // String is not bigger than or equal to 20
            return string;
        }
    }
}