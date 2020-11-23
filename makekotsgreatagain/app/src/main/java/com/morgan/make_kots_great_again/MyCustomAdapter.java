package com.morgan.make_kots_great_again;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private static Activity activity;
    private ArrayList<String> list;
    private ArrayList<String> items_owner;
    private ArrayList<String> items_quantity;
    private ArrayList<String> items_uid;
    private Context context;


    public MyCustomAdapter(ArrayList<String> list, ArrayList<String> items_owner, ArrayList<String> items_quantity, ArrayList<String> items_uid, Activity activity) {
        this.list = list;
        this.items_owner = items_owner;
        this.items_quantity = items_quantity;
        this.items_uid = items_uid;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
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
        listItemText.setText(cutLongText(list.get(position)));

        //Product OWNER
        TextView listItemOwnerText = view.findViewById(R.id.list_item_owner_string);
        String current_text = items_owner.get(position);

        listItemOwnerText.setText(current_text);
        if (current_text.equals("group")){
            listItemOwnerText.setTextColor(Color.parseColor("#3700B3"));
            listItemOwnerText.setText(current_text.toUpperCase());
        }
        else if (current_text.equals("Me")){
            listItemOwnerText.setTextColor(Color.parseColor("#ff00ff"));
        }

        //Product QUANTITY
        final TextView quantity = view.findViewById(R.id.quantity);
        quantity.setText(items_quantity.get(position));

        //Image Buttons (Moins et Plus)
        ImageButton deleteBtn = view.findViewById(R.id.delete_btn);
        ImageButton addBtn = view.findViewById(R.id.add_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (remove_one((String) quantity.getText(), items_uid.get(position)) != "error")
                {
                    quantity.setText(remove_one((String) quantity.getText(), items_uid.get(position)));
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (add_one((String) quantity.getText()) != "error"){
                    quantity.setText(add_one((String) quantity.getText()));
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

    private static String remove_one(String number_in_string, String uid){
        int number = Integer.parseInt(number_in_string);
        if (number >=2 && number <=20){
            return Integer.toString(number - 1);
        }

        if(number == 1){
            deleteProductPopup popup = new deleteProductPopup(activity, uid);
            popup.show();
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