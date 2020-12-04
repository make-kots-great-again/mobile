package com.morgan.make_kots_great_again;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {

    private static Activity activity;
    private String current_list_name;
    private ArrayList<Product> products =  new ArrayList<Product>();
    private ArrayList<Product> products_modified = new ArrayList<Product>();

    public MyCustomAdapter(String name, ArrayList<Product> products, ArrayList<Product> products_modified, Activity activity) {
        this.current_list_name = name;
        this.products = products;
        this.products_modified = products_modified;
        this.activity = activity;
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
            view = inflater.inflate(R.layout.listview_format, null);
        }

        //Product NAME
        TextView listItemText = view.findViewById(R.id.list_item_string);
        listItemText.setText(cutLongText(products.get(position).getProduct_name()));

        //Product OWNER
        TextView listItemOwnerText = view.findViewById(R.id.list_item_owner_string);
        String current_text = products.get(position).getProduct_owner();

        listItemOwnerText.setText(current_text);
        if (current_text.equals("GROUP")){
            listItemOwnerText.setTextColor(Color.parseColor("#3700B3"));
        }
        else if (current_text.equals("Me")){
            listItemOwnerText.setTextColor(Color.parseColor("#ff00ff"));
        }

        //Product QUANTITY
        final TextView quantity = view.findViewById(R.id.quantity);
        quantity.setText(Integer.toString(products.get(position).getProduct_quantity()));

        //Image Buttons (Moins et Plus)
        ImageButton deleteBtn = view.findViewById(R.id.delete_btn);
        ImageButton addBtn = view.findViewById(R.id.add_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!products.get(position).can_modify_product_quantity()){
                    deleteBtn.setClickable(false);
                }
                else {
                    if (Integer.parseInt(quantity.getText().toString()) == 1) {
                        deleteProductPopup popup = new deleteProductPopup(activity, products.get(position));

                        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                ApiRequest apiRequest = new ApiRequest(activity);
                                Page2 page2 = new Page2();

                                products.clear();
                                apiRequest.Get_Shopping_Lists_items(products, products_modified, current_list_name, activity);
                            }
                        });
                        popup.show();
                    }

                    else if (remove_one((String) quantity.getText()) != "error") {
                        quantity.setText(remove_one((String) quantity.getText()));
                        products_modified.get(position).reduce_quantity();
                    }
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!products.get(position).can_modify_product_quantity()){
                    addBtn.setClickable(false);
                }
                else {
                    if (add_one((String) quantity.getText()) != "error") {
                        quantity.setText(add_one((String) quantity.getText()));
                        products_modified.get(position).add_quantity();
                    }
                }
            }
        });

        return view;
    }

    //--------------------------------------------------------------------------------------------
    // Function that takes a String that represents a number and returns the number-1 in a String
    //--------------------------------------------------------------------------------------------

    /* @param (String) => Takes a String that represents a number
    *  @return (String) => Returns number-1 in form of a String */

    private static String remove_one(String number_in_string){
        int number = Integer.parseInt(number_in_string);
        if (number >=2 && number <=20){
            return Integer.toString(number - 1);
        }

        return "error";
    }

    //--------------------------------------------------------------------------------------------
    // Function that takes String that represents a number and returns the number+1 in a String
    //--------------------------------------------------------------------------------------------

    /* @param (String) => Takes a String that represents a number
     *  @return (String) => Returns number+1 in form of a String */

    private static String add_one(String number_in_string){
        int number = Integer.parseInt(number_in_string);
        if (number >=1 && number <20){
            return Integer.toString(number + 1);
        }
        return "error";
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